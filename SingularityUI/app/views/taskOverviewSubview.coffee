View = require './view'
Task = require '../models/Task'

killTemplate = require '../templates/vex/taskKill'
killOverrideTemplate = require '../templates/vex/taskKillOverride'
killDestroyTemplate = require '../templates/vex/taskKillDestroy'


class taskOverviewSubview extends View

    events: ->
        _.extend super,
            'click [data-action="remove"]': 'promptKillTask'
            'click [data-action="viewHealthchecks"]': 'triggerToggleHealthchecks'

    initialize: ({@collection, @model, @template, @deploys}) ->

        @taskModel = new Task id: @model.taskId
        @shouldPollHealthchecks = true

        for eventName in ['sync', 'add', 'remove', 'change']
            @listenTo @model, eventName, @render
            @listenTo @deploys, eventName, @render

        # copy latest task cleanup object over to the task model whenever things change
        for eventName in ['add', 'reset']
            @listenTo @collection, eventName, =>
                taskId = @model.get 'taskId'
                cleanup = _.last(@collection.filter (c) -> c.get('taskId').id is taskId)

                if cleanup
                    @model.set 'cleanup', cleanup.attributes
                else
                    @model.unset 'cleanup'

        @listenTo @model, 'reset', =>
            @$el.empty()


    render: ->
        return if not @model.synced or not @deploys.synced
        @checkForPendingDeploy() if not @isDeployPending and @shouldPollHealthchecks
        @$el.html @template @renderData()

    renderData: ->
        config:           config
        data:             @model.toJSON()
        isDeployPending:  @isDeployPending
        synced:           @model.synced and @collection.synced and @deploys.synced

    # If we have a deploy pending for this task,
    # we start polling until we get a healthcheck to show
    checkForPendingDeploy: ->
        @isDeployPending = false
        for deploy in @deploys.toJSON()
            if deploy.deployMarker.deployId is @model.get('task').taskId.deployId
                @isDeployPending = true
                @pollForHealthchecks()
                break

    # Poll for Healthchecks if the task 
    # is part of a pending deploy
    pollForHealthchecks: =>
        do fetchModel = =>
            @model.fetch().done =>
                if @model.get('healthcheckResults').length > 0
                    @render()
                    return @shouldPollHealthchecks = false
                    
                setTimeout ( => fetchModel() ), 1500
                   
    triggerToggleHealthchecks: ->
        @trigger 'toggleHealthchecks'

    # Choose prompt based on if we plan to 
    # gracefully kill (sigterm), or force kill (kill-9)
    promptKillTask: =>
        @model.fetch().done =>
            if @model.get 'isStillRunning'
                @collection.fetch({reset: true}).done =>
                    if @model.has('cleanup') and not @model.get('cleanup').isImmediate
                        btnText = 'Override'
                        templ = killOverrideTemplate
                    else if @model.get 'isCleaning'
                        btnText = 'Destroy task'
                        templ = killDestroyTemplate
                    else
                        btnText = 'Kill task'
                        templ = killTemplate

                    vex.dialog.confirm
                        buttons: [
                            $.extend {}, vex.dialog.buttons.YES,
                                text: btnText
                                className: 'vex-dialog-button-primary vex-dialog-button-primary-remove'
                            vex.dialog.buttons.NO
                        ]
                        message: templ id: @model.taskId

                        callback: (confirmed) =>
                            @killTask() if confirmed


    killTask: =>
        @taskModel.kill(@model.has('cleanup') or @model.get('isCleaning'))
            .done (data) =>
                @collection.add [data], parse: true  # automatically response  object to the cleanup collection
            .error (response) =>
                if response.status is 409  # HTTP 409 means a cleanup is already going on, nothing to flip out about
                    app.caughtError()
                    @collection.add [response.responseJSON], parse: true


module.exports = taskOverviewSubview
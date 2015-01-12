View = require './view'

# You feed it a collection/model & template and it listens to it and renders
# when appropriate
#
#   myView = new SimpleSubview {collection, template}
#   @$('#my-container').html myView.$el
#
# And it does everything for you, just do stuff with the collection
class ResourceSubview extends View

    expanded: false

    events: ->
        _.extend super,
            'click [data-action="expandToggle"]': 'expandToggle'

    initialize: (@params) ->
        { @template } = @params
        @data = @model
        @lineData = d3.range()
        @lineData.push
            timestamp: new Date()
            v: 0

        for eventName in ['sync', 'add', 'remove', 'change']
            @listenTo @data, eventName, @render

        @listenTo @data, 'reset', =>
            @$el.empty()

        @refreshInterval()

    refreshInterval: ->
        setInterval @render.bind(this), 2000

    render: ->
        tick = ->
            now = new Date()
            x.domain [now - (n - 2) * duration, now - duration]
            y.domain [0, 5]
            svg.select(".line").attr("d", line).attr "transform", null
            axis.transition().duration(duration).ease("linear").call x.axis
            path.transition().duration(duration).ease("linear").attr("transform", "translate(" + x(now - (n - 1) * duration) + ")").each "end", tick

        n = 243
        duration = 750
        now = new Date(Date.now() - duration)
        count = 0
        margin =
            top: 6
            right: 0
            bottom: 20
            left: 40

        width = 400 - margin.right
        height = 120 - margin.top - margin.bottom
        x = d3.time.scale().domain([now - (n - 2) * duration, now - duration]).range([0, width])
        y = d3.scale.linear().range([height, 0])
        line = d3.svg.line()
            .interpolate("basis")
            .x((d) ->
                x d.timestamp
            ).y((d) ->
                y d.v
            )
        if d3.select("#chart")[0][0]
            svg = d3.select("#chart")
        else
            svg = d3.select("#resources")
                .append("p")
                .append("svg")
                .attr("width", width + margin.left + margin.right)
                .attr("height", height + margin.top + margin.bottom)
                .attr("id", "chart")
                .style("margin-left", -margin.left + "px")
                .append("g")
                .attr("transform", "translate(" + margin.left + "," + margin.top + ")")
            svg.append("defs")
                .append("clipPath")
                .attr("id", "clip")
                .append("rect")
                .attr("width", width)
                .attr "height", height
        if d3.select("#xaxis")[0][0]
            axis = d3.select("#xaxis")
        else
            axis = svg.append("g")
                .attr("class", "x axis")
                .attr("id", "xaxis")
                .attr("transform", "translate(0," + height + ")")

        axis.call(x.axis = d3.svg.axis().scale(x).orient("bottom"))

        path = svg.append("g")
            .attr("clip-path", "url(#clip)")
            .attr("id", "clippath")
            .append("path")
            .data([@lineData])
            .attr("class", "line")
        #timestamp = new Date()
        #val = timestamp.getSeconds()
        timestamp = new Date(@data.attributes.timestamp*1000)
        val = @data.attributes.cpusUserTimeSecs
        @lineData.push
            timestamp: timestamp
            v: val

        tick()

        #@$el.html @template(@renderData())

        utils.setupCopyLinks @$el if @$('.horizontal-description-list').length

    renderData: ->
        data =
            config:    config
            data:      @data.toJSON()
            synced:    @data.synced
            expanded:  @expanded
        if @params.extraRenderData?
            _.extend data, @params.extraRenderData(this)

        data

    expandToggle: (event) ->
        @expanded = not @expanded
        @render()

module.exports = ResourceSubview

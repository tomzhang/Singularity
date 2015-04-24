package com.hubspot.singularity.docker.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DockerContainerState {

  @JsonProperty("Running") private boolean running;
  @JsonProperty("Paused") private boolean paused;
  @JsonProperty("Pid") private int pid;
  @JsonProperty("ExitCode") private int exitCode;
  @JsonProperty("StartedAt") private String startedAt;
  @JsonProperty("FinishedAt") private String finishedAt;

  public boolean isRunning() {
    return running;
  }

  public boolean isPaused() {
    return paused;
  }

  public int getPid() {
    return pid;
  }

  public int getExitCode() {
    return exitCode;
  }

  public String getStartedAt() {
    return startedAt;
  }

  public String getFinishedAt() {
    return finishedAt;
  }
}


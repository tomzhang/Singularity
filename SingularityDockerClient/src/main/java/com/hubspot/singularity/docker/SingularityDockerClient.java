package com.hubspot.singularity.docker;

import java.util.List;

import com.google.inject.Singleton;

@Singleton
public class SingularityDockerClient {
  public static final String HTTP_CLIENT_NAME = "docker.http.client";

  public void removeContainer(String containerName) {

  }

  public List<Object> listContainers() {
    return null;
  }

  public void pull(String imageName) {

  }

  public Object inspectContainer(String containerName) {
    return null;
  }
}

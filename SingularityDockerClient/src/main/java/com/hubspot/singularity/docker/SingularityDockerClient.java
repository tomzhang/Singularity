package com.hubspot.singularity.docker;

import java.util.List;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.hubspot.horizon.HttpClient;
import com.hubspot.horizon.HttpRequest;
import com.hubspot.horizon.HttpResponse;
import com.hubspot.singularity.docker.exceptions.DockerException;
import com.hubspot.singularity.docker.models.DockerContainer;
import com.hubspot.singularity.docker.models.DockerContainerInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class SingularityDockerClient {
  private static final Logger LOG = LoggerFactory.getLogger(SingularityDockerClient.class);

  public static final String HTTP_CLIENT_NAME = "docker.http.client";
  public static final String UNIX_ENDPOINT = "unix://localhost/var/run/docker.sock";

  public static final String LIST_CONTAINERS_FORMAT = UNIX_ENDPOINT + "/containers/json";
  public static final String INSPECT_CONTAINER_PATH_FORMAT = UNIX_ENDPOINT + "/containers/%s/json";
  public static final String REMOVE_CONTAINER_PATH_FORMAT = UNIX_ENDPOINT +"/containers/%s";
  public static final String PULL_IMAGE_FORMAT = UNIX_ENDPOINT + "/images/create";

  private final HttpClient httpClient;

  @Inject
  public SingularityDockerClient(@Named(SingularityDockerClient.HTTP_CLIENT_NAME)HttpClient httpClient) {
    this.httpClient = httpClient;
  }

  public void removeContainer(String containerName, boolean force) throws DockerException {
    HttpRequest request = HttpRequest.newBuilder()
      .setMethod(HttpRequest.Method.DELETE)
      .addQueryParam("force", force)
      .setUrl(String.format(REMOVE_CONTAINER_PATH_FORMAT, containerName))
      .build();
    HttpResponse response = httpClient.execute(request);
    LOG.info(response.getAsString());
    if (response.isError()) {
      throw new DockerException(response.getAsString());
    }
  }

  public List<DockerContainer> listContainers() throws DockerException {
    HttpRequest request = HttpRequest.newBuilder()
      .setMethod(HttpRequest.Method.GET)
      .setUrl(String.format(LIST_CONTAINERS_FORMAT))
      .build();
    return httpClient.execute(request).getAs(DockerContainer.class.asSubclass(List.class));
  }

  public void pull(String image) throws DockerException {
    HttpRequest request = HttpRequest.newBuilder()
      .addQueryParam("fromImage", image)
      .setMethod(HttpRequest.Method.POST)
      .setUrl(String.format(PULL_IMAGE_FORMAT))
      .build();
    HttpResponse response = httpClient.execute(request);
    LOG.info(response.getAsString());
  }

  public DockerContainerInfo inspectContainer(String containerName) throws DockerException {
    HttpRequest request = HttpRequest.newBuilder()
      .setMethod(HttpRequest.Method.GET)
      .setUrl(String.format(INSPECT_CONTAINER_PATH_FORMAT, containerName))
      .build();
    return httpClient.execute(request).getAs(DockerContainerInfo.class);
  }
}

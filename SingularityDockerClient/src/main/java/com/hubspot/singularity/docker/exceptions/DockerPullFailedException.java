package com.hubspot.singularity.docker.exceptions;

public class DockerPullFailedException extends DockerException {

  private final String image;

  public DockerPullFailedException(final String image, final Throwable cause) {
    super("Image pull failed: " + image, cause);
    this.image = image;
  }

  public DockerPullFailedException(final String image, final String message) {
    super("Image pull failed: " + image + ": " + message);
    this.image = image;
  }

  public String getImage() {
    return image;
  }
}

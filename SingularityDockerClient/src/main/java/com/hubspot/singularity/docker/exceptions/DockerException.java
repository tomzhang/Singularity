package com.hubspot.singularity.docker.exceptions;

public class DockerException extends Exception {

  public DockerException(final String message) {
    super(message);
  }

  public DockerException(final String message, final Throwable cause) {
    super(message, cause);
  }

  public DockerException(final Throwable cause) {
    super(cause);
  }
}

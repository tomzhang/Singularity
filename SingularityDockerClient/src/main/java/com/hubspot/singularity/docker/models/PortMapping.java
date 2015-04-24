package com.hubspot.singularity.docker.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;

public class PortMapping {

  @JsonProperty("PrivatePort") private int privatePort;
  @JsonProperty("PublicPort") private int publicPort;
  @JsonProperty("Type") private String type;
  @JsonProperty("IP") private String ip;

  public String getIp() {
    return ip;
  }

  public int getPrivatePort() {
    return privatePort;
  }

  public int getPublicPort() {
    return publicPort;
  }

  public String getType() {
    return type;
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    final PortMapping that = (PortMapping) o;

    if (privatePort != that.privatePort) {
      return false;
    }
    if (publicPort != that.publicPort) {
      return false;
    }
    if (ip != null ? !ip.equals(that.ip) : that.ip != null) {
      return false;
    }
    if (type != null ? !type.equals(that.type) : that.type != null) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int result = privatePort;
    result = 31 * result + publicPort;
    result = 31 * result + (type != null ? type.hashCode() : 0);
    result = 31 * result + (ip != null ? ip.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return Objects.toStringHelper(this)
      .add("privatePort", privatePort)
      .add("publicPort", publicPort)
      .add("type", type)
      .add("ip", ip)
      .toString();
  }
}

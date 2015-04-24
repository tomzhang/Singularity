package com.hubspot.singularity.docker.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;

public class LxcParameter {

  @JsonProperty("Key") private String key;
  @JsonProperty("Value") private String value;

  public LxcParameter(final String key, final String value) {
    this.key = key;
    this.value = value;
  }

  public String key() {
    return key;
  }

  public String value() {
    return value;
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    final LxcParameter that = (LxcParameter) o;

    if (key != null ? !key.equals(that.key) : that.key != null) {
      return false;
    }
    if (value != null ? !value.equals(that.value) : that.value != null) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int result = key != null ? key.hashCode() : 0;
    result = 31 * result + (value != null ? value.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return Objects.toStringHelper(this)
      .add("key", key)
      .add("value", value)
      .toString();
  }
}


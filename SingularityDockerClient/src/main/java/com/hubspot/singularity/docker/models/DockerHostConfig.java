package com.hubspot.singularity.docker.models;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;

@JsonAutoDetect(fieldVisibility = ANY, getterVisibility = NONE, setterVisibility = NONE)
public class DockerHostConfig {

  @JsonProperty("Binds") private ImmutableList<String> binds;
  @JsonProperty("ContainerIDFile") private String containerIDFile;
  @JsonProperty("LxcConf") private ImmutableList<LxcParameter> lxcConf;
  @JsonProperty("Privileged") private Boolean privileged;
  @JsonProperty("PortBindings") private Map<String, List<PortBinding>> portBindings;
  @JsonProperty("Links") private ImmutableList<String> links;
  @JsonProperty("PublishAllPorts") private Boolean publishAllPorts;
  @JsonProperty("Dns") private ImmutableList<String> dns;
  @JsonProperty("DnsSearch") private ImmutableList<String> dnsSearch;
  @JsonProperty("VolumesFrom") private ImmutableList<String> volumesFrom;
  @JsonProperty("NetworkMode") private String networkMode;


  public List<String> binds() {
    return binds;
  }

  public String containerIDFile() {
    return containerIDFile;
  }

  public List<LxcParameter> lxcConf() {
    return lxcConf;
  }

  public Boolean privileged() {
    return privileged;
  }

  public Map<String, List<PortBinding>> portBindings() {
    return (portBindings == null) ? null : Collections.unmodifiableMap(portBindings);
  }

  public List<String> links() {
    return links;
  }

  public Boolean publishAllPorts() {
    return publishAllPorts;
  }

  public List<String> dns() {
    return dns;
  }

  public List<String> dnsSearch() {
    return dnsSearch;
  }

  public List<String> volumesFrom() {
    return volumesFrom;
  }

  public String networkMode() {
    return networkMode;
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    final DockerHostConfig that = (DockerHostConfig) o;

    if (binds != null ? !binds.equals(that.binds) : that.binds != null) {
      return false;
    }
    if (containerIDFile != null ? !containerIDFile.equals(that.containerIDFile)
      : that.containerIDFile != null) {
      return false;
    }
    if (dns != null ? !dns.equals(that.dns) : that.dns != null) {
      return false;
    }
    if (dnsSearch != null ? !dnsSearch.equals(that.dnsSearch) : that.dnsSearch != null) {
      return false;
    }
    if (links != null ? !links.equals(that.links) : that.links != null) {
      return false;
    }
    if (lxcConf != null ? !lxcConf.equals(that.lxcConf) : that.lxcConf != null) {
      return false;
    }
    if (networkMode != null ? !networkMode.equals(that.networkMode) : that.networkMode != null) {
      return false;
    }
    if (portBindings != null ? !portBindings.equals(that.portBindings)
      : that.portBindings != null) {
      return false;
    }
    if (privileged != null ? !privileged.equals(that.privileged) : that.privileged != null) {
      return false;
    }
    if (publishAllPorts != null ? !publishAllPorts.equals(that.publishAllPorts)
      : that.publishAllPorts != null) {
      return false;
    }
    if (volumesFrom != null ? !volumesFrom.equals(that.volumesFrom) : that.volumesFrom != null) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int result = binds != null ? binds.hashCode() : 0;
    result = 31 * result + (containerIDFile != null ? containerIDFile.hashCode() : 0);
    result = 31 * result + (lxcConf != null ? lxcConf.hashCode() : 0);
    result = 31 * result + (privileged != null ? privileged.hashCode() : 0);
    result = 31 * result + (portBindings != null ? portBindings.hashCode() : 0);
    result = 31 * result + (links != null ? links.hashCode() : 0);
    result = 31 * result + (publishAllPorts != null ? publishAllPorts.hashCode() : 0);
    result = 31 * result + (dns != null ? dns.hashCode() : 0);
    result = 31 * result + (dnsSearch != null ? dnsSearch.hashCode() : 0);
    result = 31 * result + (volumesFrom != null ? volumesFrom.hashCode() : 0);
    result = 31 * result + (networkMode != null ? networkMode.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return Objects.toStringHelper(this)
      .add("binds", binds)
      .add("containerIDFile", containerIDFile)
      .add("lxcConf", lxcConf)
      .add("privileged", privileged)
      .add("portBindings", portBindings)
      .add("links", links)
      .add("publishAllPorts", publishAllPorts)
      .add("dns", dns)
      .add("dnsSearch", dnsSearch)
      .add("volumesFrom", volumesFrom)
      .add("networkMode", networkMode)
      .toString();
  }
}

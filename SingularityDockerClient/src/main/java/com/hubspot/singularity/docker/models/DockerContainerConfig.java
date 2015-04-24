package com.hubspot.singularity.docker.models;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Set;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;

@JsonAutoDetect(fieldVisibility = ANY, getterVisibility = NONE, setterVisibility = NONE)
public class DockerContainerConfig {

  @JsonProperty("Hostname") private String hostname;
  @JsonProperty("Domainname") private String domainname;
  @JsonProperty("User") private String user;
  @JsonProperty("Memory") private Long memory;
  @JsonProperty("MemorySwap") private Long memorySwap;
  @JsonProperty("CpuShares") private Long cpuShares;
  @JsonProperty("Cpuset") private String cpuset;
  @JsonProperty("AttachStdin") private Boolean attachStdin;
  @JsonProperty("AttachStdout") private Boolean attachStdout;
  @JsonProperty("AttachStderr") private Boolean attachStderr;
  @JsonProperty("PortSpecs") private ImmutableList<String> portSpecs;
  @JsonProperty("ExposedPorts") private ImmutableSet<String> exposedPorts;
  @JsonProperty("Tty") private Boolean tty;
  @JsonProperty("OpenStdin") private Boolean openStdin;
  @JsonProperty("StdinOnce") private Boolean stdinOnce;
  @JsonProperty("Env") private ImmutableList<String> env;
  @JsonProperty("Cmd") private ImmutableList<String> cmd;
  @JsonProperty("Image") private String image;
  @JsonProperty("Volumes") private ImmutableSet<String> volumes;
  @JsonProperty("WorkingDir") private String workingDir;
  @JsonProperty("Entrypoint") private ImmutableList<String> entrypoint;
  @JsonProperty("NetworkDisabled") private Boolean networkDisabled;
  @JsonProperty("OnBuild") private ImmutableList<String> onBuild;

  public String hostname() {
    return hostname;
  }

  public String domainname() {
    return domainname;
  }

  public String user() {
    return user;
  }

  public Long memory() {
    return memory;
  }

  public Long memorySwap() {
    return memorySwap;
  }

  public Long cpuShares() {
    return cpuShares;
  }

  public String cpuset() {
    return cpuset;
  }

  public Boolean attachStdin() {
    return attachStdin;
  }

  public Boolean attachStdout() {
    return attachStdout;
  }

  public Boolean attachStderr() {
    return attachStderr;
  }

  public List<String> portSpecs() {
    return portSpecs;
  }

  public Set<String> exposedPorts() {
    return exposedPorts;
  }

  public Boolean tty() {
    return tty;
  }

  public Boolean openStdin() {
    return openStdin;
  }

  public Boolean stdinOnce() {
    return stdinOnce;
  }

  public List<String> env() {
    return env;
  }

  public List<String> cmd() {
    return cmd;
  }

  public String image() {
    return image;
  }

  public Set<String> volumes() {
    return volumes;
  }

  public String workingDir() {
    return workingDir;
  }

  public List<String> entrypoint() {
    return entrypoint;
  }

  public Boolean networkDisabled() {
    return networkDisabled;
  }

  public List<String> onBuild() {
    return onBuild;
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    final DockerContainerConfig config = (DockerContainerConfig) o;

    if (attachStderr != null ? !attachStderr.equals(config.attachStderr)
      : config.attachStderr != null) {
      return false;
    }
    if (attachStdin != null ? !attachStdin.equals(config.attachStdin)
      : config.attachStdin != null) {
      return false;
    }
    if (attachStdout != null ? !attachStdout.equals(config.attachStdout)
      : config.attachStdout != null) {
      return false;
    }
    if (cmd != null ? !cmd.equals(config.cmd) : config.cmd != null) {
      return false;
    }
    if (cpuShares != null ? !cpuShares.equals(config.cpuShares) : config.cpuShares != null) {
      return false;
    }
    if (cpuset != null ? !cpuset.equals(config.cpuset) : config.cpuset != null) {
      return false;
    }
    if (domainname != null ? !domainname.equals(config.domainname) : config.domainname != null) {
      return false;
    }
    if (entrypoint != null ? !entrypoint.equals(config.entrypoint) : config.entrypoint != null) {
      return false;
    }
    if (env != null ? !env.equals(config.env) : config.env != null) {
      return false;
    }
    if (exposedPorts != null ? !exposedPorts.equals(config.exposedPorts)
      : config.exposedPorts != null) {
      return false;
    }
    if (hostname != null ? !hostname.equals(config.hostname) : config.hostname != null) {
      return false;
    }
    if (image != null ? !image.equals(config.image) : config.image != null) {
      return false;
    }
    if (memory != null ? !memory.equals(config.memory) : config.memory != null) {
      return false;
    }
    if (memorySwap != null ? !memorySwap.equals(config.memorySwap) : config.memorySwap != null) {
      return false;
    }
    if (networkDisabled != null ? !networkDisabled.equals(config.networkDisabled)
      : config.networkDisabled != null) {
      return false;
    }
    if (onBuild != null ? !onBuild.equals(config.onBuild) : config.onBuild != null) {
      return false;
    }
    if (openStdin != null ? !openStdin.equals(config.openStdin) : config.openStdin != null) {
      return false;
    }
    if (portSpecs != null ? !portSpecs.equals(config.portSpecs) : config.portSpecs != null) {
      return false;
    }
    if (stdinOnce != null ? !stdinOnce.equals(config.stdinOnce) : config.stdinOnce != null) {
      return false;
    }
    if (tty != null ? !tty.equals(config.tty) : config.tty != null) {
      return false;
    }
    if (user != null ? !user.equals(config.user) : config.user != null) {
      return false;
    }
    if (volumes != null ? !volumes.equals(config.volumes) : config.volumes != null) {
      return false;
    }
    if (workingDir != null ? !workingDir.equals(config.workingDir) : config.workingDir != null) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int result = hostname != null ? hostname.hashCode() : 0;
    result = 31 * result + (domainname != null ? domainname.hashCode() : 0);
    result = 31 * result + (user != null ? user.hashCode() : 0);
    result = 31 * result + (memory != null ? memory.hashCode() : 0);
    result = 31 * result + (memorySwap != null ? memorySwap.hashCode() : 0);
    result = 31 * result + (cpuShares != null ? cpuShares.hashCode() : 0);
    result = 31 * result + (cpuset != null ? cpuset.hashCode() : 0);
    result = 31 * result + (attachStdin != null ? attachStdin.hashCode() : 0);
    result = 31 * result + (attachStdout != null ? attachStdout.hashCode() : 0);
    result = 31 * result + (attachStderr != null ? attachStderr.hashCode() : 0);
    result = 31 * result + (portSpecs != null ? portSpecs.hashCode() : 0);
    result = 31 * result + (exposedPorts != null ? exposedPorts.hashCode() : 0);
    result = 31 * result + (tty != null ? tty.hashCode() : 0);
    result = 31 * result + (openStdin != null ? openStdin.hashCode() : 0);
    result = 31 * result + (stdinOnce != null ? stdinOnce.hashCode() : 0);
    result = 31 * result + (env != null ? env.hashCode() : 0);
    result = 31 * result + (cmd != null ? cmd.hashCode() : 0);
    result = 31 * result + (image != null ? image.hashCode() : 0);
    result = 31 * result + (volumes != null ? volumes.hashCode() : 0);
    result = 31 * result + (workingDir != null ? workingDir.hashCode() : 0);
    result = 31 * result + (entrypoint != null ? entrypoint.hashCode() : 0);
    result = 31 * result + (networkDisabled != null ? networkDisabled.hashCode() : 0);
    result = 31 * result + (onBuild != null ? onBuild.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return Objects.toStringHelper(this)
      .add("hostname", hostname)
      .add("domainname", domainname)
      .add("username", user)
      .add("memory", memory)
      .add("memorySwap", memorySwap)
      .add("cpuShares", cpuShares)
      .add("cpuset", cpuset)
      .add("attachStdin", attachStdin)
      .add("attachStdout", attachStdout)
      .add("attachStderr", attachStderr)
      .add("portSpecs", portSpecs)
      .add("exposedPorts", exposedPorts)
      .add("tty", tty)
      .add("openStdin", openStdin)
      .add("stdinOnce", stdinOnce)
      .add("env", env)
      .add("cmd", cmd)
      .add("image", image)
      .add("volumes", volumes)
      .add("workingDir", workingDir)
      .add("entrypoint", entrypoint)
      .add("networkDisabled", networkDisabled)
      .add("onBuild", onBuild)
      .toString();
  }
}

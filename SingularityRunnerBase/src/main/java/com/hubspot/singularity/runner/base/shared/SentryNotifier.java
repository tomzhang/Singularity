package com.hubspot.singularity.runner.base.shared;

import net.kencochrane.raven.Raven;
import net.kencochrane.raven.RavenFactory;
import net.kencochrane.raven.event.Event;
import net.kencochrane.raven.event.EventBuilder;
import net.kencochrane.raven.event.interfaces.ExceptionInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.hubspot.singularity.runner.base.config.SingularityRunnerBaseConfigurationLoader;

@Singleton
public class SentryNotifier {
  private static final Logger LOG = LoggerFactory.getLogger(SentryNotifier.class);

  private final Optional<Raven> raven;
  private final String prefix;

  @Inject
  public SentryNotifier(@Named(SingularityRunnerBaseConfigurationLoader.SENTRY_DSN) String dsn, @Named(SingularityRunnerBaseConfigurationLoader.SENTRY_PREFIX) String prefix) {
    if (Strings.isNullOrEmpty(dsn)) {
      this.raven = Optional.of(RavenFactory.ravenInstance(dsn));
    } else {
      this.raven = Optional.absent();
    }

    this.prefix = prefix;
  }

  private String getPrefix() {
    if (!Strings.isNullOrEmpty(prefix)) {
      return prefix + " ";
    }
    return "";
  }

  public void notify(Throwable t) {
    if (!raven.isPresent()) {
      return;
    }

    try {
      notify(raven.get(), t);
    } catch (Throwable e) {
      LOG.error("Caught exception while trying to report {} to Sentry", t.getMessage(), e);
    }
  }

  public void notify(String message) {
    if (!raven.isPresent()) {
      return;
    }

    try {
      notify(raven.get(), message);
    } catch (Throwable e) {
      LOG.error("Caught exception while trying to report {} to Sentry", message, e);
    }
  }

  private void notify(Raven raven, String message) {
    final EventBuilder eventBuilder = new EventBuilder()
            .setMessage(getPrefix() + message)
            .setLevel(Event.Level.ERROR);

    sendEvent(raven, eventBuilder);
  }

  private void notify(Raven raven, Throwable t) {
    final EventBuilder eventBuilder = new EventBuilder()
            .setMessage(getPrefix() + t.getMessage())
            .setLevel(Event.Level.ERROR)
            .addSentryInterface(new ExceptionInterface(t));

    sendEvent(raven, eventBuilder);
  }

  private void sendEvent(Raven raven, final EventBuilder eventBuilder) {
    raven.runBuilderHelpers(eventBuilder);

    raven.sendEvent(eventBuilder.build());
  }
}

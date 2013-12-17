package com.hubspot.singularity.smtp;

import java.util.List;
import java.util.Properties;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.mail.Message.RecipientType;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.mesos.Protos.TaskState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.google.inject.Inject;
import com.hubspot.mesos.JavaUtils;
import com.hubspot.singularity.SingularityCloseable;
import com.hubspot.singularity.SingularityCloser;
import com.hubspot.singularity.SingularityHostState;
import com.hubspot.singularity.SingularityRequest;
import com.hubspot.singularity.SingularityTaskHistory;
import com.hubspot.singularity.SingularityTaskId;
import com.hubspot.singularity.config.SMTPConfiguration;
import com.hubspot.singularity.data.StateManager;
import com.hubspot.singularity.data.history.HistoryManager;

public class SingularityMailer implements SingularityCloseable {

  private final static Logger LOG = LoggerFactory.getLogger(SingularityMailer.class);

  private final Optional<SMTPConfiguration> maybeSmtpConfiguration;
  private final Optional<ThreadPoolExecutor> mailSenderExecutorService;
  
  private final SingularityCloser closer;

  private final StateManager stateManager;
  private final HistoryManager historyManager;

  private final SimpleEmailTemplate taskFailedTemplate;

  private static final String LOGS_LINK_FORMAT = "http://%s:5050/#/slaves/%s/browse?path=%s";
  
  @Inject
  public SingularityMailer(Optional<SMTPConfiguration> maybeSmtpConfiguration, SingularityCloser closer, StateManager stateManager, HistoryManager historyManager) {
    this.maybeSmtpConfiguration = maybeSmtpConfiguration;
    this.closer = closer;
    this.stateManager = stateManager;
    this.historyManager = historyManager;
    
    if (maybeSmtpConfiguration.isPresent()) {
      this.mailSenderExecutorService = Optional.of(new ThreadPoolExecutor(maybeSmtpConfiguration.get().getMailThreads(), maybeSmtpConfiguration.get().getMailMaxThreads(), 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(), new ThreadFactoryBuilder().setNameFormat("SingularityMailer-%d").build()));
    } else {
      this.mailSenderExecutorService = Optional.absent();
    }
    
    this.taskFailedTemplate = new SimpleEmailTemplate("task_failed.html");
  }
  
  @Override
  public void close() {
    if (!mailSenderExecutorService.isPresent()) {
      return;
    }
    
    closer.shutdown(getClass().getName(), mailSenderExecutorService.get());
  }
  
  private String getEmailLogFormat(List<String> toList, String subject, String body) {
    return String.format("[to: %s, subject: %s, body: %s]", toList, subject, body);
  }
  
  public void sendRequestPausedMail(SingularityRequest request) {
    final List<String> to = request.getOwners();
    final String subject = String.format("Request %s is PAUSED", request.getId());
    final String body = String.format("It has failed %s times consecutively. It will not run again until it is manually unpaused or updated.", request.getMaxFailuresBeforePausing());
    
    queueMail(to, subject, body); 
  }
  
  public void sendTaskFailedMail(SingularityTaskId taskId, SingularityRequest request, TaskState state) {
    final List<String> to = request.getOwners();
    final String subject = String.format("Task %s failed with state %s", taskId.toString(), state.name());

    ImmutableMap.Builder<String, String> builder = ImmutableMap.builder();
    builder.put("request_id", request.getId());
    builder.put("task_id", taskId.getId());
    builder.put("status", state.name());
    builder.put("mesos_logs_link", getMesosLogsLink(taskId));
    
    final String body = taskFailedTemplate.render(builder.build());
    
    queueMail(to, subject, body);
  }
  
  private String getMesosLogsLink(SingularityTaskId taskId) {
    Optional<SingularityTaskHistory> taskHistory = historyManager.getTaskHistory(taskId.getId());
    
    if (!taskHistory.isPresent() || !taskHistory.get().getDirectory().isPresent()) {
      return "";
    }
    
    String masterHost = null;
    
    for (SingularityHostState state : stateManager.getHostStates()) {
      if (state.isMaster()) {
        masterHost = state.getHostname();
      }
    }
    
    if (masterHost == null) {
      return "";
    }
    
    String slave = taskHistory.get().getTask().getOffer().getSlaveId().getValue();
    
    return String.format(LOGS_LINK_FORMAT, masterHost, slave, JavaUtils.urlEncode(taskHistory.get().getDirectory().get()));
  }
  
  private void queueMail(final List<String> toList, final String subject, final String body) {
    if (toList.isEmpty()) {
      LOG.warn(String.format("Couldn't queue email %s because no to address is present", getEmailLogFormat(toList, subject, body)));
      return;
    }
    
    if (!mailSenderExecutorService.isPresent()) {
      LOG.warn(String.format("Couldn't queue email %s because no SMTP configuration is present", getEmailLogFormat(toList, subject, body)));
      return;
    }  
    
    final Runnable cmd = new Runnable() {
      
      @Override
      public void run() {
        sendMail(toList, subject, body);
      }
    };
    
    LOG.debug(String.format("Queuing an email to %s (subject: %s)", toList, subject)); 
    
    mailSenderExecutorService.get().submit(cmd);
  }
  
  public void sendMail(List<String> toList, String subject, String body) {
    if (!maybeSmtpConfiguration.isPresent()) {
      LOG.warn(String.format("Couldn't send email %s because no SMTP configuration is present", getEmailLogFormat(toList, subject, body)));
    }
    
    SMTPConfiguration smtpConfiguration = maybeSmtpConfiguration.get();
    
    boolean useAuth = false;
    
    if (smtpConfiguration.getUsername().isPresent() && smtpConfiguration.getPassword().isPresent()) {
      useAuth = true;
    } else if (smtpConfiguration.getUsername().isPresent() || smtpConfiguration.getPassword().isPresent()) {
      LOG.warn(String.format("Not using smtp authentication because username (%s) or password (%s) was not present", smtpConfiguration.getUsername().isPresent(), smtpConfiguration.getPassword().isPresent()));
    }
    
    try {
      Properties properties = System.getProperties();

      properties.setProperty("mail.smtp.host", smtpConfiguration.getHost());
      
      if (smtpConfiguration.getPort().isPresent()) {
        properties.setProperty("mail.smtp.port", Integer.toString(smtpConfiguration.getPort().get()));
      }
      
      if (useAuth) {
        properties.setProperty("mail.smtp.auth", "true");
      }
      
      Session session = Session.getDefaultInstance(properties);
      Transport transport = session.getTransport("smtp");
      
      if (useAuth) {
        transport.connect(smtpConfiguration.getUsername().get(), smtpConfiguration.getPassword().get());
      }
      
      MimeMessage message = new MimeMessage(session);

      message.setFrom(new InternetAddress(smtpConfiguration.getFrom()));
      
      List<InternetAddress> addresses = Lists.newArrayList();
      
      for (String to : toList) {
        addresses.add(new InternetAddress(to));
        message.addRecipients(RecipientType.TO, to);
      }
      
      message.setSubject(subject);
      message.setContent(body, "text/html; charset=utf-8");
      
      transport.sendMessage(message, addresses.toArray(new InternetAddress[addresses.size()]));
    } catch (Throwable t) {
      LOG.warn(String.format("Unable to send message %s", getEmailLogFormat(toList, subject, body)), t);
    }
  }

}

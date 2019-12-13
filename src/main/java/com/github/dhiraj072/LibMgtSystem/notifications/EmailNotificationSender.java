package com.github.dhiraj072.LibMgtSystem.notifications;

import com.github.dhiraj072.LibMgtSystem.book.BookCheckout;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class EmailNotificationSender implements NotificationSender {

  private static final Logger LOGGER = LoggerFactory.getLogger(EmailNotificationSender.class);

  @Resource
  private JavaMailSender mailSender;

  @Override
  public void sendNotification(BookCheckout checkout, Type type) {

    switch (type) {

      case LATE:
        LOGGER.info("Sending late notification email to {}", checkout.getMember());
        mailSender.send(MailTemplates.bookLateNotification(checkout));
        break;
      default:
        throw new IllegalArgumentException("Unsupported notification type");
    }
  }
}

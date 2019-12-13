package com.github.dhiraj072.LibMgtSystem.system;

import com.github.dhiraj072.LibMgtSystem.book.BookCheckout;
import com.github.dhiraj072.LibMgtSystem.book.BookDAO;
import com.github.dhiraj072.LibMgtSystem.notifications.NotificationSender;
import com.github.dhiraj072.LibMgtSystem.notifications.NotificationSender.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import javax.annotation.Resource;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class System {

  private static final Logger LOGGER = LoggerFactory.getLogger(System.class);

  @Resource
  private BookDAO bookDAO;

  @Resource
  private List<NotificationSender> notifiers;

  @Scheduled(cron = "0 15 10 15 * ?")
  public void sendReturnReminderEmails() {

    List<BookCheckout> checkouts = bookDAO.getActiveCheckouts();
    checkouts.forEach(checkout -> {

      LOGGER.info("Checking late date {}", checkout.getCheckoutDate());
      if (isDueForReturn(checkout)) {

        LOGGER.info("Sending late notification email to {}", checkout.getMember());
        sendLateNotification(checkout);
      }

    });
  }

  private void sendLateNotification(BookCheckout checkout) {

    notifiers.forEach(notifier -> notifier.sendNotification(checkout, Type.LATE));
  }

  private boolean isDueForReturn(BookCheckout latest) {

    return latest.getCheckoutDate().plusDays(10).isBefore(LocalDate.now());
  }
}

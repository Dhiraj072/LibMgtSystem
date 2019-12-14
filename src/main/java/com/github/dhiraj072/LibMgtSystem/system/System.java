package com.github.dhiraj072.LibMgtSystem.system;

import com.github.dhiraj072.LibMgtSystem.book.BookCheckout;
import com.github.dhiraj072.LibMgtSystem.book.BookDAO;
import com.github.dhiraj072.LibMgtSystem.notifications.NotificationSender;
import com.github.dhiraj072.LibMgtSystem.notifications.NotificationSender.Type;
import java.time.LocalDate;
import java.util.List;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class System {

  private static final Logger LOGGER = LoggerFactory.getLogger(System.class);
  public static final Integer MAX_DAYS = 10;

  @Resource
  private BookDAO bookDAO;

  @Resource
  private List<NotificationSender> notifiers;

  @Scheduled(cron = "0 15 10 15 * ?")
  public void sendReturnReminderEmails() {

    List<BookCheckout> checkouts = bookDAO.getActiveCheckouts();
    checkouts.forEach(checkout -> {

      LOGGER.info("Checking late date {}", checkout.getCheckoutDate());
      if (isApproachingReturn(checkout)) {

        LOGGER.info("Sending return reminder email to {}", checkout.getMember());
        sendReminderNotification(checkout);
      } else if (isDueForReturn(checkout)) {

        LOGGER.info("Sending late notification email to {}", checkout.getMember());
        sendLateNotification(checkout);
      }
    });
  }

  private void sendReminderNotification(BookCheckout checkout) {

    notifiers.forEach(notifier -> notifier.sendNotification(checkout, Type.REMINDER));
  }

  /**
   * Book approaches return in {@link #MAX_DAYS} - 2 days from the date of checkout
   */
  private boolean isApproachingReturn(BookCheckout checkout) {

    return checkout.getCheckoutDate().plusDays(MAX_DAYS - 2).isEqual(LocalDate.now());
  }

  private void sendLateNotification(BookCheckout checkout) {

    notifiers.forEach(notifier -> notifier.sendNotification(checkout, Type.LATE));
  }

  /**
   * Book is due for return in {@link #MAX_DAYS} from the date of checkout
   */
  private boolean isDueForReturn(BookCheckout latest) {

    return latest.getCheckoutDate().plusDays(MAX_DAYS).isBefore(LocalDate.now());
  }
}

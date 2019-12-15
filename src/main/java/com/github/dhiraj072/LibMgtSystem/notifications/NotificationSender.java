package com.github.dhiraj072.LibMgtSystem.notifications;

import com.github.dhiraj072.LibMgtSystem.book.BookCheckout;
import java.util.Map;

/**
 * Interface that can be implemented by classes which want to send notifications for
 * {@link BookCheckout}. This is mainly here to support multiple types of notifications,
 * e.g. email, sms, etc.
 */
public interface NotificationSender {

  public enum Type {

    LATE, REMINDER, FINE_IMPOSED
  }

  default void sendNotification(BookCheckout checkout, Type type) {

    sendNotification(checkout, type, null);
  }

  void sendNotification(BookCheckout checkout, Type type, Map<String, String> metadata);
}

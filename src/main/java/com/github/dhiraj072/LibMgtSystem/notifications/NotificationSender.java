package com.github.dhiraj072.LibMgtSystem.notifications;

import com.github.dhiraj072.LibMgtSystem.book.BookCheckout;

/**
 * Interface that can be implemented by classes which want to send notifications for
 * {@link BookCheckout}. This is mainly here to support multiple types of notifications,
 * e.g. email, sms, etc.
 */
public interface NotificationSender {

  public enum Type {

    LATE, REMINDER
  }

  void sendNotification(BookCheckout checkout, Type type);
}

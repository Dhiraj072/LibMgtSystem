package com.github.dhiraj072.LibMgtSystem.notifications;

import com.github.dhiraj072.LibMgtSystem.book.BookCheckout;

public interface NotificationSender {

  public enum Type {

    LATE, REMINDER
  }

  void sendNotification(BookCheckout checkout, Type type);
}

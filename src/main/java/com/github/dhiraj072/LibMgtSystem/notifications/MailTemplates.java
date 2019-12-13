package com.github.dhiraj072.LibMgtSystem.notifications;

import com.github.dhiraj072.LibMgtSystem.book.BookCheckout;
import org.springframework.mail.SimpleMailMessage;

public class MailTemplates {

  private static final String LIBRARY_SIGNATURE = "Library";
  private static final String LIBRARY_EMAIL = "library@bot.com";

  public static final String LATE_BOOK_NOTIFICATION_SUB = "Late book return";
  public static final String LATE_NOTIFICATION_NOTIFICATION_BODY = "Dear %s,\n"
      + "This is to notify you that you have exceeded the deadline %s "
      + "to return the book %s. Kindly return the same as soon as possible.\n"
      + LIBRARY_SIGNATURE;

  public static SimpleMailMessage bookLateNotification(BookCheckout checkout) {

    SimpleMailMessage message = new SimpleMailMessage();
    message.setText(String.format(LATE_NOTIFICATION_NOTIFICATION_BODY,
        checkout.getMember().getUserName(),
        checkout.getCheckoutDate().plusDays(10),
        checkout.getBook().getTitle()));
    message.setFrom(LIBRARY_EMAIL);
    message.setSubject(LATE_BOOK_NOTIFICATION_SUB);
    message.setTo(checkout.getMember().getEmail());
    return message;
  }
}

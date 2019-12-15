package com.github.dhiraj072.LibMgtSystem.notifications;

import static com.github.dhiraj072.LibMgtSystem.system.System.MAX_DAYS;

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

  public static final String REMINDER_BOOK_NOTIFICATION_SUB = "Reminder for book return";
  public static final String REMINDER_NOTIFICATION_NOTIFICATION_BODY = "Dear %s,\n"
      + "This is to notify you that you are approaching the deadline %s "
      + "to return the book %s. Kindly return the book by the deadline to avoid fines.\n"
      + LIBRARY_SIGNATURE;

  public static final String FINE_IMPOSED_NOTIFICATION_SUB = "Fine imposed notification";
  public static final String FINE_IMPOSED_NOTIFICATION_BODY = "\"Dear %s,\\n\"\n"
      + "This is to notify you that a fine of %s has been imposed for the late \"\n"
      + "return of the book %s.\n"
      + LIBRARY_SIGNATURE;

  public static SimpleMailMessage bookLateNotification(BookCheckout checkout) {

    String body = String.format(LATE_NOTIFICATION_NOTIFICATION_BODY,
        checkout.getMember().getUserName(),
        checkout.getCheckoutDate().plusDays(MAX_DAYS),
        checkout.getBook().getTitle());
    return buildMessage(checkout.getMember().getEmail(), LATE_BOOK_NOTIFICATION_SUB, body);
  }

  public static SimpleMailMessage bookReturnReminderNotification(BookCheckout checkout) {

    String body = String.format(REMINDER_NOTIFICATION_NOTIFICATION_BODY,
        checkout.getMember().getUserName(),
        checkout.getCheckoutDate().plusDays(MAX_DAYS),
        checkout.getBook().getTitle());
  return buildMessage(checkout.getMember().getEmail(), REMINDER_BOOK_NOTIFICATION_SUB, body);
  }

  public static SimpleMailMessage fineImposedNotification(BookCheckout checkout, String amount) {

    String body = String.format(FINE_IMPOSED_NOTIFICATION_BODY, checkout.getMember().getName(),
        amount, checkout.getBook().getTitle());
    return buildMessage(checkout.getMember().getEmail(), FINE_IMPOSED_NOTIFICATION_SUB, body);
  }

  private static SimpleMailMessage buildMessage(String to, String MSG_SUBJECT, String MSG_BODY) {

    SimpleMailMessage message = new SimpleMailMessage();
    message.setText(MSG_BODY);
    message.setFrom(LIBRARY_EMAIL);
    message.setSubject(MSG_SUBJECT);
    message.setTo(to);
    return message;
  }
}

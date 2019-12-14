package com.github.dhiraj072.LibMgtSystem.notifications;

import static com.github.dhiraj072.LibMgtSystem.TestUtils.BOOK_1;
import static com.github.dhiraj072.LibMgtSystem.TestUtils.MEMBER_1;
import static com.github.dhiraj072.LibMgtSystem.notifications.MailTemplates.LATE_BOOK_NOTIFICATION_SUB;
import static com.github.dhiraj072.LibMgtSystem.notifications.MailTemplates.REMINDER_BOOK_NOTIFICATION_SUB;
import static com.github.dhiraj072.LibMgtSystem.system.System.MAX_DAYS;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.github.dhiraj072.LibMgtSystem.H2IntegrationTest;
import com.github.dhiraj072.LibMgtSystem.Library;
import com.github.dhiraj072.LibMgtSystem.system.System;
import com.icegreen.greenmail.store.FolderException;
import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetup;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.List;
import javax.annotation.Resource;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.sql.DataSource;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

@H2IntegrationTest
public class EmailNotificationSenderIntegrationTest  {

  private GreenMail smtpServer;

  @Resource private JdbcTemplate jdbcTemplate;
  @Resource private DataSource dataSource;

  @Resource
  private System system;

  @Resource
  private Library library;

  @BeforeAll
  void setup() throws IOException {

    smtpServer = new GreenMail(new ServerSetup(2525, null, "smtp"));
    smtpServer.start();
    // Hack to edit sql script to have checout 8 days ago from today
    File eightDaysOldCheckoutTmpl = new ClassPathResource("AddBookCheckOutTemplate.sql").getFile();
    List<String> lines = Files.readAllLines(eightDaysOldCheckoutTmpl.toPath());
    String updatedChecoutDate = lines.get(2)
        .replace("CHECKOUT_DATE_PLACEHOLDER", LocalDate.now().minusDays(MAX_DAYS - 2).toString());
    lines.set(2, updatedChecoutDate);
    Files.write(eightDaysOldCheckoutTmpl.toPath(), lines);
  }

  @BeforeEach
  public void cleanupSmtpServer() throws FolderException {

    smtpServer.purgeEmailFromAllMailboxes();
  }

  @Test
  @Sql("classpath:AddVeryOldBookCheckOut.sql")
  void testSendNotificationForLateBooks() throws MessagingException {

    assertEquals(0, smtpServer.getReceivedMessages().length);
    system.sendReturnReminderEmails();
    MimeMessage[] mailsSent = smtpServer.getReceivedMessages();
    assertEquals(1, mailsSent.length);
    MimeMessage sentMail = mailsSent[0];
    assertEquals(LATE_BOOK_NOTIFICATION_SUB, sentMail.getSubject());
    assertEquals(MEMBER_1.getEmail(), sentMail.getRecipients(RecipientType.TO)[0].toString());
  }

  @Test
  @Sql("classpath:AddBookCheckOutTemplate.sql")
  void testSendReminderNotificationForSoonToReturnBooks()
      throws MessagingException {

    assertEquals(0, smtpServer.getReceivedMessages().length);
    system.sendReturnReminderEmails();
    MimeMessage[] mailsSent = smtpServer.getReceivedMessages();
    assertEquals(1, mailsSent.length);
    MimeMessage sentMail = mailsSent[0];
    assertEquals(REMINDER_BOOK_NOTIFICATION_SUB, sentMail.getSubject());
    assertEquals(MEMBER_1.getEmail(), sentMail.getRecipients(RecipientType.TO)[0].toString());
  }

  @AfterAll
  void cleanup() {

    // Ensure stuff we added is cleaned up to avoid side-effects in other tests
    smtpServer.stop();
    assertEquals(0, library.getCheckedOutBooks(MEMBER_1).size());
    assertNull(library.getBook(BOOK_1.getUid()));
    assertNull(library.getMember(MEMBER_1.getUserName()));
  }
}

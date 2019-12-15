package com.github.dhiraj072.LibMgtSystem.fine;

import static com.github.dhiraj072.LibMgtSystem.TestUtils.BOOK_1;
import static com.github.dhiraj072.LibMgtSystem.TestUtils.MEMBER_1;
import static com.github.dhiraj072.LibMgtSystem.notifications.MailTemplates.FINE_IMPOSED_NOTIFICATION_SUB;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.github.dhiraj072.LibMgtSystem.H2IntegrationTest;
import com.github.dhiraj072.LibMgtSystem.Library;
import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetup;
import java.io.IOException;
import java.util.List;
import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.context.jdbc.Sql;

@H2IntegrationTest
public class ImposeFineIntegrationTest {

  @Resource
  private Library library;

  @Resource
  private ResourceLoader resourceLoader;

  private GreenMail smtpServer;

  @BeforeAll
  void setup() throws IOException {

    smtpServer = new GreenMail(new ServerSetup(2525, null, "smtp"));
    smtpServer.start();
  }

  @Test
  @Sql("classpath:AddVeryOldBookCheckOut.sql")
  void testFineImposedForLateReturn() throws MessagingException {

    assertEquals(1, library.getCheckedOutBooks(MEMBER_1).size());
    assertEquals(0, library.getPendingFines(MEMBER_1).size());
    library.returnBook(BOOK_1);
    List<Fine> imposedFines = library.getPendingFines(MEMBER_1);
    assertEquals(1, imposedFines.size());
    Fine imposedFine = imposedFines.get(0);
    assertEquals(MEMBER_1.getUserName(), imposedFine.getBookCheckout().getMember().getUserName());
    library.payFine(imposedFine);
    assertEquals(0, library.getPendingFines(MEMBER_1).size());
    MimeMessage imposeFineMail = smtpServer.getReceivedMessages()[0];
    assertEquals(FINE_IMPOSED_NOTIFICATION_SUB, imposeFineMail.getSubject());
  }

  @AfterAll
  void cleanup() {

    smtpServer.stop();
    // Ensure stuff we added is cleaned up to avoid side-effects in other tests
    assertEquals(0, library.getCheckedOutBooks(MEMBER_1).size());
    assertNull(library.getBook(BOOK_1.getUid()));
    assertNull(library.getMember(MEMBER_1.getUserName()));
  }
}

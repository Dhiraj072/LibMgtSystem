package com.github.dhiraj072.LibMgtSystem;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.github.dhiraj072.LibMgtSystem.book.Book;
import com.github.dhiraj072.LibMgtSystem.member.Member;
import com.github.dhiraj072.LibMgtSystem.system.Fine;
import java.time.LocalDate;
import java.util.List;
import javax.annotation.Resource;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

@H2IntegrationTest
public class ImposeFineIntegrationTest extends
    AbstractTransactionalJUnit4SpringContextTests {

  @Resource
  private Library library;

  @Resource
  private ResourceLoader resourceLoader;

  private Book book;
  private Member member;

  @BeforeAll
  void init() {

    book = new Book("UID_1", "F24", "334", "Title1",
        "Author1", "Category1", LocalDate.now());
    library.addBook(book);
    member = new Member("member1");
    library.addMember(member);
  }

  @Test
  void testFineImposedForLateReturn() {

    // Add a checkout in the past so that a fine is imposed for late return
    executeSqlScript("classpath:FineIntegrationTestData.sql", false);
    assertEquals(1, library.getCheckedOutBooks(member).size());
    assertEquals(0, library.getPendingFines(member).size());
    library.returnBook(book);
    List<Fine> imposedFines = library.getPendingFines(member);
    assertEquals(1, imposedFines.size());
    Fine imposedFine = imposedFines.get(0);
    assertEquals(member.getUserName(), imposedFine.getBookCheckout().getMember().getUserName());
  }

  @AfterAll
  void cleanup() {

    // Cleanup stuff we added to avoid side-effects in other tests
    deleteFromTables("fine", "book_checkout");
    assertEquals(0, library.getCheckedOutBooks(member).size());
  }

}

package com.github.dhiraj072.LibMgtSystem;

import static com.github.dhiraj072.LibMgtSystem.TestUtils.BOOK_1;
import static com.github.dhiraj072.LibMgtSystem.TestUtils.MEMBER_1;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.github.dhiraj072.LibMgtSystem.fine.Fine;
import java.util.List;
import javax.annotation.Resource;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.context.jdbc.Sql;

@H2IntegrationTest
public class ImposeFineIntegrationTest {

  @Resource
  private Library library;

  @Resource
  private ResourceLoader resourceLoader;

  @Test
  @Sql("classpath:AddVeryOldBookCheckOut.sql")
  void testFineImposedForLateReturn() {

    assertEquals(1, library.getCheckedOutBooks(MEMBER_1).size());
    assertEquals(0, library.getPendingFines(MEMBER_1).size());
    library.returnBook(BOOK_1);
    List<Fine> imposedFines = library.getPendingFines(MEMBER_1);
    assertEquals(1, imposedFines.size());
    Fine imposedFine = imposedFines.get(0);
    assertEquals(MEMBER_1.getUserName(), imposedFine.getBookCheckout().getMember().getUserName());
  }

  @AfterAll
  void cleanup() {

    // Ensure stuff we added is cleaned up to avoid side-effects in other tests
    assertEquals(0, library.getCheckedOutBooks(MEMBER_1).size());
    assertNull(library.getBook(BOOK_1.getUid()));
    assertNull(library.getMember(MEMBER_1.getUserName()));
  }
}

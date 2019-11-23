package com.github.dhiraj072.LibMgtSystem.book;

import com.github.dhiraj072.LibMgtSystem.H2IntegrationTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@H2IntegrationTest
class BookDAOIntegrationTest {

  @Autowired
  private BookDAO bookDAO;

  @BeforeAll
  void setup() {

    bookDAO.store(new Book("1", "F24", "334"));
    bookDAO.store(new Book("2", "F25", "334"));
  }

  @Test
  void storesBookCorrectly() {

    Book book = bookDAO.getBook("1");
    Assertions.assertNotNull(book);
    Assertions.assertEquals("F24", book.getRackNumber());
    Assertions.assertNotNull(bookDAO.getBook("2"));
  }
}

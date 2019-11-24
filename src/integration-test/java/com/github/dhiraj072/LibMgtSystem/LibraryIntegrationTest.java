package com.github.dhiraj072.LibMgtSystem;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.github.dhiraj072.LibMgtSystem.book.Book;
import com.github.dhiraj072.LibMgtSystem.member.Member;
import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

@H2IntegrationTest
public class LibraryIntegrationTest {

  @Resource
  private Library library;

  @PersistenceContext
  private EntityManager em;

  private Book book;
  private Member member;
  private static final String UID = "1";

  @BeforeAll
  void setup() {

    book = new Book(UID, "F24", "334");
    member = new Member("name");
    library.addMember(member);
  }

  @Test
  void testAddABookToLibrary() {

    library.addBook(book);
    Book added = library.getBook(UID);
    assertNotNull(added);
  }

  @Test
  void testChecksOutBookCorrectly() {

    library.addBook(book);
    assertNotNull(library.getBook(UID));
    library.checkout(book, member);
    assertNull(library.getBook(UID));
    library.returnBook(book);
    assertNotNull(library.getBook(UID));
  }
}

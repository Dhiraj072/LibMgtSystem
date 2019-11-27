package com.github.dhiraj072.LibMgtSystem;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.github.dhiraj072.LibMgtSystem.book.Book;
import com.github.dhiraj072.LibMgtSystem.book.BookSearchQuery;
import com.github.dhiraj072.LibMgtSystem.book.BookSearchQueryBuilder;
import com.github.dhiraj072.LibMgtSystem.member.Member;
import java.time.LocalDate;
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

  private Book book1;
  private Book book2;
  private Member member;
  private final BookSearchQuery emptyQuery = new BookSearchQueryBuilder().build();
  private static final String UID_1 = "1";
  private static final String UID_2 = "2";

  @BeforeAll
  void setup() {

    book1 = new Book(UID_1, "F24", "334", "Title1",
        "Author1", "Category1", LocalDate.now());
    book2 = new Book(UID_2, "F25", "335", "Title2",
        "Author2", "Category2", LocalDate.now());
    member = new Member("name");
    library.addMember(member);
  }

  @Test
  void testAddABookToLibrary() {

    library.addBook(book1);
    Book added = library.getBook(UID_1);
    assertNotNull(added);
  }

  @Test
  void testChecksOutBookCorrectly() {

    library.addBook(book1);
    assertNotNull(library.getBook(UID_1));
    library.checkout(book1, member);
    assertNull(library.getBook(UID_1));
    library.returnBook(book1);
    assertNotNull(library.getBook(UID_1));
  }

  @Test
  void testSearchBooksByTitle() {

    library.addBook(book1);
    BookSearchQuery strictQuery = new BookSearchQueryBuilder().title("Title1").build();
    BookSearchQuery partialQuery = new BookSearchQueryBuilder().title("itle").build();
    assertEquals(1, library.searchBooks(strictQuery).size());
    assertEquals(1, library.searchBooks(partialQuery).size());
    assertEquals(1, library.searchBooks(emptyQuery).size());
    library.addBook(book2);
    assertEquals(1, library.searchBooks(strictQuery).size());
    assertEquals(2, library.searchBooks(partialQuery).size());
    assertEquals(2, library.searchBooks(emptyQuery).size());
  }

  @Test
  void testSearchBooksByAuthor() {

    library.addBook(book1);
    BookSearchQuery strictQuery = new BookSearchQueryBuilder().author("Author1").build();
    BookSearchQuery partialQuery = new BookSearchQueryBuilder().author("uthor").build();
    assertEquals(1, library.searchBooks(strictQuery).size());
    assertEquals(1, library.searchBooks(partialQuery).size());
    assertEquals(1, library.searchBooks(emptyQuery).size());
    library.addBook(book2);
    assertEquals(1, library.searchBooks(strictQuery).size());
    assertEquals(2, library.searchBooks(partialQuery).size());
    assertEquals(2, library.searchBooks(emptyQuery).size());
  }

  @Test
  void testSearchBooksBySubCategory() {

    library.addBook(book1);
    BookSearchQuery strictQuery = new BookSearchQueryBuilder().subjectCategory("Category1").build();
    BookSearchQuery partialQuery = new BookSearchQueryBuilder().subjectCategory("ategory").build();
    assertEquals(1, library.searchBooks(strictQuery).size());
    assertEquals(1, library.searchBooks(partialQuery).size());
    assertEquals(1, library.searchBooks(emptyQuery).size());
    library.addBook(book2);
    assertEquals(1, library.searchBooks(strictQuery).size());
    assertEquals(2, library.searchBooks(partialQuery).size());
    assertEquals(2, library.searchBooks(emptyQuery).size());
  }

  @Test
  void testSearchBooksByPublicationDate() {

    library.addBook(book1);
    BookSearchQuery q = new BookSearchQueryBuilder()
        .publicationDate(book1.getPublicationDate())
        .build();
    BookSearchQuery noResultQuery = new BookSearchQueryBuilder()
        .publicationDate(LocalDate.now().minusDays(1))
        .build();
    assertEquals(1, library.searchBooks(q).size());
    assertEquals(0, library.searchBooks(noResultQuery).size());
  }

  @Test
  void testSearchBooksByTitleAuthorSubCategoryPubDate() {

    library.addBook(book1);
    BookSearchQuery strictQuery = new BookSearchQueryBuilder()
        .title("Title1")
        .author("Author1")
        .subjectCategory("Category1")
        .publicationDate(book1.getPublicationDate())
        .build();
    BookSearchQuery noResultQuery = new BookSearchQueryBuilder()
        .title("xxxxxxx")
        .subjectCategory("xxxxx")
        .author("xxxxxxx")
        .publicationDate(LocalDate.now().minusDays(1))
        .build();
    assertEquals(1, library.searchBooks(strictQuery).size());
    assertEquals(0, library.searchBooks(noResultQuery).size());
    library.addBook(book2);
    assertEquals(1, library.searchBooks(strictQuery).size());
    assertEquals(0, library.searchBooks(noResultQuery).size());
  }
}

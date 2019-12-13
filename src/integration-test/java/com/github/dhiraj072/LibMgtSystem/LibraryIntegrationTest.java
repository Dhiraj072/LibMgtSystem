package com.github.dhiraj072.LibMgtSystem;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.github.dhiraj072.LibMgtSystem.book.Book;
import com.github.dhiraj072.LibMgtSystem.book.BookSearchQuery;
import com.github.dhiraj072.LibMgtSystem.book.BookSearchQueryBuilder;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.github.dhiraj072.LibMgtSystem.TestUtils.*;

@H2IntegrationTest
public class LibraryIntegrationTest {

  @Resource
  private Library library;

  @PersistenceContext
  private EntityManager em;

  private List<Book> books;

  private final BookSearchQuery emptyQuery = new BookSearchQueryBuilder().build();

  @BeforeEach
  void setup() {

    books = Arrays.asList(BOOK_1, BOOK_2, BOOK_3, BOOK_4, BOOK_5, BOOK_6);
    library.addMember(MEMBER_1);
    library.addMember(MEMBER_2);
  }

  @Test
  void testAddABookToLibrary() {

    library.addBook(BOOK_1);
    Book added = library.getBook(UID_1);
    assertNotNull(added);
  }

  @Test
  void testChecksOutBookCorrectly() {

    library.addBook(BOOK_1);
    assertNotNull(library.getBook(UID_1));
    library.checkout(BOOK_1, MEMBER_1);
    assertNull(library.getBook(UID_1));
    library.returnBook(BOOK_1);
    assertNotNull(library.getBook(UID_1));
  }

  @Test
  void testSearchBooksByTitle() {

    library.addBook(BOOK_1);
    BookSearchQuery strictQuery = new BookSearchQueryBuilder().title("Title1").build();
    BookSearchQuery partialQuery = new BookSearchQueryBuilder().title("itle").build();
    assertEquals(1, library.searchBooks(strictQuery).size());
    assertEquals(1, library.searchBooks(partialQuery).size());
    assertEquals(1, library.searchBooks(emptyQuery).size());
    library.addBook(BOOK_2);
    assertEquals(1, library.searchBooks(strictQuery).size());
    assertEquals(2, library.searchBooks(partialQuery).size());
    assertEquals(2, library.searchBooks(emptyQuery).size());
  }

  @Test
  void testSearchBooksByAuthor() {

    library.addBook(BOOK_1);
    BookSearchQuery strictQuery = new BookSearchQueryBuilder().author("Author1").build();
    BookSearchQuery partialQuery = new BookSearchQueryBuilder().author("uthor").build();
    assertEquals(1, library.searchBooks(strictQuery).size());
    assertEquals(1, library.searchBooks(partialQuery).size());
    assertEquals(1, library.searchBooks(emptyQuery).size());
    library.addBook(BOOK_2);
    assertEquals(1, library.searchBooks(strictQuery).size());
    assertEquals(2, library.searchBooks(partialQuery).size());
    assertEquals(2, library.searchBooks(emptyQuery).size());
  }

  @Test
  void testSearchBooksBySubCategory() {

    library.addBook(BOOK_1);
    BookSearchQuery strictQuery = new BookSearchQueryBuilder().subjectCategory("Category1").build();
    BookSearchQuery partialQuery = new BookSearchQueryBuilder().subjectCategory("ategory").build();
    assertEquals(1, library.searchBooks(strictQuery).size());
    assertEquals(1, library.searchBooks(partialQuery).size());
    assertEquals(1, library.searchBooks(emptyQuery).size());
    library.addBook(BOOK_2);
    assertEquals(1, library.searchBooks(strictQuery).size());
    assertEquals(2, library.searchBooks(partialQuery).size());
    assertEquals(2, library.searchBooks(emptyQuery).size());
  }

  @Test
  void testSearchBooksByPublicationDate() {

    library.addBook(BOOK_1);
    BookSearchQuery q = new BookSearchQueryBuilder()
        .publicationDate(BOOK_1.getPublicationDate())
        .build();
    BookSearchQuery noResultQuery = new BookSearchQueryBuilder()
        .publicationDate(LocalDate.now().minusDays(1))
        .build();
    assertEquals(1, library.searchBooks(q).size());
    assertEquals(0, library.searchBooks(noResultQuery).size());
  }

  @Test
  void testSearchBooksByTitleAuthorSubCategoryPubDate() {

    library.addBook(BOOK_1);
    BookSearchQuery strictQuery = new BookSearchQueryBuilder()
        .title("Title1")
        .author("Author1")
        .subjectCategory("Category1")
        .publicationDate(BOOK_1.getPublicationDate())
        .build();
    BookSearchQuery noResultQuery = new BookSearchQueryBuilder()
        .title("xxxxxxx")
        .subjectCategory("xxxxx")
        .author("xxxxxxx")
        .publicationDate(LocalDate.now().minusDays(1))
        .build();
    assertEquals(1, library.searchBooks(strictQuery).size());
    assertEquals(0, library.searchBooks(noResultQuery).size());
    library.addBook(BOOK_2);
    assertEquals(1, library.searchBooks(strictQuery).size());
    assertEquals(0, library.searchBooks(noResultQuery).size());
  }

  @Test
  public void testGetCheckedOutBooksByMember() {

    List<Book> checkedOutMbr1, checkedOutMbr2;
    library.addBook(BOOK_1);
    library.addBook(BOOK_2);

    // Member 1 checks out BOOK_1, Member 2 nothing
    library.checkout(BOOK_1, MEMBER_1);
    checkedOutMbr1 = library.getCheckedOutBooks(MEMBER_1);
    checkedOutMbr2 = library.getCheckedOutBooks(MEMBER_2);
    assertEquals(1, checkedOutMbr1.size());
    assertEquals(0, checkedOutMbr2.size());
    assertEquals(BOOK_1.getUid(), checkedOutMbr1.get(0).getUid());

    // Member 1 checks out BOOK_2, Member 2 nothing
    library.checkout(BOOK_2, MEMBER_1);
    checkedOutMbr1 = library.getCheckedOutBooks(MEMBER_1);
    assertEquals(checkedOutMbr1.size(), 2);
    assertEquals(BOOK_1.getUid(), checkedOutMbr1.get(0).getUid());
    assertEquals(BOOK_2.getUid(), checkedOutMbr1.get(1).getUid());

    // Member 1 returns BOOK_2, Member 2 checks out book 1
    library.returnBook(BOOK_1);
    library.checkout(BOOK_1, MEMBER_2);
    checkedOutMbr1 = library.getCheckedOutBooks(MEMBER_1);
    checkedOutMbr2 = library.getCheckedOutBooks(MEMBER_2);
    assertEquals(1, checkedOutMbr1.size());
    assertEquals(1, checkedOutMbr2.size());
    assertEquals(BOOK_1.getUid(), checkedOutMbr2.get(0).getUid());
    assertEquals(BOOK_2.getUid(), checkedOutMbr1.get(0).getUid());
  }

  @Test
  public void testMaxBookCheckoutLimit() {

    library.addBooks(books);
    assertThrows(IllegalArgumentException.class,
        () -> books.forEach(b -> library.checkout(b, MEMBER_1)));
  }
}

package com.github.dhiraj072.LibMgtSystem;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.github.dhiraj072.LibMgtSystem.book.Book;
import com.github.dhiraj072.LibMgtSystem.book.BookSearchQuery;
import com.github.dhiraj072.LibMgtSystem.book.BookSearchQueryBuilder;
import com.github.dhiraj072.LibMgtSystem.member.Member;
import java.time.LocalDate;
import java.util.List;
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
  private Member member1;
  private Member member2;
  private final BookSearchQuery emptyQuery = new BookSearchQueryBuilder().build();
  private static final String UID_1 = "1";
  private static final String UID_2 = "2";

  @BeforeAll
  void setup() {

    book1 = new Book(UID_1, "F24", "334", "Title1",
        "Author1", "Category1", LocalDate.now());
    book2 = new Book(UID_2, "F25", "335", "Title2",
        "Author2", "Category2", LocalDate.now());
    member1 = new Member("name1");
    member2 = new Member("name2");
    library.addMember(member1);
    library.addMember(member2);
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
    library.checkout(book1, member1);
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

  @Test
  public void testGetCheckedOutBooksByMember() {

    List<Book> checkedOutMbr1, checkedOutMbr2;
    library.addBook(book1);
    library.addBook(book2);

    // Member 1 checks out book1, Member 2 nothing
    library.checkout(book1, member1);
    checkedOutMbr1 = library.getCheckedOutBooks(member1);
    checkedOutMbr2 = library.getCheckedOutBooks(member2);
    assertEquals(1, checkedOutMbr1.size());
    assertEquals(0, checkedOutMbr2.size());
    assertEquals(book1.getUid(), checkedOutMbr1.get(0).getUid());

    // Member 1 checks out book2, Member 2 nothing
    library.checkout(book2, member1);
    checkedOutMbr1 = library.getCheckedOutBooks(member1);
    assertEquals(checkedOutMbr1.size(), 2);
    assertEquals(book1.getUid(), checkedOutMbr1.get(0).getUid());
    assertEquals(book2.getUid(), checkedOutMbr1.get(1).getUid());

    // Member 1 returns book2, Member 2 checks out book 1
    library.returnBook(book1);
    library.checkout(book1, member2);
    checkedOutMbr1 = library.getCheckedOutBooks(member1);
    checkedOutMbr2 = library.getCheckedOutBooks(member2);
    assertEquals(1, checkedOutMbr1.size());
    assertEquals(1, checkedOutMbr2.size());
    assertEquals(book1.getUid(), checkedOutMbr2.get(0).getUid());
    assertEquals(book2.getUid(), checkedOutMbr1.get(0).getUid());
  }
}

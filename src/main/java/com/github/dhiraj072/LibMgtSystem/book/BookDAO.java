package com.github.dhiraj072.LibMgtSystem.book;

import static com.github.dhiraj072.LibMgtSystem.book.BookCheckout.MEMBER;
import static com.github.dhiraj072.LibMgtSystem.book.BookCheckout.RETURN_DATE;

import com.github.dhiraj072.LibMgtSystem.member.Member;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.stereotype.Component;

@Component
public class BookDAO {

  private static final Logger LOGGER = LoggerFactory.getLogger(BookDAO.class);

  @PersistenceContext
  private EntityManager em;

  private CriteriaBuilder cb;

  @PostConstruct
  public void init() {

    cb = em.getCriteriaBuilder();
  }

  public void addBook(Book book) {

    em.persist(book);
    LOGGER.info("New book {} added to library", book.getUid());
  }

  public Book getBook(String uid) {

   Book book = em.find(Book.class, uid);
    if (!isCheckedOut(book))
      return book;
    return null;
  }

  public List<BookCheckout> getActiveCheckouts() {

    CriteriaQuery<BookCheckout> searchQuery = cb.createQuery(BookCheckout.class);
    Root<BookCheckout> bookCheckout = searchQuery.from(BookCheckout.class);
    searchQuery.select(bookCheckout);
    searchQuery.where(cb.isNull(bookCheckout.get(RETURN_DATE)));
    return em.createQuery(searchQuery).getResultList();
  }

  public List<Book> getCheckedOutBooks(Member member) {

    CriteriaQuery<BookCheckout> searchQuery = cb.createQuery(BookCheckout.class);
    Root<BookCheckout> bookCheckout = searchQuery.from(BookCheckout.class);
    searchQuery.select(bookCheckout);
    Predicate checkedOutByMember = cb.equal(bookCheckout.get(MEMBER), member);
    Predicate bookNotReturned = cb.isNull(bookCheckout.get(RETURN_DATE));
    searchQuery.where(checkedOutByMember, bookNotReturned);
    List<BookCheckout> bcs = em.createQuery(searchQuery).getResultList();
    return bcs.stream().map(BookCheckout::getBook).collect(Collectors.toList());
  }

  public List<Book> searchBooks(BookSearchQuery query) {

    CriteriaQuery<Book> searchQuery = cb.createQuery(Book.class);
    Root<Book> root = searchQuery.from(Book.class);
    searchQuery.select(root);
    List<Predicate> filters = new ArrayList<>();
    if (query.getTitle() != null)
      filters.add(cb.like(root.get(Book.TITLE), like(query.getTitle())));
    if (query.getAuthor() != null)
      filters.add(cb.like(root.get(Book.AUTHOR), like(query.getAuthor())));
    if (query.getSubjectCategory() != null)
      filters.add(cb.like(root.get(Book.SUB_CATEGORY), like(query.getSubjectCategory())));
    if (query.getPublicationDate() != null)
      filters.add(cb.equal(root.get(Book.PUB_DATE), query.getPublicationDate()));
    searchQuery.where(filters.toArray(new Predicate[0]));
    return em.createQuery(searchQuery).getResultList();
  }

  public void checkout(Book book, Member member) {

    BookCheckout checkout = new BookCheckout(book, member);
    em.persist(checkout);
  }

  public BookCheckout returnBook(Book book) {

    BookCheckout latest = getLatestCheckout(book);
    latest.setReturnDate(LocalDate.now());
    em.persist(latest);
    return latest;
  }
  private String like(String str) {

    return "%".concat(str).concat("%");
  }

  public boolean isCheckedOut(Book book) {

    BookCheckout latestCheckout = getLatestCheckout(book);
    return latestCheckout != null && latestCheckout.getReturnDate() == null;
  }

  public BookCheckout getLatestCheckout(Book book) {

    return DataAccessUtils.singleResult(
        em.createNamedQuery(BookCheckout.GET_LATEST_CHECKOUT, BookCheckout.class)
            .setParameter("book", book).getResultList());
  }

}

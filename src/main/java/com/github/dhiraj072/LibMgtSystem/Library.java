package com.github.dhiraj072.LibMgtSystem;

import static com.github.dhiraj072.LibMgtSystem.book.BookCheckout.MEMBER;
import static com.github.dhiraj072.LibMgtSystem.book.BookCheckout.RETURN_DATE;
import static com.github.dhiraj072.LibMgtSystem.member.Member.MAX_BOOKS;
import static com.github.dhiraj072.LibMgtSystem.system.Fine.PAYMENT_DATE;

import com.github.dhiraj072.LibMgtSystem.book.Book;
import com.github.dhiraj072.LibMgtSystem.book.BookCheckout;
import com.github.dhiraj072.LibMgtSystem.book.BookSearchQuery;
import com.github.dhiraj072.LibMgtSystem.member.Member;
import com.github.dhiraj072.LibMgtSystem.system.Fine;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.ResourceLoader;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.stereotype.Service;

@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
@Transactional
public class Library {

  Logger LOGGER = LoggerFactory.getLogger(Library.class);

  @PersistenceContext
  private EntityManager em;

  @Resource
  private ResourceLoader resourceLoader;

  private CriteriaBuilder cb;

  @PostConstruct
  public void init() {

    cb = em.getCriteriaBuilder();
  }

  public void addMember(Member m) {

    em.persist(m);
    LOGGER.info("New member {} added to library", m.getUserName());
  }

  public void addBooks(List<Book> books) {

    books.forEach(this::addBook);
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

  private String like(String str) {

    return "%".concat(str).concat("%");
  }

  public void checkout(Book book, Member member) {

    if (getCheckedOutBooks(member).size() >= MAX_BOOKS)
      throw new IllegalArgumentException(
          "Unable to checkout book as member " + member.getUserName() +
              " has already reached max limit of " + MAX_BOOKS);
    if (isCheckedOut(book))
      throw new IllegalArgumentException("Book " + book.getUid() + " is already checked out");
    BookCheckout checkout = new BookCheckout(book, member);
    em.persist(checkout);
    LOGGER.info("{} checked out book {}", member.getName(), book.getUid());
  }

  public void returnBook(Book book) {

    if (!isCheckedOut(book))
      throw new IllegalArgumentException("Invalid return for " + book.getUid() +" not checked out");
    BookCheckout latest = getLatestCheckout(book);
    latest.setReturnDate(LocalDateTime.now());
    em.persist(latest);
    if (isLateReturn(latest))
      imposeFine(latest);
    LOGGER.info("Book {} has been returned to library", book.getUid());
  }

  public List<Fine> getPendingFines(Member m) {

    CriteriaQuery<Fine> searchQuery = cb.createQuery(Fine.class);
    Root<Fine> fine = searchQuery.from(Fine.class);
    Join<Fine, BookCheckout> fineBookCheckoutJoin = fine.join("bookCheckout");
    searchQuery.select(fine);
    Predicate checkedOutByMember = cb.equal(fineBookCheckoutJoin.get(MEMBER), m);
    Predicate fineNotPaid = cb.isNull(fine.get(PAYMENT_DATE));
    searchQuery.where(checkedOutByMember, fineNotPaid);
    return em.createQuery(searchQuery).getResultList();
  }

  private void imposeFine(BookCheckout checkout) {

    Fine fine = new Fine(checkout);
    em.persist(fine);
    LOGGER.info("Fine of {} imposed on {}", fine.getAmount(), checkout.getMember());
  }

  private boolean isLateReturn(BookCheckout latest) {

    return latest.getReturnDate().plusDays(10).isAfter(LocalDateTime.now());
  }

  private boolean isCheckedOut(Book book) {

    BookCheckout latestCheckout = getLatestCheckout(book);
    return latestCheckout != null && latestCheckout.getReturnDate() == null;
  }

  private BookCheckout getLatestCheckout(Book book) {

    return DataAccessUtils.singleResult(
        em.createNamedQuery(BookCheckout.GET_LATEST_CHECKOUT, BookCheckout.class)
            .setParameter("book", book).getResultList());
  }
}

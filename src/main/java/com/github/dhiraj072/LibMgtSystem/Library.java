package com.github.dhiraj072.LibMgtSystem;

import com.github.dhiraj072.LibMgtSystem.book.Book;
import com.github.dhiraj072.LibMgtSystem.book.BookCheckout;
import com.github.dhiraj072.LibMgtSystem.book.BookSearchQuery;
import com.github.dhiraj072.LibMgtSystem.member.Member;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.stereotype.Service;

@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
@Transactional
public class Library {

  Logger LOGGER = LoggerFactory.getLogger(Library.class);

  @PersistenceContext
  private EntityManager em;

  public void addMember(Member m) {

    em.persist(m);
  }

  public void addBook(Book book) {

    em.persist(book);
  }

  public Book getBook(String uid) {

    Book book = em.find(Book.class, uid);
    if (!isCheckedOut(uid))
      return book;
    return null;
  }

  public List<Book> searchBooks(BookSearchQuery query) {

    CriteriaBuilder cb = em.getCriteriaBuilder();
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

    if (isCheckedOut(book.getUid()))
      throw new IllegalArgumentException("Book " + book.getUid() + " is already checked out");
    BookCheckout checkout = new BookCheckout(book, member);
    em.persist(checkout);
    LOGGER.info("{} checked out book {}", member.getName(), book.getUid());
  }

  public void returnBook(Book book) {

    if (!isCheckedOut(book.getUid()))
      throw new IllegalArgumentException("Invalid return for " + book.getUid() +" not checked out");
    BookCheckout latest = getLatestCheckout(book.getUid());
    latest.setReturnDate(LocalDateTime.now());
    em.persist(latest);
  }

  private boolean isCheckedOut(String uid) {

    BookCheckout latestCheckout = getLatestCheckout(uid);
    return latestCheckout != null && latestCheckout.getReturnDate() == null;
  }

  private BookCheckout getLatestCheckout(String uid) {

    return DataAccessUtils.singleResult(
        em.createNamedQuery(BookCheckout.GET_LATEST_CHECKOUT, BookCheckout.class)
            .setParameter("uid", uid).getResultList());
  }
}

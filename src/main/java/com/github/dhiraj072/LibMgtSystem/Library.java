package com.github.dhiraj072.LibMgtSystem;

import com.github.dhiraj072.LibMgtSystem.book.Book;
import com.github.dhiraj072.LibMgtSystem.book.BookCheckout;
import com.github.dhiraj072.LibMgtSystem.book.BookSearchQuery;
import com.github.dhiraj072.LibMgtSystem.member.Member;
import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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

    return em.createNamedQuery(Book.SEARCH_BOOKS, Book.class)
        .setParameter(Book.TITLE, query.getTitle())
        .setParameter(Book.AUTHOR, query.getAuthor())
        .setParameter(Book.SUB_CATEGORY, query.getSubjectCategory())
        .getResultList();
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

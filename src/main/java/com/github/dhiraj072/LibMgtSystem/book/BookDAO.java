package com.github.dhiraj072.LibMgtSystem.book;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import org.springframework.stereotype.Component;

@Component
@Transactional
public class BookDAO {

  @PersistenceContext
  private EntityManager em;

  public void store(Book book) {

    em.persist(book);
  }

  public Book getBook(String uid) {

    return em.find(Book.class, uid);
  }
}

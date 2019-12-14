package com.github.dhiraj072.LibMgtSystem;

import static com.github.dhiraj072.LibMgtSystem.member.Member.MAX_BOOKS;

import com.github.dhiraj072.LibMgtSystem.book.Book;
import com.github.dhiraj072.LibMgtSystem.book.BookCheckout;
import com.github.dhiraj072.LibMgtSystem.book.BookDAO;
import com.github.dhiraj072.LibMgtSystem.book.BookSearchQuery;
import com.github.dhiraj072.LibMgtSystem.fine.FineDAO;
import com.github.dhiraj072.LibMgtSystem.member.Member;
import com.github.dhiraj072.LibMgtSystem.fine.Fine;
import com.github.dhiraj072.LibMgtSystem.member.MemberDAO;
import java.time.LocalDate;
import java.util.List;
import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
@Transactional(TxType.REQUIRED)
public class Library {

  private static final Logger LOGGER = LoggerFactory.getLogger(Library.class);

  @PersistenceContext
  private EntityManager em;

  @Resource
  private BookDAO bookDAO;

  @Resource
  private FineDAO fineDAO;

  @Resource
  private MemberDAO memberDAO;

  public void addMember(Member m) {

    memberDAO.addMember(m);
    LOGGER.info("New member {} added to library", m.getUserName());
  }

  public Member getMember(String userName) {

    return memberDAO.getMember(userName);
  }

  public void addBooks(List<Book> books) {

    books.forEach(this::addBook);
  }

  public void addBook(Book book) {

    bookDAO.addBook(book);
  }

  public Book getBook(String uid) {

    return bookDAO.getBook(uid);
  }

  public List<Book> getCheckedOutBooks(Member member) {

    return bookDAO.getCheckedOutBooks(member);
  }

  public List<Book> searchBooks(BookSearchQuery query) {

    return bookDAO.searchBooks(query);
  }

  public void checkout(Book book, Member member) {

    if (getCheckedOutBooks(member).size() >= MAX_BOOKS)
      throw new IllegalArgumentException(
          "Unable to checkout book as member " + member.getUserName() +
              " has already reached max limit of " + MAX_BOOKS);
    if (bookDAO.isCheckedOut(book))
      throw new IllegalArgumentException("Book " + book.getUid() + " is already checked out");
    bookDAO.checkout(book, member);
    LOGGER.info("{} checked out book {}", member.getName(), book.getUid());
  }

  public void returnBook(Book book) {

    if (!bookDAO.isCheckedOut(book))
      throw new IllegalArgumentException("Invalid return for " + book.getUid() +" not checked out");
    BookCheckout updatedCheckout = bookDAO.returnBook(book);
    if (isLateReturn(updatedCheckout))
      fineDAO.imposeFine(updatedCheckout);
    LOGGER.info("Book {} has been returned to library", book.getUid());
  }

  public void payFine(Fine fine) {

    fineDAO.payFine(fine);
  }

  public List<Fine> getPendingFines(Member m) {

    return fineDAO.getPendingFines(m);
  }

  private boolean isLateReturn(BookCheckout latest) {

    return latest.getReturnDate().plusDays(10).isAfter(LocalDate.now());
  }
}

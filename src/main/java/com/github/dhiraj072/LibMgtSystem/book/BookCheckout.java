package com.github.dhiraj072.LibMgtSystem.book;

import com.github.dhiraj072.LibMgtSystem.member.Member;
import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@NamedQueries({
    @NamedQuery(name = BookCheckout.GET_LATEST_CHECKOUT,
        query = "select c from BookCheckout c where c.book = :book order by c.id desc"
    )
})
@Entity
public class BookCheckout {

  @Id
  @GeneratedValue(strategy= GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "book_uid")
  private Book book;

  @ManyToOne
  @JoinColumn(name = "user_name")
  private Member member;

  @Column
  private LocalDate checkoutDate;

  @Column
  private LocalDateTime returnDate;

  public BookCheckout(Book book, Member member) {

    this.book = book;
    this.member = member;
    this.checkoutDate = LocalDate.now();
    this.returnDate = null;
  }

  public static final String GET_LATEST_CHECKOUT = "bookdao.latestCheckout";
  public static final String MEMBER = "member";
  public static final String RETURN_DATE = "returnDate";

  public LocalDateTime getReturnDate() {

    return returnDate;
  }

  public void setReturnDate(LocalDateTime returnDate) {

    this.returnDate = returnDate;
  }

  public Member getMember() {

    return member;
  }

  public Book getBook() {

    return book;
  }

  public LocalDate getCheckoutDate() {

    return checkoutDate;
  }
}

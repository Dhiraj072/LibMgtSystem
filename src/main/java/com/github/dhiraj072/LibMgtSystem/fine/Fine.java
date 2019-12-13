package com.github.dhiraj072.LibMgtSystem.fine;

import static java.time.temporal.ChronoUnit.DAYS;

import com.github.dhiraj072.LibMgtSystem.book.BookCheckout;
import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
public class Fine {

  @Id
  @GeneratedValue(strategy= GenerationType.IDENTITY)
  private Long id;

  @OneToOne
  @JoinColumn(name = "book_checkout_id", referencedColumnName = "id")
  private BookCheckout bookCheckout;

  @Column
  private Integer amount;

  @Column
  private LocalDate imposedDate;

  @Column
  private LocalDate paymentDate;

  public static final String PAYMENT_DATE = "paymentDate";

  public Integer getAmount() {

    return amount;
  }

  public Fine(BookCheckout bookCheckout) {

    this.bookCheckout = bookCheckout;
    long days = DAYS.between(LocalDate.now(), bookCheckout.getReturnDate());
    this.amount = Math.toIntExact(days * 10);
    this.imposedDate = LocalDate.now();
    this.paymentDate = null;
  }

  public LocalDate getImposedDate() {

    return imposedDate;
  }

  public LocalDate getPaymentDate() {

    return paymentDate;
  }

  public BookCheckout getBookCheckout() {

    return bookCheckout;
  }
}

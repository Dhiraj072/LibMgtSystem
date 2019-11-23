package com.github.dhiraj072.LibMgtSystem.book;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Book {

  @GeneratedValue(strategy= GenerationType.IDENTITY)
  private Long id;

  @Id
  @Column
  private String uid;

  @Column
  private String rackNumber;

  @Column
  private String bookId;

  public Book() { }

  public Book(String uid, String rackNumber, String bookId) {

    this.uid = uid;
    this.rackNumber = rackNumber;
    this.bookId = bookId;
  }

  public String getUid() {

    return uid;
  }

  public String getRackNumber() {

    return rackNumber;
  }

  public String getBookId() {

    return bookId;
  }
}

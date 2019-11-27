package com.github.dhiraj072.LibMgtSystem.book;

import java.time.LocalDate;
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

  @Column
  private String title;

  @Column
  private String author;

  @Column
  private String subjectCategory;

  @Column
  private LocalDate publicationDate;

  public Book() { }

  public Book(String uid, String rackNumber, String bookId,
      String title, String author, String subCategory, LocalDate pubDate) {

    this.uid = uid;
    this.rackNumber = rackNumber;
    this.bookId = bookId;
    this.title = title;
    this.author = author;
    this.subjectCategory = subCategory;
    this.publicationDate = pubDate;
  }

  public static final String SEARCH_BOOKS = "Book.searchBooks";
  public static final String TITLE = "title";
  public static final String AUTHOR = "author";
  public static final String SUB_CATEGORY = "subjectCategory";
  public static final String PUB_DATE = "publicationDate";

  public String getUid() {

    return uid;
  }

  public String getRackNumber() {

    return rackNumber;
  }

  public String getBookId() {

    return bookId;
  }

  public String getTitle() {

    return title;
  }

  public String getSubjectCategory() {

    return subjectCategory;
  }

  public LocalDate getPublicationDate() {

    return publicationDate;
  }
}

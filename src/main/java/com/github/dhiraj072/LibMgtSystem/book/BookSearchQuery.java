package com.github.dhiraj072.LibMgtSystem.book;

import java.time.LocalDate;

/**
 * Query for searching books
 * Parameters default to empty so that we match everything when parameter is not set
 */
public class BookSearchQuery {

  private String title;
  private String author;
  private String subjectCategory;
  private LocalDate publicationDate;

  public void setTitle(String title) {

    this.title = title;
  }

  public void setAuthor(String author) {

    this.author = author;
  }

  public void setSubjectCategory(String subjectCategory) {

    this.subjectCategory = subjectCategory;
  }

  public void setPublicationDate(LocalDate publicationDate) {

    this.publicationDate = publicationDate;
  }

  public String getTitle() {

    return title;
  }

  public String getAuthor() {

    return author;
  }

  public String getSubjectCategory() {

    return subjectCategory;
  }

  public LocalDate getPublicationDate() {

    return publicationDate;
  }
}

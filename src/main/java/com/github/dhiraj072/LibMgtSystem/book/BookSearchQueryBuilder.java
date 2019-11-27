package com.github.dhiraj072.LibMgtSystem.book;

import java.time.LocalDate;

public class BookSearchQueryBuilder {

  private BookSearchQuery bookSearchQuery;

  public BookSearchQueryBuilder() {

      bookSearchQuery = new BookSearchQuery();
  }

  public BookSearchQueryBuilder title(String title) {

    bookSearchQuery.setTitle(title);
    return this;
  }

  public BookSearchQueryBuilder author(String author) {

    bookSearchQuery.setAuthor(author);
    return this;
  }

  public BookSearchQueryBuilder subjectCategory(String c) {

    bookSearchQuery.setSubjectCategory(c);
    return this;
  }

  public BookSearchQueryBuilder publicationDate(LocalDate d) {

    bookSearchQuery.setPublicationDate(d);
    return this;
  }

  public BookSearchQuery build() {

    return bookSearchQuery;
  }
}

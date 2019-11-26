package com.github.dhiraj072.LibMgtSystem.book;

/**
 * Query for searching books
 * Parameters default to empty so that we match everything when parameter is not set
 */
public class BookSearchQuery {

  private String title = "";
  private String author = "";
  private String subjectCategory = "";

  public void setTitle(String title) {

    this.title = title;
  }

  public void setAuthor(String author) {

    this.author = author;
  }

  public void setSubjectCategory(String subjectCategory) {

    this.subjectCategory = subjectCategory;
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
}

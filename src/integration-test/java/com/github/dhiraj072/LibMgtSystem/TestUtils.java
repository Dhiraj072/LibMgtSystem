package com.github.dhiraj072.LibMgtSystem;

import com.github.dhiraj072.LibMgtSystem.book.Book;
import com.github.dhiraj072.LibMgtSystem.member.Member;
import java.time.LocalDate;

public class TestUtils {

  public static final String UID_1 = "1";
  public static final String UID_2 = "2";

  public static final Member MEMBER_1 = new Member("name1", "name1@bot.com");
  public static final Member MEMBER_2 = new Member("name2", "name2@bot.com");

  public static final Book BOOK_1 = new Book(UID_1, "F24", "334", "Title1",
      "Author1", "Category1", LocalDate.now());
  public static final Book BOOK_2 = new Book(UID_2, "F25", "335", "Title2",
      "Author2", "Category2", LocalDate.now());
  public static final Book BOOK_3 = new Book("UID_3", "F3", "33", "Title3",
      "Author3", "Category3", LocalDate.now());
  public static final Book BOOK_4 = new Book("UID_4", "F4", "44", "Title4",
      "Author4", "Category4", LocalDate.now());
  public static final Book BOOK_5 = new Book("UID_5", "F5", "55", "Title5",
      "Author5", "Category5", LocalDate.now());
  public static final Book BOOK_6 = new Book("UID_6", "F6", "66", "Title6",
      "Author6", "Category6", LocalDate.now());
}

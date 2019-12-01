package com.github.dhiraj072.LibMgtSystem.member;

import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Member {

  public static final int MAX_BOOKS = 5;

  public Member() { }

  public Member(String userName) {

    this(userName, userName);
  }

  public Member(String userName, String name) {

    this.userName = userName;
    this.name = name;
    this.joinDate = LocalDate.now();
  }

  @GeneratedValue(strategy= GenerationType.IDENTITY)
  private Long id;

  @Id
  @Column
  private String userName;

  @Column
  private String name;

  @Column
  private LocalDate joinDate;

  public Long getId() {

    return id;
  }

  public String getName() {

    return name;
  }

  public String getUserName() {

    return userName;
  }

  public LocalDate getJoinDate() {

    return joinDate;
  }

  @Override
  public String toString() {

    return this.getUserName();
  }
}

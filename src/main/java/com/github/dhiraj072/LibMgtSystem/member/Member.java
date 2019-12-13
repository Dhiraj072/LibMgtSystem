package com.github.dhiraj072.LibMgtSystem.member;

import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Email;

@Entity
public class Member {

  public static final int MAX_BOOKS = 5;

  public Member() { }

  public Member(String userName, String email) {

    this(userName, userName, email);
  }

  public Member(String userName, String name, String email) {

    this.userName = userName;
    this.name = name;
    this.joinDate = LocalDate.now();
    this.email = email;
  }

  @GeneratedValue(strategy= GenerationType.AUTO)
  private Long id;

  @Id
  @Column
  private String userName;

  @Column
  private String name;

  @Email
  @Column
  private String email;

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

  public String getEmail() {

    return email;
  }

  @Override
  public String toString() {

    return this.getUserName();
  }
}

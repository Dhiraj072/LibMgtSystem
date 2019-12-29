package com.github.dhiraj072.LibMgtSystem.member;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
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

  public Member(String userName, String email, String password) {

    this(userName, userName, email, password);
  }

  public Member(String userName, String name, String email, String password) {

    this.userName = userName;
    this.name = name;
    this.joinDate = LocalDate.now();
    this.email = email;
    this.password = password;
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
  @JsonProperty(access = Access.WRITE_ONLY)
  private  String password;

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

  public String getPassword() {

    return password;
  }

  public void setJoinDate(LocalDate joinDate) {

    this.joinDate = joinDate;
  }

  @Override
  public String toString() {

    return this.getUserName() + ":" + this.getEmail() + ":" + this.getPassword();
  }

  public void setName(String name) {

    this.name = name;
  }
}

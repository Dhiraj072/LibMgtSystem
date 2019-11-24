package com.github.dhiraj072.LibMgtSystem.member;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Member {

  public Member(String name) {

    this.name = name;
  }

  @Id
  @GeneratedValue(strategy= GenerationType.IDENTITY)
  private Long id;

  @Column
  private String name;

  public Long getId() {

    return id;
  }

  public String getName() {

    return name;
  }
}

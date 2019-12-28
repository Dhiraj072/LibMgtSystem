package com.github.dhiraj072.LibMgtSystem.exceptions;

import com.github.dhiraj072.LibMgtSystem.member.Member;

public class UsernameExistsException extends IllegalArgumentException {

  public UsernameExistsException(Member m) {

    super("A user with username " + m.getUserName() + " already exists");
  }
}

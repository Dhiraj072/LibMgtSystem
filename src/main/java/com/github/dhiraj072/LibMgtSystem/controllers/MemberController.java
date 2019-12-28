package com.github.dhiraj072.LibMgtSystem.controllers;

import com.github.dhiraj072.LibMgtSystem.Library;
import com.github.dhiraj072.LibMgtSystem.member.Member;
import javax.annotation.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class MemberController {

  @Resource
  private Library library;

  @GetMapping("/member/{username}")
  public Member getMember(@PathVariable String username) {

    Member m = library.getMember(username);
    if (m == null)
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No user with username " + username);
    return m;
  }

  @PostMapping("/member/add")
  public void addMember(@RequestBody Member member) {

    library.addMember(member);
  }
}

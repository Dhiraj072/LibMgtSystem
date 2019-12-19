package com.github.dhiraj072.LibMgtSystem;

import com.github.dhiraj072.LibMgtSystem.member.Member;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LibraryController {

  @Resource
  private Library library;

  @RequestMapping("/member/{username}")
  public Member getMember(@PathVariable String username) {

    return library.getMember(username);
  }
}

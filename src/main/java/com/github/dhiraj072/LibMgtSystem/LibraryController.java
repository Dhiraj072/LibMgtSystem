package com.github.dhiraj072.LibMgtSystem;

import com.github.dhiraj072.LibMgtSystem.member.Member;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LibraryController {

  private static final Logger LOGGER = LoggerFactory.getLogger(LibraryController.class);

  @Resource
  private Library library;

  @GetMapping("/test")
  public String test() {

    return "test";
  }

  @GetMapping("/member/{username}")
  public Member getMember(@PathVariable String username) {

    return library.getMember(username);
  }
}

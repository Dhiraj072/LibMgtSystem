package com.github.dhiraj072.LibMgtSystem.controllers;

import com.github.dhiraj072.LibMgtSystem.Library;
import com.github.dhiraj072.LibMgtSystem.exceptions.UsernameExistsException;
import com.github.dhiraj072.LibMgtSystem.member.Member;
import java.util.Map;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class MemberController {

  private static final Logger LOGGER = LoggerFactory.getLogger(MemberController.class);

  @Resource
  private Library library;

  @Resource
  private PasswordEncoder bCryptEncoder;

  @GetMapping("/member/{username}")
  public Member getMember(@PathVariable String username) {

    Member m = library.getMember(username);
    if (m == null)
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No user with username " + username);
    return m;
  }

  @PostMapping(value = "/member/add", consumes = MediaType.APPLICATION_JSON_VALUE)
  public Member addMember(@RequestBody Map<String, String> memberDetails) {

    LOGGER.info("member {}", memberDetails);
    String username = memberDetails.get("userName");
    String password = memberDetails.get("password");
    String email = memberDetails.get("email");
    String name = memberDetails.get("name");
    if (name == null)
      name = username;
    if (username == null || password == null || email == null)
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
          "Username/Password/Email cannot be null");
    Member toAdd = new Member(username, name, email, bCryptEncoder.encode(password));
    try {

      library.addMember(toAdd);
    } catch (UsernameExistsException e) {

      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
    }
    return toAdd;
  }
}

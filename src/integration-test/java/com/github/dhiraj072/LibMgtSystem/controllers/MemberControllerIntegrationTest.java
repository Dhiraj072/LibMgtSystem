package com.github.dhiraj072.LibMgtSystem.controllers;

import static io.restassured.RestAssured.given;

import com.github.dhiraj072.LibMgtSystem.ControllerIntegrationTest;
import com.github.dhiraj072.LibMgtSystem.Library;
import com.github.dhiraj072.LibMgtSystem.member.Member;
import io.restassured.RestAssured;
import java.time.LocalDate;
import javax.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;

@ControllerIntegrationTest
public class MemberControllerIntegrationTest {

  @LocalServerPort
  private int port;

  @Resource
  private Library library;

  @Resource
  private PasswordEncoder bCryptEncoder;

  @Test
  public void testAddsAMemberCorrectly() {

    RestAssured.port = port;
    given()
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .body("{\"userName\": \"test\", \"password\":\"pass\", \"email\": \"test@boo.com\"}")
    .when()
        .post("/member/add")
    .then()
        .statusCode(200);
    Member added = library.getMember("test");
    Assertions.assertEquals("test@boo.com", added.getEmail());
    Assertions.assertEquals(LocalDate.now(), added.getJoinDate());
    Assertions.assertEquals("test", added.getName());
    Assertions.assertEquals("test", added.getName());
    Assertions.assertTrue(bCryptEncoder.matches("pass", added.getPassword()));
  }
}

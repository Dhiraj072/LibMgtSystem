package com.github.dhiraj072.LibMgtSystem;

import com.github.dhiraj072.LibMgtSystem.member.Member;
import io.restassured.RestAssured;
import io.restassured.authentication.FormAuthConfig;
import javax.annotation.Resource;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class LibraryControllerTest {

  @LocalServerPort
  private int serverPort;

  @Resource
  private Library library;

  @Test
  @Disabled("Until auth support is added to this test")
  public void testGetsMemberViaRestAPI() {

    RestAssured.port = serverPort;
    library.addMember(new Member("foo", "foo@bar.com"));
    given()
        .auth().form("user", "password",
        new FormAuthConfig("/login", "username", "password"));
    when()
        .get("/member/foo").then().statusCode(200);
  }
}

package com.github.dhiraj072.LibMgtSystem;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import com.github.dhiraj072.LibMgtSystem.exceptions.UsernameExistsException;
import com.github.dhiraj072.LibMgtSystem.member.Member;
import io.restassured.RestAssured;
import io.restassured.authentication.FormAuthConfig;
import javax.annotation.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class LibraryControllerTest {

  @LocalServerPort
  private int serverPort;

  @Resource
  private Library library;

  @Resource
  private PasswordEncoder bCryptEncoder;

  private FormAuthConfig authConfig =
      new FormAuthConfig("/login", "username", "password");

  @BeforeEach
  public void setup() {

    try {

      library.addMember(new Member("foo", "foo@bar.com", bCryptEncoder.encode("pass")));
    } catch (UsernameExistsException e) {

      // consume
    }
  }

  @Test
  public void testGetsExistingMemberCorrectly() {

    RestAssured.port = serverPort;
    given()
        .auth().form("foo", "pass", authConfig)
    .when()
        .get("/member/foo")
    .then()
        .assertThat()
        .statusCode(200)
        .body("userName", equalTo("foo"))
        .body("password", equalTo(null)); // ensure we don't expose password via rest api
  }

  @Test
  public void testReturnBadStatusCodeForNonExistingMemer() {

    RestAssured.port = serverPort;
    given()
        .auth().form("foo", "pass", authConfig)
    .when()
        .get("/member/bar")
    .then()
        .assertThat()
        .statusCode(400);
  }
}

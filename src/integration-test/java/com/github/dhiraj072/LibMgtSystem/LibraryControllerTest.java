package com.github.dhiraj072.LibMgtSystem;

import com.github.dhiraj072.LibMgtSystem.member.Member;
import io.restassured.RestAssured;
import io.restassured.authentication.FormAuthConfig;
import javax.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class LibraryControllerTest {

  @LocalServerPort
  private int serverPort;

  @Resource
  private Library library;

  private FormAuthConfig authConfig = new FormAuthConfig("/login", "username", "password");

  @Test
  public void testGetsExistingMemberCorrectly() {

    RestAssured.port = serverPort;
    library.addMember(new Member("foo", "foo@bar.com"));
    given()
        .auth().form("user", "password", authConfig)
    .when()
        .get("/member/foo")
    .then()
        .assertThat()
        .statusCode(200)
        .body("userName", equalTo("foo"));
  }

  @Test
  public void testReturnBadStatusCodeForNonExistingMemer() {

    RestAssured.port = serverPort;
    given()
        .auth().form("user", "password", authConfig)
    .when()
        .get("/member/foo")
    .then()
        .assertThat()
        .statusCode(400);
  }
}

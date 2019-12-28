package com.github.dhiraj072.LibMgtSystem.auth;

import static io.restassured.RestAssured.given;

import com.github.dhiraj072.LibMgtSystem.Library;
import com.github.dhiraj072.LibMgtSystem.member.Member;
import io.restassured.RestAssured;
import io.restassured.authentication.FormAuthConfig;
import javax.annotation.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class LoginLogoutIntegrationTest {

  private static final Logger LOGGER = LoggerFactory.getLogger(LoginLogoutIntegrationTest.class);

  @LocalServerPort
  private int serverPort;

  @Resource
  private Library library;

  @Resource
  private PasswordEncoder bCryptEncoder;

  private static boolean setupDone;

  @BeforeEach
  public void setup() {

    LOGGER.info("Setup done {}", setupDone);
    if (setupDone)
      return;
    RestAssured.port = serverPort;
    library.addMember(new Member("foo", "foo@bar.com", bCryptEncoder.encode("pass")));
    setupDone = true;
  }

  @Test
  public void testLoginRejectsWithBadCredentials() {

    RestAssured.port = serverPort;
    given()
        .param("username", "user")
        .param("password", "paword")
        .when()
        .post("/login")
        .then()
        .statusCode(401);
  }

  @Test
  public void testLogsInCorrectlyWithGoodCredentials() {

    given()
        .param("username", "foo")
        .param("password", "pass")
        .when()
        .post("/login")
        .then()
        .statusCode(200);
  }

  @Test
  public void testLogOutsCorrectly() {

    given()
        .auth().form("foo", "pass",
        new FormAuthConfig("/login", "username", "password"))
    .when()
        .post("/logout")
        .then()
        .statusCode(302); // redirects to login
  }
}

package com.github.dhiraj072.LibMgtSystem.auth;

import static io.restassured.RestAssured.given;

import io.restassured.RestAssured;
import io.restassured.authentication.FormAuthConfig;
import io.restassured.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class LoginLogoutIntegrationTest {

  private static final Logger LOGGER = LoggerFactory.getLogger(LoginLogoutIntegrationTest.class);

  @LocalServerPort
  private int serverPort;

  private Cookie cookie;

  @BeforeEach
  public void setup() {

    RestAssured.port = serverPort;
  }

  @Test
  public void testLoginRejectsWithBadCredentials() {

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
        .param("username", "user")
        .param("password", "password")
        .when()
        .post("/login")
        .then()
        .statusCode(200);
  }

  @Test
  public void testLogOutsCorrectly() {

    given()
        .auth().form("user", "password",
        new FormAuthConfig("/login", "username", "password"))
    .when()
        .post("/logout")
        .then()
        .statusCode(302); // redirects to login
  }
}

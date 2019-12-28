package com.github.dhiraj072.LibMgtSystem.controllers;

import static io.restassured.RestAssured.get;

import com.github.dhiraj072.LibMgtSystem.ControllerIntegrationTest;
import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;
import org.springframework.boot.web.server.LocalServerPort;

@ControllerIntegrationTest
public class MemberControllerIntegrationTest {

  @LocalServerPort
  private int port;

  @Test
  public void test() {

    RestAssured.port = port;
    get("/test").then().statusCode(200);
  }
}

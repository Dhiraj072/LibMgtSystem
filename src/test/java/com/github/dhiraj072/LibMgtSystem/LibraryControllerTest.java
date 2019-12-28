package com.github.dhiraj072.LibMgtSystem;

import static org.mockito.ArgumentMatchers.anyString;

import com.github.dhiraj072.LibMgtSystem.controllers.MemberController;
import com.github.dhiraj072.LibMgtSystem.member.Member;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LibraryControllerTest {

  @Mock
  private Library library;

  @InjectMocks
  private MemberController controller;

  @BeforeEach
  void setup() {

    RestAssuredMockMvc.standaloneSetup(controller);
  }

  @Test
  void testGetsMemberCorrectly() {

    Member member = new Member("username", "email", "pass");
    MockitoAnnotations.initMocks(this);
    Mockito.when(library.getMember(anyString())).thenReturn(member);
    RestAssuredMockMvc
        .when()
          .get("/member/username")
        .then()
          .statusCode(200);
  }
}
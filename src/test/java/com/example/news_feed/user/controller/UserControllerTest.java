package com.example.news_feed.user.controller;

import com.example.news_feed.user.dto.request.SignupReqDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2, replace = AutoConfigureTestDatabase.Replace.ANY)
@TestPropertySource("classpath:application-test.yml")
@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {
    @Autowired
    ObjectMapper mapper;

    @Autowired
    MockMvc mvc;

    @Nested
    @DisplayName("signup")
    class signup {
        @Test
        @Transactional
        void signup_ok() throws Exception {

            // given
            String username = "newtest";
            String email = "newtest@gmail.com";
            String pwd = "newsfeed12!";

            // when(객체 변환)
            String body = mapper.writeValueAsString(
                    SignupReqDto.builder()
                            .username(username)
                            .email(email)
                            .pwd(pwd)
                            .build()
            );

            // then
            mvc.perform(post("/user/signup")
                            .content(body) // body
                            .contentType(MediaType.APPLICATION_JSON) // 타입 명시
                    )
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.statusCode").value(200))
                    .andExpect(jsonPath("$.message").value("회원 가입 완료"));
            }
        }
    }

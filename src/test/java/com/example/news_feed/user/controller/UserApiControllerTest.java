package com.example.news_feed.user.controller;

import com.example.news_feed.user.dto.request.SignupReqDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
@SpringBootTest
@AutoConfigureMockMvc
class UserApiControllerTest {

    @Autowired
    ObjectMapper mapper;

    @Autowired
    MockMvc mvc;
    @Test
    @Transactional
    void signup_ok() throws Exception {

        // given
        String username = "testing";
        String email = "testing@gmail.com";
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
                .andExpect(content().json("{\"statusCode\":200,\"message\":\"회원 가입 완료\"}"));
    }

    @Test
    @Transactional
    void signup_fail_invalid_pwd() throws Exception {
        // given
        String username = "testname2";
        String email = "test2@gmail.com";
        String pwd = "newsfeed";

        // when
        String body = mapper.writeValueAsString(
                SignupReqDto.builder()
                        .username(username)
                        .email(email)
                        .pwd(pwd)
                        .build()
        );

        // then
        mvc.perform(post("/user/signup")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"statusCode\":400,\"message\":\"[비밀번호는 알파벳 대소문자, 숫자, 특수문자를 포함하여 8~15자여야 합니다.]\"}"));
    }

    @Test
    @Transactional
    void signup_fail_duplicate_email() throws Exception {
        // given
        String username = "admin23";
        String email = "admin@gmail.com";
        String pwd = "asdf1234!";

        // when
        String body = mapper.writeValueAsString(
                SignupReqDto.builder()
                        .username(username)
                        .email(email)
                        .pwd(pwd)
                        .build()
        );

        // then
        mvc.perform(post("/user/signup")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"statusCode\":400,\"message\":\"중복된 이메일이 존재합니다.\"}"));
    }
}
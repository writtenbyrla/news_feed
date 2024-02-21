package com.example.news_feed.user.controller;

import com.example.news_feed.auth.security.UserDetailsImpl;
import com.example.news_feed.auth.security.UserDetailsServiceImpl;
import com.example.news_feed.user.dto.request.LoginReqDto;
import com.example.news_feed.user.dto.request.SignupReqDto;
import com.example.news_feed.user.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
class UserApiControllerTest {

    @Autowired
    ObjectMapper mapper;

    @Autowired
    MockMvc mvc;

    private Authentication authentication;

    @BeforeEach
    void setup() {
        UserDetails userDetails = new UserDetailsImpl(2L, "admin@gmail.com", "awsedr12!", "admin", Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN")));
        authentication = new TestingAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    @Nested
    @DisplayName("signup")
    class signup{
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
                    .andExpect(jsonPath("$.statusCode").value(400))
                    .andExpect(jsonPath("$.message").value("[비밀번호는 알파벳 대소문자, 숫자, 특수문자를 포함하여 8~15자여야 합니다.]"));
        }

        @Test
        @Transactional
        void signup_fail_duplicated_email() throws Exception {
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
                    .andExpect(jsonPath("$.statusCode").value(400))
                    .andExpect(jsonPath("$.message").value("중복된 이메일이 존재합니다."));
        }
    }

    @Nested
    @DisplayName("login")
    class Login{
        @Test
        @Transactional
        void login_ok() throws Exception {

            // given
            String email = "admin@gmail.com";
            String pwd = "awsedr12!";

            // when
            String body = mapper.writeValueAsString(
                    LoginReqDto.builder()
                            .email(email)
                            .pwd(pwd)
                            .build()
            );

            // then
            mvc.perform(post("/user/login")
                            .content(body)
                            .contentType(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(status().isOk())
                    .andDo(print());

        }

        @Test
        @Transactional
        void login_fail_not_fount_user() throws Exception {
            // given
            String email = "newuser@gmail.com";
            String pwd = "awsedr12!";

            // when
            String body = mapper.writeValueAsString(
                    LoginReqDto.builder()
                            .email(email)
                            .pwd(pwd)
                            .build()
            );

            // then
            mvc.perform(post("/user/login")
                            .content(body)
                            .contentType(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.statusCode").value(400))
                    .andExpect(jsonPath("$.message").value("등록된 사용자가 없습니다."));
        }

        @Test
        @Transactional
        void login_invalid_user() throws Exception {
            // given
            String email = "user@gmail.com";
            String pwd = "awsedr12!";

            // when
            String body = mapper.writeValueAsString(
                    LoginReqDto.builder()
                            .email(email)
                            .pwd(pwd)
                            .build()
            );

            // then
            mvc.perform(post("/user/login")
                            .content(body)
                            .contentType(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.statusCode").value(400))
                    .andExpect(jsonPath("$.message").value("이미 탈퇴한 회원입니다."));
        }

        @Test
        @Transactional
        void login_missMatch_pwd() throws Exception {
            // given
            String email = "admin@gmail.com";
            String pwd = "awsedr123!";

            // when
            String body = mapper.writeValueAsString(
                    LoginReqDto.builder()
                            .email(email)
                            .pwd(pwd)
                            .build()
            );

            // then
            mvc.perform(post("/user/login")
                            .content(body)
                            .contentType(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.statusCode").value(400))
                    .andExpect(jsonPath("$.message").value("비밀번호가 일치하지 않습니다."));
        }
    }

    @Nested
    @DisplayName("logout")
    class Logout{
        @Test
        @Transactional
        void logout_ok () throws Exception {
            // 실제 토큰값 넣기
            String accessToken = "";
            String refreshToken = "";

            // then
            mvc.perform(post("/user/logout")
                            .header("Authorization", "Bearer " + accessToken)
                            .header("Refresh", refreshToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .with(authentication(authentication))
                    )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.statusCode").value(200))
                    .andExpect(jsonPath("$.message").value("로그아웃 완료!"))
                    .andDo(print());
        }
        @Test
        @Transactional
        void logout_fail_already_logout () throws Exception {
            // 실제 토큰값 넣기
            String accessToken = "";
            String refreshToken = "";

            // then
            mvc.perform(post("/user/logout")
                            .header("Authorization", "Bearer " + accessToken)
                            .header("Refresh", refreshToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .with(authentication(authentication))
                    )
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.statusCode").value(400))
                    .andExpect(jsonPath("$.message").value("이미 로그아웃하셨습니다."));
        }
    }

    @Nested
    @DisplayName("UserList")
    class UserList {
        @Test
        void all_user_list_ok() throws Exception{
            // then
            mvc.perform(get("/user/list")
                            .contentType(MediaType.APPLICATION_JSON)
                            .with(authentication(authentication))
                    )
                    .andExpect(status().isOk())
                    .andDo(print());
        }

        @Test
        void search_user_ok() throws Exception{
            String username = "ad";

            // then
            mvc.perform(get("/user/search")
                            .param("username", username) // 쿼리 매개변수로 전달
                            .contentType(MediaType.APPLICATION_JSON)
                            .with(authentication(authentication))
                    )
                    .andExpect(status().isOk())
                    .andDo(print());
        }

        @Test
        void search_user_fail_not_fount_user() throws Exception{
            String username = "useruser";

            // then
            mvc.perform(get("/user/search")
                            .param("username", username) // 쿼리 매개변수로 전달
                            .contentType(MediaType.APPLICATION_JSON)
                            .with(authentication(authentication))
                    )
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.statusCode").value(400))
                    .andExpect(jsonPath("$.message").value("등록된 사용자가 없습니다."));
        }

    }
}
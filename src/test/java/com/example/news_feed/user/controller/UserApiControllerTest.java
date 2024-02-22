package com.example.news_feed.user.controller;

import com.example.news_feed.auth.security.UserDetailsImpl;
import com.example.news_feed.user.dto.request.LoginReqDto;
import com.example.news_feed.user.dto.request.SignupReqDto;
import com.example.news_feed.user.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;
import java.util.Objects;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
@TestPropertySource("classpath:application-test.yml")
@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
class UserApiControllerTest {

    @Autowired
    ObjectMapper mapper;

    @Autowired
    MockMvc mvc;

    @Autowired
    UserRepository userRepository;

    private Authentication authentication;

    @BeforeEach
    void setup() {
        // Spring Security 인증 객체 생성
        UserDetails userDetails = new UserDetailsImpl(1L, "jidong@gmail.com", "awsedr12!", "jidong", List.of(new SimpleGrantedAuthority("ROLE_USER")));
        authentication = new TestingAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    @Nested
    @DisplayName("signup")
    class signup{
        @Test
        @Transactional
        void signup_ok() throws Exception {

            // given
            String username = "newnew";
            String email = "newnew@gmail.com";
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
                    .andExpect(jsonPath("$.statusCode").value(201))
                    .andExpect(jsonPath("$.message").value("회원 가입 완료"));
        }

        @Test
        @Transactional
        void signup_fail_invalid_pwd() throws Exception {

            // given
            String username = "user";
            String email = "user@gmail.com";
            String pwd = "newsfeed12";

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

            // when
            String body = mapper.writeValueAsString(
                    SignupReqDto.builder()
                            .username("newnew")
                            .email("jidong@gmail.com")
                            .pwd("asdf1234!")
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
            String email = "jidong@gmail.com";
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
                    .andExpect(jsonPath("$.email").value(email))
                    .andExpect(jsonPath("$.username").value("jidong"))
                    .andExpect(jsonPath("$.role").value("USER"))
                    .andDo(print());
        }

        @Test
        @Transactional
        void login_fail_not_found_user() throws Exception {
            // given
            String email = "user123@gmail.com";
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
            String email = "deleted@gmail.com";
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
            String email = "jidong@gmail.com";
            String pwd = "awsedr1234!";

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
    class logout {

        @Test
        @Transactional
        void logout_ok() throws Exception {
            // given
            MvcResult loginResult = login_ok_sample();

            // 로그인 응답에서 토큰 추출
            String accessToken = Objects.requireNonNull(loginResult.getResponse().getHeader("Authorization")).substring("Bearer ".length());
            String refreshToken = loginResult.getResponse().getHeader("Refresh");

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
        void logout_fail_already_logout() throws Exception {
            // given
            MvcResult loginResult = login_ok_sample();

            // 로그인 응답에서 토큰 추출
            String accessToken = Objects.requireNonNull(loginResult.getResponse().getHeader("Authorization")).substring("Bearer ".length());
            String refreshToken = loginResult.getResponse().getHeader("Refresh");

            // 1차 로그아웃
            logout_ok();

            // then(2차 로그아웃)
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

        private MvcResult login_ok_sample() throws Exception {
            // given
            String email = "jidong@gmail.com";
            String pwd = "awsedr12!";

            // when
            String body = mapper.writeValueAsString(
                    LoginReqDto.builder()
                            .email(email)
                            .pwd(pwd)
                            .build()
            );

            // then
            return mvc.perform(post("/user/login")
                            .content(body)
                            .contentType(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.email").value(email))
                    .andExpect(jsonPath("$.username").value("jidong"))
                    .andExpect(jsonPath("$.role").value("USER"))
                    .andDo(print())
                    .andReturn();
        }
    }

    @Nested
    @DisplayName("UserList")
    class UserList {

        @Test
        @Transactional
        void all_user_list_ok() throws Exception{
            // then
            mvc.perform(get("/user/list")
                            .contentType(MediaType.APPLICATION_JSON)
                            .with(authentication(authentication))
                    )
                    .andExpect(status().isOk());
        }
        @Test
        @Transactional
        void search_user_ok() throws Exception{
            String username = "jieun";

            mvc.perform(get("/user/search")
                            .param("username", username) // 쿼리 매개변수로 전달
                            .contentType(MediaType.APPLICATION_JSON)
                            .with(authentication(authentication))
                    )
                    .andExpect(status().isOk())
                    .andDo(print());
        }
        @Test
        @Transactional
        void search_user_fail_not_fount_user() throws Exception{
            String username = "useruser";

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
package com.example.news_feed.follow.controller;

import com.example.news_feed.auth.security.UserDetailsImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestPropertySource("classpath:application-test.yml")
@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
class FollowApicontrollerTest {
    @Autowired
    ObjectMapper mapper;

    @Autowired
    MockMvc mvc;

    private Authentication authentication;

    @BeforeEach
    void setup() {
        // Spring Security 인증 객체 생성
        UserDetails userDetails = new UserDetailsImpl(1L, "jidong@gmail.com", "awsedr12!", "jidong", List.of(new SimpleGrantedAuthority("ROLE_USER")));
        authentication = new TestingAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    @Nested
    @DisplayName("create")
    class create{
        @Test
        @Transactional
        void create_ok() throws Exception {
            mvc.perform(post("/follow/" + 5)
                            .contentType(MediaType.APPLICATION_JSON)
                            .with(authentication(authentication))
                    )
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.statusCode").value(201))
                    .andExpect(jsonPath("$.message").value("팔로우 완료"));
        }

        @Test
        @Transactional
        void create_fail_not_found_following() throws Exception {
            mvc.perform(post("/follow/" + 100)
                            .contentType(MediaType.APPLICATION_JSON)
                            .with(authentication(authentication))
                    )
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.statusCode").value(404))
                    .andExpect(jsonPath("$.message").value("등록된 사용자가 없습니다."));
        }

        @Test
        @Transactional
        void create_fail_self_following() throws Exception {
            mvc.perform(post("/follow/" + 1)
                            .contentType(MediaType.APPLICATION_JSON)
                            .with(authentication(authentication))
                    )
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.statusCode").value(400))
                    .andExpect(jsonPath("$.message").value("자신을 팔로우할 수 없습니다."));
        }

        @Test
        @Transactional
        void create_fail_already_following() throws Exception {
            mvc.perform(post("/follow/" + 2)
                            .contentType(MediaType.APPLICATION_JSON)
                            .with(authentication(authentication))
                    )
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.statusCode").value(400))
                    .andExpect(jsonPath("$.message").value("이미 팔로우중입니다."));
        }
    }

    @Nested
    @DisplayName("delete")
    class delete{
        @Test
        @Transactional
        void delete_ok() throws Exception {
            // then
            mvc.perform(MockMvcRequestBuilders.delete("/follow/" +2)
                            .contentType(MediaType.APPLICATION_JSON)
                            .with(authentication(authentication))
                    )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.statusCode").value(200))
                    .andExpect(jsonPath("$.message").value("팔로우 취소 완료"));
        }

        @Test
        @Transactional
        void delete_fail_not_found_follow() throws Exception {
            // then
            mvc.perform(MockMvcRequestBuilders.delete("/follow/" +100)
                            .contentType(MediaType.APPLICATION_JSON)
                            .with(authentication(authentication))
                    )
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.statusCode").value(404))
                    .andExpect(jsonPath("$.message").value("팔로우 정보가 없습니다."));
        }

    }

    @Nested
    @DisplayName("followingFeed")
    class followingFeed {
        @Test
        @Transactional
        void postList_ok() throws Exception {
            mvc.perform(get("/follow/post")
                            .contentType(MediaType.APPLICATION_JSON)
                            .with(authentication(authentication))
                    )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content[0].title").value("좋아요 눌러줘"))
                    .andExpect(jsonPath("$.content[1].title").value("소셜로그인 언제하지"))
                    .andDo(print());
        }

        @Test
        @Transactional
        void postList_fail_not_found_following() throws Exception {
            UserDetailsImpl userDetails = new UserDetailsImpl(4L, "newuser@gmail.com", "awsedr12!", "newuser", Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));
            authentication = new TestingAuthenticationToken(userDetails, null, userDetails.getAuthorities());

            mvc.perform(get("/follow/post")
                            .contentType(MediaType.APPLICATION_JSON)
                            .with(authentication(authentication))
                    )
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.statusCode").value(404))
                    .andExpect(jsonPath("$.message").value("팔로우 정보가 없습니다."));
        }

        @Test
        @Transactional
        void postList_fail_not_found_post() throws Exception {
            UserDetailsImpl userDetails = new UserDetailsImpl(2L, "jieun@gmail.com", "awsedr12!", "jieun", Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));
            authentication = new TestingAuthenticationToken(userDetails, null, userDetails.getAuthorities());

            mvc.perform(get("/follow/post")
                            .contentType(MediaType.APPLICATION_JSON)
                            .with(authentication(authentication))
                    )
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.statusCode").value(404))
                    .andExpect(jsonPath("$.message").value("게시글 정보가 없습니다."));
        }
    }
}
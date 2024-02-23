package com.example.news_feed.comment.controller;

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

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestPropertySource("classpath:application-test.yml")
@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
class CommentLikeApiControllerTest {

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
            mvc.perform(post("/comment/" + 3 +"/like")
                            .contentType(MediaType.APPLICATION_JSON)
                            .with(authentication(authentication))
                    )
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.statusCode").value(201))
                    .andExpect(jsonPath("$.message").value("댓글 좋아요 +1"));
        }

        @Test
        @Transactional
        void create_fail_not_found_comment() throws Exception {
            // given
            // when
            // then
            mvc.perform(post("/comment/" + 100 +"/like")
                            .contentType(MediaType.APPLICATION_JSON)
                            .with(authentication(authentication))
                    )
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.statusCode").value(404))
                    .andExpect(jsonPath("$.message").value("댓글 정보가 없습니다."));
        }

        @Test
        @Transactional
        void create_fail_self_like() throws Exception {
            // given
            // when
            // then
            mvc.perform(post("/comment/" + 1 +"/like")
                            .contentType(MediaType.APPLICATION_JSON)
                            .with(authentication(authentication))
                    )
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.statusCode").value(400))
                    .andExpect(jsonPath("$.message").value("본인의 댓글에는 좋아요를 할 수 없습니다."));
        }

        @Test
        @Transactional
        void create_fail_already_like() throws Exception {
            // given
            create_ok();

            mvc.perform(post("/comment/" + 3 +"/like")
                            .contentType(MediaType.APPLICATION_JSON)
                            .with(authentication(authentication))
                    )
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.statusCode").value(400))
                    .andExpect(jsonPath("$.message").value("이미 이 댓글을 좋아합니다."));
        }
    }


    @Nested
    @DisplayName("delete")
    class delete{
        @Test
        @Transactional
        void delete_ok() throws Exception {
            // when
            // then
            mvc.perform(MockMvcRequestBuilders.delete("/comment/" + 1 +"/like")
                            .contentType(MediaType.APPLICATION_JSON)
                            .with(authentication(authentication))
                    )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.statusCode").value(200))
                    .andExpect(jsonPath("$.message").value("댓글 좋아요 취소"));
        }

        @Test
        @Transactional
        void delete_fail_not_found_comment_like() throws Exception {
            // given
            // when
            // then
            mvc.perform(MockMvcRequestBuilders.delete("/comment/" + 10 +"/like")
                            .contentType(MediaType.APPLICATION_JSON)
                            .with(authentication(authentication))
                    )
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.statusCode").value(404))
                    .andExpect(jsonPath("$.message").value("댓글 좋아요 정보가 없습니다."));
        }
    }
}
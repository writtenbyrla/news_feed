package com.example.news_feed.post.controller;

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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class PostLikeApiControllerTest {
    @Autowired
    ObjectMapper mapper;

    @Autowired
    MockMvc mvc;

    private Authentication authentication;

    @BeforeEach
    void setup() {
        UserDetails userDetails = new UserDetailsImpl(3L, "jidong@gmail.com", "jidong123!", "jidong", Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));
        authentication = new TestingAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    @Nested
    @DisplayName("create")
    class create{
        @Test
        @Transactional
        void create_post_like_ok() throws Exception {
            // given
            // when
            // then
            mvc.perform(post("/post/" + 1 +"/like")
                            .contentType(MediaType.APPLICATION_JSON)
                            .with(authentication(authentication))
                    )
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.statusCode").value(201))
                    .andExpect(jsonPath("$.message").value("게시글 좋아요 +1"));
        }

        @Test
        @Transactional
        void create_post_like_fail_not_found_post() throws Exception {
            // given
            // when
            // then
            mvc.perform(post("/post/" + 100 +"/like")
                            .contentType(MediaType.APPLICATION_JSON)
                            .with(authentication(authentication))
                    )
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.statusCode").value(400))
                    .andExpect(jsonPath("$.message").value("게시글 정보가 없습니다."));
        }

        @Test
        @Transactional
        void create_post_like_fail_self_like() throws Exception {
            // given
            // when
            // then
            mvc.perform(post("/post/" + 27 +"/like")
                            .contentType(MediaType.APPLICATION_JSON)
                            .with(authentication(authentication))
                    )
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.statusCode").value(400))
                    .andExpect(jsonPath("$.message").value("본인의 글에는 좋아요를 할 수 없습니다."));
        }

        @Test
        @Transactional
        void create_post_like_fail_already_like() throws Exception {
            // given
            // when
            // then
            mvc.perform(post("/post/" + 5 +"/like")
                            .contentType(MediaType.APPLICATION_JSON)
                            .with(authentication(authentication))
                    )
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.statusCode").value(400))
                    .andExpect(jsonPath("$.message").value("이미 이 게시글을 좋아합니다."));
        }
    }

    @Nested
    @DisplayName("delete")
    class delete{
        @Test
        @Transactional
        void delete_post_like_ok() throws Exception {
            // given
            // when
            // then
            mvc.perform(MockMvcRequestBuilders.delete("/post/" + 1 +"/like")
                            .contentType(MediaType.APPLICATION_JSON)
                            .with(authentication(authentication))
                    )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.statusCode").value(200))
                    .andExpect(jsonPath("$.message").value("게시글 좋아요 취소"));
        }

        @Test
        @Transactional
        void delete_post_like_fail_not_fount_post_like() throws Exception {
            // given
            // when
            // then
            mvc.perform(MockMvcRequestBuilders.delete("/post/" + 10 +"/like")
                            .contentType(MediaType.APPLICATION_JSON)
                            .with(authentication(authentication))
                    )
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.statusCode").value(400))
                    .andExpect(jsonPath("$.message").value("게시글 좋아요 정보가 없습니다."));
        }
    }
}
package com.example.news_feed.admin.controller;

import com.example.news_feed.auth.security.UserDetailsImpl;
import com.example.news_feed.comment.dto.request.UpdateCommentDto;
import com.example.news_feed.user.repository.UserRepository;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestPropertySource("classpath:application-test.yml")
@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
class AdminCommentControllerTest {
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
        UserDetails userDetails = new UserDetailsImpl(5L, "user@gmail.com", "newsfeed12!", "user", List.of(new SimpleGrantedAuthority("ROLE_ADMIN")));
        authentication = new TestingAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    @Nested
    @DisplayName("update")
    class update {
        @Test
        @Transactional
        void update_ok() throws Exception {
            // given
            String content = "댓글 내용 수정합니다.";

            // when(객체 변환)
            String body = mapper.writeValueAsString(
                    UpdateCommentDto.builder()
                            .content(content)
                            .build()
            );

            // then
            mvc.perform(patch("/admin/comment/"+1)
                            .content(body)
                            .contentType(MediaType.APPLICATION_JSON)
                            .with(authentication(authentication))
                    )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.statusCode").value(200))
                    .andExpect(jsonPath("$.message").value("댓글 수정 완료"));
        }

        @Test
        @Transactional
        void update_fail_not_found_comment() throws Exception {
            // given
            String content = "댓글 내용 수정합니다.";

            // when(객체 변환)
            String body = mapper.writeValueAsString(
                    UpdateCommentDto.builder()
                            .content(content)
                            .build()
            );

            // then
            mvc.perform(patch("/admin/comment/"+100)
                            .content(body)
                            .contentType(MediaType.APPLICATION_JSON)
                            .with(authentication(authentication))
                    )
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.statusCode").value(404))
                    .andExpect(jsonPath("$.message").value("댓글 정보가 없습니다."));
        }
    }

    @Nested
    @DisplayName("delete")
    class delete{
        @Test
        @Transactional
        void delete_ok() throws Exception {
            // then
            mvc.perform(MockMvcRequestBuilders.delete("/admin/comment/"+1)
                            .contentType(MediaType.APPLICATION_JSON)
                            .with(authentication(authentication))
                    )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.statusCode").value(200))
                    .andExpect(jsonPath("$.message").value("댓글 삭제 완료"));
        }

        @Test
        @Transactional
        void delete_fail_not_found_comment() throws Exception {

            // then
            mvc.perform(MockMvcRequestBuilders.delete("/admin/comment/"+100)
                            .contentType(MediaType.APPLICATION_JSON)
                            .with(authentication(authentication))
                    )
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.statusCode").value(404))
                    .andExpect(jsonPath("$.message").value("댓글 정보가 없습니다."));
        }


    }
}
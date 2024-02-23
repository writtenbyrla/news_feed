package com.example.news_feed.comment.controller;

import com.example.news_feed.auth.security.UserDetailsImpl;
import com.example.news_feed.comment.dto.request.CreateCommentDto;
import com.example.news_feed.comment.dto.request.UpdateCommentDto;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestPropertySource("classpath:application-test.yml")
@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
class CommentApiControllerTest {

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
    class create {
        @Test
        @Transactional
        void create_ok() throws Exception {
            // given
            String content = "댓글 테스트중";

            // when(객체 변환)
            String body = mapper.writeValueAsString(
                    CreateCommentDto.builder()
                            .content(content)
                            .build()
            );

            // then
            mvc.perform(post("/post/"+3+"/comment")
                            .content(body) // body
                            .contentType(MediaType.APPLICATION_JSON) // 타입 명시
                            .with(authentication(authentication))
                    )
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.statusCode").value(201))
                    .andExpect(jsonPath("$.message").value("댓글 등록 완료"));
        }

        @Test
        @Transactional
        void create_fail_not_found_post() throws Exception {
            // given
            String content = "댓글 테스트중";

            // when(객체 변환)
            String body = mapper.writeValueAsString(
                    CreateCommentDto.builder()
                            .content(content)
                            .build()
            );

            // then
            mvc.perform(post("/post/"+100+"/comment")
                            .content(body) // body
                            .contentType(MediaType.APPLICATION_JSON) // 타입 명시
                            .with(authentication(authentication))
                    )
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.statusCode").value(404))
                    .andExpect(jsonPath("$.message").value("게시글 정보가 없습니다."));
        }
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
            mvc.perform(patch("/comment/"+1)
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
            mvc.perform(patch("/comment/"+100)
                            .content(body)
                            .contentType(MediaType.APPLICATION_JSON)
                            .with(authentication(authentication))
                    )
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.statusCode").value(404))
                    .andExpect(jsonPath("$.message").value("댓글 정보가 없습니다."));
        }

        @Test
        @Transactional
        void update_fail_unauthorized_user() throws Exception {
            // given
            String content = "댓글 내용 수정합니다.";

            // when(객체 변환)
            String body = mapper.writeValueAsString(
                    UpdateCommentDto.builder()
                            .content(content)
                            .build()
            );

            // then
            mvc.perform(patch("/comment/"+3)
                            .content(body)
                            .contentType(MediaType.APPLICATION_JSON)
                            .with(authentication(authentication))
                    )
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.statusCode").value(400))
                    .andExpect(jsonPath("$.message").value("본인이 아닙니다."));
        }

    }

    @Nested
    @DisplayName("delete")
    class delete{
        @Test
        @Transactional
        void delete_ok() throws Exception {
            // then
            mvc.perform(MockMvcRequestBuilders.delete("/comment/"+1)
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
            mvc.perform(MockMvcRequestBuilders.delete("/comment/"+100)
                            .contentType(MediaType.APPLICATION_JSON)
                            .with(authentication(authentication))
                    )
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.statusCode").value(404))
                    .andExpect(jsonPath("$.message").value("댓글 정보가 없습니다."));
        }

        @Test
        @Transactional
        void delete_fail_unauthorized_user() throws Exception {

            // then
            mvc.perform(MockMvcRequestBuilders.delete("/comment/"+3)
                            .contentType(MediaType.APPLICATION_JSON)
                            .with(authentication(authentication))
                    )
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.statusCode").value(400))
                    .andExpect(jsonPath("$.message").value("본인이 아닙니다."));
        }


    }

    @Nested
    @DisplayName("commentList")
    class commentList{
        @Test
        @Transactional
        void commentList_ok() throws Exception {
            mvc.perform(get("/post/"+3+"/comment")
                            .contentType(MediaType.APPLICATION_JSON)
                            .with(authentication(authentication))
                    )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content[0].content").value("ㅋㅋㅋㅋ"))
                    .andExpect(jsonPath("$.content[1].content").value("ㅎㅎㅎ"))
                    .andExpect(jsonPath("$.content[2].content").value("빨리 해야겠다"))
                    .andExpect(jsonPath("$.content[3].content").value("나도 아직 못함"))
                    .andDo(print());
        }

        @Test
        @Transactional
        void comment_detail_ok() throws Exception {
            mvc.perform(get("/comment/"+3)
                            .contentType(MediaType.APPLICATION_JSON)
                            .with(authentication(authentication))
                    )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content").value("ㅎㅎㅎ"))
                    .andDo(print());
        }

        @Test
        @Transactional
        void comment_detail_fail_not_found_comment() throws Exception {
            mvc.perform(get("/comment/"+100)
                            .contentType(MediaType.APPLICATION_JSON)
                            .with(authentication(authentication))
                    )
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.statusCode").value(404))
                    .andExpect(jsonPath("$.message").value("댓글 정보가 없습니다."));
        }
    }

}
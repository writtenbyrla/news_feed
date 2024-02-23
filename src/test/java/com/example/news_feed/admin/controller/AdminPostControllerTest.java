package com.example.news_feed.admin.controller;

import com.example.news_feed.auth.security.UserDetailsImpl;
import com.example.news_feed.post.dto.request.UpdatePostDto;
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

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestPropertySource("classpath:application-test.yml")
@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
class AdminPostControllerTest {

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
    class update{
        @Test
        @Transactional
        void update_ok() throws Exception {
            // given
            String title = "제목 수정";
            String content = "내용도 수정";

            // when(객체 변환)
            String body = mapper.writeValueAsString(
                    UpdatePostDto.builder()
                            .title(title)
                            .content(content)
                            .build()
            );

            // then
            mvc.perform(patch("/admin/post/"+1)
                            .content(body) // body
                            .contentType(MediaType.APPLICATION_JSON) // 타입 명시
                            .with(authentication(authentication))
                    )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.statusCode").value(200))
                    .andExpect(jsonPath("$.message").value("게시물 수정 완료"));
        }
        @Test
        @Transactional
        void update_ok_title() throws Exception {
            // given
            String title = "제목만 수정";

            // when(객체 변환)
            String body = mapper.writeValueAsString(
                    UpdatePostDto.builder()
                            .title(title)
                            .build()
            );

            // then
            mvc.perform(patch("/admin/post/"+1)
                            .content(body) // body
                            .contentType(MediaType.APPLICATION_JSON) // 타입 명시
                            .with(authentication(authentication))
                    )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.statusCode").value(200))
                    .andExpect(jsonPath("$.message").value("게시물 수정 완료"));
        }

        @Test
        @Transactional
        void update_ok_content() throws Exception {
            // given
            String content = "내용만 수정";

            // when(객체 변환)
            String body = mapper.writeValueAsString(
                    UpdatePostDto.builder()
                            .content(content)
                            .build()
            );

            // then
            mvc.perform(patch("/admin/post/"+1)
                            .content(body) // body
                            .contentType(MediaType.APPLICATION_JSON) // 타입 명시
                            .with(authentication(authentication))
                    )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.statusCode").value(200))
                    .andExpect(jsonPath("$.message").value("게시물 수정 완료"));
        }

        @Test
        @Transactional
        void update_fail_not_found_post() throws Exception {
            // given
            String title = "제목 수정";
            String content = "내용도 수정";

            // when(객체 변환)
            String body = mapper.writeValueAsString(
                    UpdatePostDto.builder()
                            .title(title)
                            .content(content)
                            .build()
            );

            // then
            mvc.perform(patch("/admin/post/"+100)
                            .content(body)
                            .contentType(MediaType.APPLICATION_JSON)
                            .with(authentication(authentication))
                    )
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.statusCode").value(404))
                    .andExpect(jsonPath("$.message").value("게시글 정보가 없습니다."));
        }
    }

    @Nested
    @DisplayName("delete")
    class delete{
        @Test
        @Transactional
        void delete_ok() throws Exception {

            // then
            mvc.perform(MockMvcRequestBuilders.delete("/admin/post/"+1)
                            .contentType(MediaType.APPLICATION_JSON)
                            .with(authentication(authentication))
                    )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.statusCode").value(200))
                    .andExpect(jsonPath("$.message").value("게시물 삭제 완료"));
        }

        @Test
        @Transactional
        void delete_fail_not_found_post() throws Exception {

            // then
            mvc.perform(MockMvcRequestBuilders.delete("/admin/post/"+100)
                            .contentType(MediaType.APPLICATION_JSON)
                            .with(authentication(authentication))
                    )
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.statusCode").value(404))
                    .andExpect(jsonPath("$.message").value("게시글 정보가 없습니다."));
        }
    }

    @Nested
    @DisplayName("multiMedia")
    class multiMedia {
        @Test
        @Transactional
        void delete_ok() throws Exception {
            mvc.perform(MockMvcRequestBuilders.delete("/admin/post/"+1+"/file")
                            .contentType(MediaType.APPLICATION_JSON)
                            .with(authentication(authentication))
                    )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.statusCode").value(200))
                    .andExpect(jsonPath("$.message").value("멀티미디어 삭제 완료"));
        }

        @Test
        @Transactional
        void delete_fail_not_found_multimedia() throws Exception {
            // then
            mvc.perform(MockMvcRequestBuilders.delete("/admin/post/"+100+"/file")
                            .contentType(MediaType.APPLICATION_JSON)
                            .with(authentication(authentication))
                    )
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.statusCode").value(404))
                    .andExpect(jsonPath("$.message").value("파일을 찾을 수 없습니다."));
        }
    }
}
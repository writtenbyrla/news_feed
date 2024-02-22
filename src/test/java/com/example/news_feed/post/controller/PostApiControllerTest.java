package com.example.news_feed.post.controller;

import com.example.news_feed.auth.security.UserDetailsImpl;
import com.example.news_feed.post.dto.request.CreatePostDto;
import com.example.news_feed.post.dto.request.UpdatePostDto;
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

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class PostApiControllerTest {

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
        void create_ok() throws Exception {
            // given
            String title = "게시글 작성";
            String content = "테스트중";

            // when(객체 변환)
            String body = mapper.writeValueAsString(
                    CreatePostDto.builder()
                            .title(title)
                            .content(content)
                            .build()
            );

            // then
            mvc.perform(post("/post")
                            .content(body) // body
                            .contentType(MediaType.APPLICATION_JSON) // 타입 명시
                            .with(authentication(authentication))
                    )
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.statusCode").value(201))
                    .andExpect(jsonPath("$.message").value("게시물 등록 완료"));
        }

        @Test
        @Transactional
        void create_fail_empty_title() throws Exception {
            // given
            String content = "테스트중";

            // when(객체 변환)
            String body = mapper.writeValueAsString(
                    CreatePostDto.builder()
                            .title("")
                            .content(content)
                            .build()
            );

            // then
            mvc.perform(post("/post")
                            .content(body) // body
                            .contentType(MediaType.APPLICATION_JSON) // 타입 명시
                            .with(authentication(authentication))
                    )
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.statusCode").value(400))
                    .andExpect(jsonPath("$.message").value("제목을 입력해주세요."));
        }

        @Test
        @Transactional
        void create_fail_empty_content() throws Exception {
            // given
            String title = "게시글 작성중";

            // when(객체 변환)
            String body = mapper.writeValueAsString(
                    CreatePostDto.builder()
                            .title(title)
                            .content("")
                            .build()
            );

            // then
            mvc.perform(post("/post")
                            .content(body) // body
                            .contentType(MediaType.APPLICATION_JSON) // 타입 명시
                            .with(authentication(authentication))
                    )
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.statusCode").value(400))
                    .andExpect(jsonPath("$.message").value("내용을 입력해주세요."));
        }


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
            mvc.perform(patch("/post/"+27)
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
            mvc.perform(patch("/post/"+27)
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
            mvc.perform(patch("/post/"+27)
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
            mvc.perform(patch("/post/"+100)
                            .content(body) // body
                            .contentType(MediaType.APPLICATION_JSON) // 타입 명시
                            .with(authentication(authentication))
                    )
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.statusCode").value(400))
                    .andExpect(jsonPath("$.message").value("게시글 정보가 없습니다."));
        }

        @Test
        @Transactional
        void update_fail_unauthorized_user() throws Exception {
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
            mvc.perform(patch("/post/"+1)
                            .content(body) // body
                            .contentType(MediaType.APPLICATION_JSON) // 타입 명시
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
            mvc.perform(MockMvcRequestBuilders.delete("/post/"+27)
                            .contentType(MediaType.APPLICATION_JSON) // 타입 명시
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
            mvc.perform(MockMvcRequestBuilders.delete("/post/"+100)
                            .contentType(MediaType.APPLICATION_JSON)
                            .with(authentication(authentication))
                    )
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.statusCode").value(400))
                    .andExpect(jsonPath("$.message").value("게시글 정보가 없습니다."));
        }

        @Test
        @Transactional
        void delete_fail_unauthorized_user() throws Exception {

            // then
            mvc.perform(MockMvcRequestBuilders.delete("/post/"+1)
                            .contentType(MediaType.APPLICATION_JSON)
                            .with(authentication(authentication))
                    )
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.statusCode").value(400))
                    .andExpect(jsonPath("$.message").value("본인이 아닙니다."));
        }
    }

    @Nested
    @DisplayName("postList")
    class postList {
        @Test
        void postList_ok() throws Exception {
            // given
            // when
            // then
            mvc.perform(get("/post")
                            .contentType(MediaType.APPLICATION_JSON)
                            .with(authentication(authentication))
                    )
                    .andExpect(status().isOk())
                    .andDo(print());
        }

        @Test
        void post_detail_ok() throws Exception {
            // given
            // when
            // then
            mvc.perform(get("/post/" + 1)
                            .contentType(MediaType.APPLICATION_JSON)
                            .with(authentication(authentication))
                    )
                    .andExpect(status().isOk())
                    .andDo(print());
        }

        @Test
        void post_detail_fail_not_found_post() throws Exception {
            // given
            // when
            // then
            mvc.perform(get("/post/" + 100)
                            .contentType(MediaType.APPLICATION_JSON)
                            .with(authentication(authentication))
                    )
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.statusCode").value(400))
                    .andExpect(jsonPath("$.message").value("게시글 정보가 없습니다."));
        }
    }

    @Nested
    @DisplayName("search")
    class search {
        @Test
        void search_with_keyword_ok() throws Exception {
            // given
            String keyword = "2";
            // when
            // then
            mvc.perform(get("/post/search?keyword="+keyword)
                            .contentType(MediaType.APPLICATION_JSON)
                            .with(authentication(authentication))
                    )
                    .andExpect(status().isOk())
                    .andDo(print());
        }

        @Test
        void search_with_keyword_fail_not_found_post() throws Exception {
            // given
            String keyword = "실패";
            // when
            // then
            mvc.perform(get("/post/search?keyword="+keyword)
                            .contentType(MediaType.APPLICATION_JSON)
                            .with(authentication(authentication))
                    )
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.statusCode").value(400))
                    .andExpect(jsonPath("$.message").value("게시글 정보가 없습니다."));
        }

        @Test
        void search_with_username_ok() throws Exception {
            // given
            String username = "admin";
            // when
            // then
            mvc.perform(get("/post/search/user?username="+username)
                            .contentType(MediaType.APPLICATION_JSON)
                            .with(authentication(authentication))
                    )
                    .andExpect(status().isOk())
                    .andDo(print());
        }


        @Test
        void search_with_username_fail_not_found_post() throws Exception {
            // given
            String username = "newuser";
            // when
            // then
            mvc.perform(get("/post/search/user?username="+username)
                            .contentType(MediaType.APPLICATION_JSON)
                            .with(authentication(authentication))
                    )
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.statusCode").value(400))
                    .andExpect(jsonPath("$.message").value("게시글 정보가 없습니다."));
        }
    }

    @Nested
    @DisplayName("multiMedia")
    class multiMedia {
        @Test
        void multiMedia_list_ok() throws Exception {
            // given
            // when
            // then
            mvc.perform(get("/post/" + 1 +"/file")
                            .contentType(MediaType.APPLICATION_JSON)
                            .with(authentication(authentication))
                    )
                    .andExpect(status().isOk())
                    .andDo(print());
        }

        @Test
        void multiMedia_list_fail_not_found_post() throws Exception {
            // given
            // when
            // then
            mvc.perform(get("/post/" + 100 +"/file")
                            .contentType(MediaType.APPLICATION_JSON)
                            .with(authentication(authentication))
                    )
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.statusCode").value(400))
                    .andExpect(jsonPath("$.message").value("게시글 정보가 없습니다."));
        }

        @Test
        void multiMedia_list_fail_not_found_multimedia() throws Exception {
            // given
            // when
            // then
            mvc.perform(get("/post/" + 2 +"/file")
                            .contentType(MediaType.APPLICATION_JSON)
                            .with(authentication(authentication))
                    )
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.statusCode").value(400))
                    .andExpect(jsonPath("$.message").value("파일을 찾을 수 없습니다."));
        }

        @Test
        @Transactional
        void delete_ok() throws Exception {
            UserDetails userDetails = new UserDetailsImpl(2L, "admin@gmail.com", "awsedr12!", "admin", Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN")));
            authentication = new TestingAuthenticationToken(userDetails, null, userDetails.getAuthorities());

            // then
            mvc.perform(MockMvcRequestBuilders.delete("/post/"+1+"/file")
                            .contentType(MediaType.APPLICATION_JSON) // 타입 명시
                            .with(authentication(authentication))
                    )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.statusCode").value(200))
                    .andExpect(jsonPath("$.message").value("멀티미디어 삭제 완료"));
        }

        @Test
        @Transactional
        void delete_fail_not_found_multimedia() throws Exception {
            UserDetails userDetails = new UserDetailsImpl(2L, "admin@gmail.com", "awsedr12!", "admin", Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN")));
            authentication = new TestingAuthenticationToken(userDetails, null, userDetails.getAuthorities());

            // then
            mvc.perform(MockMvcRequestBuilders.delete("/post/"+100+"/file")
                            .contentType(MediaType.APPLICATION_JSON) // 타입 명시
                            .with(authentication(authentication))
                    )
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.statusCode").value(400))
                    .andExpect(jsonPath("$.message").value("파일을 찾을 수 없습니다."));
        }

        @Test
        @Transactional
        void delete_fail_unauthorized_user() throws Exception {

            // then
            mvc.perform(MockMvcRequestBuilders.delete("/post/"+1+"/file")
                            .contentType(MediaType.APPLICATION_JSON) // 타입 명시
                            .with(authentication(authentication))
                    )
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.statusCode").value(400))
                    .andExpect(jsonPath("$.message").value("본인이 아닙니다."));
        }
    }



}
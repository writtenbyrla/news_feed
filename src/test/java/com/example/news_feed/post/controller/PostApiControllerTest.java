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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestPropertySource("classpath:application-test.yml")
@ActiveProfiles("test")
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
                            .content(body)
                            .contentType(MediaType.APPLICATION_JSON)
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
                            .content(body)
                            .contentType(MediaType.APPLICATION_JSON)
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
                            .content(body)
                            .contentType(MediaType.APPLICATION_JSON)
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
            mvc.perform(patch("/post/"+1)
                            .content(body)
                            .contentType(MediaType.APPLICATION_JSON)
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
            mvc.perform(patch("/post/"+1)
                            .content(body)
                            .contentType(MediaType.APPLICATION_JSON)
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
            mvc.perform(patch("/post/"+1)
                            .content(body)
                            .contentType(MediaType.APPLICATION_JSON)
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
                            .content(body)
                            .contentType(MediaType.APPLICATION_JSON)
                            .with(authentication(authentication))
                    )
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.statusCode").value(404))
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
            mvc.perform(patch("/post/"+3)
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
            mvc.perform(MockMvcRequestBuilders.delete("/post/"+1)
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
            mvc.perform(MockMvcRequestBuilders.delete("/post/"+100)
                            .contentType(MediaType.APPLICATION_JSON)
                            .with(authentication(authentication))
                    )
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.statusCode").value(404))
                    .andExpect(jsonPath("$.message").value("게시글 정보가 없습니다."));
        }

        @Test
        @Transactional
        void delete_fail_unauthorized_user() throws Exception {

            // then
            mvc.perform(MockMvcRequestBuilders.delete("/post/"+3)
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
        @Transactional
        void postList_ok() throws Exception {
            mvc.perform(get("/post")
                            .contentType(MediaType.APPLICATION_JSON)
                            .with(authentication(authentication))
                    )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content[0].title").value("좋아요 눌러줘"))
                    .andExpect(jsonPath("$.content[1].title").value("소셜로그인 언제하지"))
                    .andExpect(jsonPath("$.content[2].title").value("프로젝트중"))
                    .andExpect(jsonPath("$.content[3].title").value("테스트코드 작성중"))
                    .andDo(print());
        }

        @Test
        @Transactional
        void post_detail_ok() throws Exception {
            // given
            // when
            // then
            mvc.perform(get("/post/" + 1)
                            .contentType(MediaType.APPLICATION_JSON)
                            .with(authentication(authentication))
                    )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.title").value("테스트코드 작성중"))
                    .andDo(print());
        }

        @Test
        @Transactional
        void post_detail_fail_not_found_post() throws Exception {
            // given
            // when
            // then
            mvc.perform(get("/post/" + 100)
                            .contentType(MediaType.APPLICATION_JSON)
                            .with(authentication(authentication))
                    )
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.statusCode").value(404))
                    .andExpect(jsonPath("$.message").value("게시글 정보가 없습니다."));
        }
    }

    @Nested
    @DisplayName("search")
    class search {
        @Test
        @Transactional
        void search_with_keyword_ok() throws Exception {
            // given
            String keyword = "중";
            // when
            // then
            mvc.perform(get("/post/search?keyword="+keyword)
                            .contentType(MediaType.APPLICATION_JSON)
                            .with(authentication(authentication))
                    )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content[0].title").value("프로젝트중"))
                    .andExpect(jsonPath("$.content[1].title").value("테스트코드 작성중"))
                    .andDo(print());
        }

        @Test
        @Transactional
        void search_with_keyword_fail_not_found_post() throws Exception {
            // given
            String keyword = "안녕";
            // when
            // then
            mvc.perform(get("/post/search?keyword="+keyword)
                            .contentType(MediaType.APPLICATION_JSON)
                            .with(authentication(authentication))
                    )
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.statusCode").value(404))
                    .andExpect(jsonPath("$.message").value("게시글 정보가 없습니다."));
        }

        @Test
        @Transactional
        void search_with_username_ok() throws Exception {
            // given
            String username = "jieun";
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
        @Transactional
        void search_with_username_fail_not_found_post() throws Exception {
            // given
            String username = "newuser";
            // when
            // then
            mvc.perform(get("/post/search/user?username="+username)
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
        @Transactional
        void multiMedia_list_fail_not_found_post() throws Exception {
            // given
            // when
            // then
            mvc.perform(get("/post/" + 100 +"/file")
                            .contentType(MediaType.APPLICATION_JSON)
                            .with(authentication(authentication))
                    )
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.statusCode").value(404))
                    .andExpect(jsonPath("$.message").value("게시글 정보가 없습니다."));
        }

        @Test
        @Transactional
        void multiMedia_list_fail_not_found_multimedia() throws Exception {
            // given
            // when
            // then
            mvc.perform(get("/post/" + 3 +"/file")
                            .contentType(MediaType.APPLICATION_JSON)
                            .with(authentication(authentication))
                    )
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.statusCode").value(404))
                    .andExpect(jsonPath("$.message").value("파일을 찾을 수 없습니다."));
        }

        @Test
        @Transactional
        void delete_ok() throws Exception {
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
            // then
            mvc.perform(MockMvcRequestBuilders.delete("/post/"+100+"/file")
                            .contentType(MediaType.APPLICATION_JSON) // 타입 명시
                            .with(authentication(authentication))
                    )
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.statusCode").value(404))
                    .andExpect(jsonPath("$.message").value("파일을 찾을 수 없습니다."));
        }

        @Test
        @Transactional
        void delete_fail_unauthorized_user() throws Exception {
            UserDetailsImpl userDetails = new UserDetailsImpl(4L, "newuser@gmail.com", "awsedr12!", "newuser", Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));
            authentication = new TestingAuthenticationToken(userDetails, null, userDetails.getAuthorities());

            // then
            mvc.perform(MockMvcRequestBuilders.delete("/post/"+2+"/file")
                            .contentType(MediaType.APPLICATION_JSON) // 타입 명시
                            .with(authentication(authentication))
                    )
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.statusCode").value(400))
                    .andExpect(jsonPath("$.message").value("본인이 아닙니다."));
        }
    }



}
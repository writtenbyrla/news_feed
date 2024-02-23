package com.example.news_feed.user.controller;

import com.example.news_feed.auth.security.UserDetailsImpl;
import com.example.news_feed.user.dto.request.PwdUpdateDto;
import com.example.news_feed.user.dto.request.UserUpdateDto;
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

import java.util.Arrays;
import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
@TestPropertySource("classpath:application-test.yml")
@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
class MypageApiControllerTest {

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
    @DisplayName("myInfo")
    class myInfo{
        @Test
        @Transactional
        void myInfo_ok() throws Exception {
            // given
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            Long userId = ((UserDetailsImpl) userDetails).getId();

            // when

            // then
            mvc.perform(get("/myPage/"+userId+"/info")
                            .contentType(MediaType.APPLICATION_JSON)
                            .with(authentication(authentication))
                    )
                    .andExpect(status().isOk())
                    .andDo(print());
        }

        @Test
        @Transactional
        void myInfo_fail_unauthorized_user() throws Exception {
            // then
            mvc.perform(get("/myPage/"+2+"/info")
                            .contentType(MediaType.APPLICATION_JSON)
                            .with(authentication(authentication))
                    )
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.statusCode").value(400))
                    .andExpect(jsonPath("$.message").value("본인이 아닙니다."));
        }
    }


    @Nested
    @DisplayName("updateProfileText")
    class updateProfileText{
        @Test
        @Transactional
        void update_ok() throws Exception {
            // given
            String description = "hi";
            String username = "jidong2";
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            Long userId = ((UserDetailsImpl) userDetails).getId();

            // when(객체 변환)
            String body = mapper.writeValueAsString(
                    UserUpdateDto.builder()
                            .username(username)
                            .description(description)
                            .build()
            );

            // then
            mvc.perform(patch("/myPage/"+userId+"/profile")
                            .content(body) // body
                            .contentType(MediaType.APPLICATION_JSON) // 타입 명시
                            .with(authentication(authentication))
                    )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.statusCode").value(200))
                    .andExpect(jsonPath("$.message").value("프로필 수정 완료"));
        }

        @Test
        @Transactional
        void update_ok_username_only() throws Exception {
            // given
            String username = "jidong2";
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            Long userId = ((UserDetailsImpl) userDetails).getId();

            // when(객체 변환)
            String body = mapper.writeValueAsString(
                    UserUpdateDto.builder()
                            .username(username)
                            .build()
            );

            // then
            mvc.perform(patch("/myPage/"+userId+"/profile")
                            .content(body) // body
                            .contentType(MediaType.APPLICATION_JSON) // 타입 명시
                            .with(authentication(authentication))
                    )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.statusCode").value(200))
                    .andExpect(jsonPath("$.message").value("프로필 수정 완료"));
        }

        @Test
        @Transactional
        void update_fail_invalid_username() throws Exception {
            // given
            String username = "hi";
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            Long userId = ((UserDetailsImpl) userDetails).getId();

            // when(객체 변환)
            String body = mapper.writeValueAsString(
                    UserUpdateDto.builder()
                            .username(username)
                            .build()
            );

            // then
            mvc.perform(patch("/myPage/"+userId+"/profile")
                            .content(body) // body
                            .contentType(MediaType.APPLICATION_JSON) // 타입 명시
                            .with(authentication(authentication))
                    )
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.statusCode").value(400))
                    .andExpect(jsonPath("$.message").value("[이름은 한글, 알파벳 소문자, 숫자를 포함하여 4~10자여야 합니다.]"));
        }
    }

    @Nested
    @DisplayName("updateProfileImg")
    class updateProfileImg{
        @Test
        void show_profileImg_ok() throws Exception {
            // given
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            Long userId = ((UserDetailsImpl) userDetails).getId();

            // when

            // then
            mvc.perform(get("/myPage/"+userId+"/profileImg")
                            .contentType(MediaType.APPLICATION_JSON)
                            .with(authentication(authentication))
                    )
                    .andExpect(status().isOk())
                    .andDo(print());
        }

        @Test
        void show_profileImg_fail_unauthorized_user() throws Exception {
            // given
            // when
            // then
            mvc.perform(get("/myPage/"+10+"/profileImg")
                            .contentType(MediaType.APPLICATION_JSON)
                            .with(authentication(authentication))
                    )
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.statusCode").value(400))
                    .andExpect(jsonPath("$.message").value("본인이 아닙니다."));
        }
    }

    @Nested
    @DisplayName("updatePwd")
    class updatePwd{

        @Test
        @Transactional
        void updatePwd_ok() throws Exception {
            // given
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            Long userId = ((UserDetailsImpl) userDetails).getId();
            String oldPwd = userDetails.getPassword();
            String newPwd = "newpwd123!";


            // when(객체 변환)
            String body = mapper.writeValueAsString(
                    PwdUpdateDto.builder()
                            .oldPwd(oldPwd)
                            .newPwd(newPwd)
                            .build()
            );

            // then
            mvc.perform(patch("/myPage/"+userId+"/pwd")
                            .content(body) // body
                            .contentType(MediaType.APPLICATION_JSON) // 타입 명시
                            .with(authentication(authentication))
                    )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.statusCode").value(200))
                    .andExpect(jsonPath("$.message").value("비밀번호 수정 완료"));
        }

        @Test
        @Transactional
        void updatePwd_fail_invalid_pwd() throws Exception {
            // given
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            Long userId = ((UserDetailsImpl) userDetails).getId();
            String oldPwd = userDetails.getPassword();
            String newPwd = "awsedr12";


            // when(객체 변환)
            String body = mapper.writeValueAsString(
                    PwdUpdateDto.builder()
                            .oldPwd(oldPwd)
                            .newPwd(newPwd)
                            .build()
            );

            // then
            mvc.perform(patch("/myPage/"+userId+"/pwd")
                            .content(body) // body
                            .contentType(MediaType.APPLICATION_JSON) // 타입 명시
                            .with(authentication(authentication))
                    )
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.statusCode").value(400))
                    .andExpect(jsonPath("$.message").value("[비밀번호는 알파벳 대소문자, 숫자, 특수문자를 포함하여 8~15자여야 합니다.]"));
        }

        @Test
        @Transactional
        void updatePwd_not_found_user() throws Exception {
            // given
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String oldPwd = userDetails.getPassword();
            String newPwd = "awsedr12!";


            // when(객체 변환)
            String body = mapper.writeValueAsString(
                    PwdUpdateDto.builder()
                            .oldPwd(oldPwd)
                            .newPwd(newPwd)
                            .build()
            );

            // then
            mvc.perform(patch("/myPage/"+10+"/pwd")
                            .content(body) // body
                            .contentType(MediaType.APPLICATION_JSON) // 타입 명시
                            .with(authentication(authentication))
                    )
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.statusCode").value(404))
                    .andExpect(jsonPath("$.message").value("등록된 사용자가 없습니다."));
        }

        @Test
        @Transactional
        void updatePwd_missMatch_pwd() throws Exception {
            // given
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            Long userId = ((UserDetailsImpl) userDetails).getId();
            String oldPwd = "abcd1234!";
            String newPwd = "awsedr12!";


            // when(객체 변환)
            String body = mapper.writeValueAsString(
                    PwdUpdateDto.builder()
                            .oldPwd(oldPwd)
                            .newPwd(newPwd)
                            .build()
            );

            // then
            mvc.perform(patch("/myPage/"+userId+"/pwd")
                            .content(body) // body
                            .contentType(MediaType.APPLICATION_JSON) // 타입 명시
                            .with(authentication(authentication))
                    )
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.statusCode").value(400))
                    .andExpect(jsonPath("$.message").value("비밀번호가 일치하지 않습니다."));
        }

        @Test
        @Transactional
        void updatePwd_recently_used_pwd() throws Exception {
            // given
            // 1차 비밀번호 변경
            updatePwd_ok();

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            Long userId = ((UserDetailsImpl) userDetails).getId();
            String oldPwd = "newpwd123!";
            String newPwd = "awsedr12!";

            // when(객체 변환)
            String body = mapper.writeValueAsString(
                    PwdUpdateDto.builder()
                            .oldPwd(oldPwd)
                            .newPwd(newPwd)
                            .build()
            );

            // then
            mvc.perform(patch("/myPage/"+userId+"/pwd")
                            .content(body) // body
                            .contentType(MediaType.APPLICATION_JSON) // 타입 명시
                            .with(authentication(authentication))
                    )
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.statusCode").value(400))
                    .andExpect(jsonPath("$.message").value("최근에 사용한 비밀번호입니다."));
        }
    }

    @Nested
    @DisplayName("myActivity")
    class myActivity{
        @Test
        @Transactional
        void show_my_posts() throws Exception {
            // given
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            Long userId = ((UserDetailsImpl) userDetails).getId();

            // when

            // then
            mvc.perform(get("/myPage/"+userId+"/posts")
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
        void show_my_posts_fail_not_found() throws Exception {
            UserDetailsImpl userDetails = new UserDetailsImpl(4L, "newuser@gmail.com", "awsedr12!", "newuser", Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));
            authentication = new TestingAuthenticationToken(userDetails, null, userDetails.getAuthorities());

            // given
            Long userId = userDetails.getId();

            // when

            // then
            mvc.perform(get("/myPage/"+userId+"/posts")
                            .contentType(MediaType.APPLICATION_JSON)
                            .with(authentication(authentication))
                    )
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.statusCode").value(404))
                    .andExpect(jsonPath("$.message").value("게시글 정보가 없습니다."));
        }


        @Test
        @Transactional
        void show_my_comments() throws Exception {
            // given
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            Long userId = ((UserDetailsImpl) userDetails).getId();

            // when

            // then
            mvc.perform(get("/myPage/"+userId+"/comments")
                            .contentType(MediaType.APPLICATION_JSON)
                            .with(authentication(authentication))
                    )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content[0].content").value("빨리 해야겠다"))
                    .andExpect(jsonPath("$.content[1].content").value("나도 아직 못함"))
                    .andDo(print());
        }

        @Test
        @Transactional
        void show_my_comments_fail_not_found() throws Exception {
            UserDetailsImpl userDetails = new UserDetailsImpl(4L, "newuser@gmail.com", "awsedr12!", "newuser", Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));
            authentication = new TestingAuthenticationToken(userDetails, null, userDetails.getAuthorities());

            // given
            Long userId = userDetails.getId();

            // when

            // then
            mvc.perform(get("/myPage/"+userId+"/comments")
                            .contentType(MediaType.APPLICATION_JSON)
                            .with(authentication(authentication))
                    )
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.statusCode").value(404))
                    .andExpect(jsonPath("$.message").value("댓글 정보가 없습니다."));
        }

    }

    @Nested
    @DisplayName("myFollow")
    class myFollow{
        @Test
        void showMyFollowings() throws Exception {
            // given
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            Long userId = ((UserDetailsImpl) userDetails).getId();

            // when

            // then
            mvc.perform(get("/myPage/"+userId+"/followings")
                            .contentType(MediaType.APPLICATION_JSON)
                            .with(authentication(authentication))
                    )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content[0].username").value("jieun"))
                    .andExpect(jsonPath("$.content[1].username").value("newuser"))
                    .andDo(print());
        }

        @Test
        void showMyFollowings_fail_not_found() throws Exception {
            // given
            UserDetailsImpl userDetails = new UserDetailsImpl(5L, "user@gmail.com", "newsfeed12!", "user", Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN")));
            authentication = new TestingAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            Long userId = userDetails.getId();

            // when

            // then
            mvc.perform(get("/myPage/"+userId+"/followings")
                            .contentType(MediaType.APPLICATION_JSON)
                            .with(authentication(authentication))
                    )
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.statusCode").value(404))
                    .andExpect(jsonPath("$.message").value("팔로우 정보가 없습니다."));
        }


        @Test
        void showMyFollowers() throws Exception {
            // given
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            Long userId = ((UserDetailsImpl) userDetails).getId();

            // when

            // then
            mvc.perform(get("/myPage/"+userId+"/followers")
                            .contentType(MediaType.APPLICATION_JSON)
                            .with(authentication(authentication))
                    )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content[0].username").value("jieun"))
                    .andDo(print());
        }

        @Test
        void showMyFollowers_fail_not_found() throws Exception {
            // given
            UserDetailsImpl userDetails = new UserDetailsImpl(5L, "user@gmail.com", "newsfeed12!", "user", Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN")));
            authentication = new TestingAuthenticationToken(userDetails, null, userDetails.getAuthorities());

            // given
            Long userId = userDetails.getId();

            // when

            // then
            mvc.perform(get("/myPage/"+userId+"/followers")
                            .contentType(MediaType.APPLICATION_JSON)
                            .with(authentication(authentication))
                    )
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.statusCode").value(404))
                    .andExpect(jsonPath("$.message").value("팔로우 정보가 없습니다."));
        }
    }
}
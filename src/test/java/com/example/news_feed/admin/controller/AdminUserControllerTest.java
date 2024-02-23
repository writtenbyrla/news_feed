package com.example.news_feed.admin.controller;

import com.example.news_feed.auth.security.UserDetailsImpl;
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

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestPropertySource("classpath:application-test.yml")
@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
class AdminUserControllerTest {
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
    @DisplayName("UserList")
    class UserList {
        @Test
        @Transactional
        void all_user_list_ok() throws Exception {
            mvc.perform(get("/admin/user")
                            .contentType(MediaType.APPLICATION_JSON)
                            .with(authentication(authentication))
                    )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content[0].username").value("user"))
                    .andExpect(jsonPath("$.content[1].username").value("newuser"))
                    .andExpect(jsonPath("$.content[2].username").value("deleted"))
                    .andExpect(jsonPath("$.content[3].username").value("jieun"))
                    .andExpect(jsonPath("$.content[4].username").value("jidong"))
                    .andDo(print());
        }
    }

    @Nested
    @DisplayName("updateRole")
    class updateRole {
        @Test
        @Transactional
        void update_to_admin_ok() throws Exception {
            mvc.perform(patch("/admin/user/"+1+"/role")
                            .param("role","ADMIN")
                            .contentType(MediaType.APPLICATION_JSON)
                            .with(authentication(authentication))
                    )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.statusCode").value(200))
                    .andExpect(jsonPath("$.message").value("유저 권한이 관리자로 변경되었습니다."));
        }

        @Test
        @Transactional
        void update_to_admin_fail_already_admin() throws Exception {
            mvc.perform(patch("/admin/user/"+5+"/role")
                            .param("role","ADMIN")
                            .contentType(MediaType.APPLICATION_JSON)
                            .with(authentication(authentication))
                    )
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.statusCode").value(400))
                    .andExpect(jsonPath("$.message").value("이미 해당 권한을 가지고 있습니다."));
        }

        @Test
        @Transactional
        void update_to_user_ok() throws Exception {
            mvc.perform(patch("/admin/user/"+5+"/role")
                            .param("role","USER")
                            .contentType(MediaType.APPLICATION_JSON)
                            .with(authentication(authentication))
                    )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.statusCode").value(200))
                    .andExpect(jsonPath("$.message").value("유저 권한이 사용자로 변경되었습니다."));
        }

        @Test
        @Transactional
        void update_to_admin_fail_already_user() throws Exception {
            mvc.perform(patch("/admin/user/"+1+"/role")
                            .param("role","USER")
                            .contentType(MediaType.APPLICATION_JSON)
                            .with(authentication(authentication))
                    )
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.statusCode").value(400))
                    .andExpect(jsonPath("$.message").value("이미 해당 권한을 가지고 있습니다."));
        }
    }

    @Nested
    @DisplayName("updateStatus")
    class updateStats{
        @Test
        @Transactional
        void update_to_N_ok() throws Exception {
            mvc.perform(patch("/admin/user/"+1+"/status")
                            .param("status","N")
                            .contentType(MediaType.APPLICATION_JSON)
                            .with(authentication(authentication))
                    )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.statusCode").value(200))
                    .andExpect(jsonPath("$.message").value("유저 강제탈퇴 성공!"));
        }

        @Test
        @Transactional
        void update_to_N_fail_already_N() throws Exception {
            mvc.perform(patch("/admin/user/"+3+"/status")
                            .param("status","N")
                            .contentType(MediaType.APPLICATION_JSON)
                            .with(authentication(authentication))
                    )
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.statusCode").value(400))
                    .andExpect(jsonPath("$.message").value("변경할 상태가 없습니다."));
        }

        @Test
        @Transactional
        void update_to_Y_ok() throws Exception {
            mvc.perform(patch("/admin/user/"+3+"/status")
                            .param("status","Y")
                            .contentType(MediaType.APPLICATION_JSON)
                            .with(authentication(authentication))
                    )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.statusCode").value(200))
                    .andExpect(jsonPath("$.message").value("유저 복구 성공!"));
        }

        @Test
        @Transactional
        void update_to_Y_fail_already_Y() throws Exception {
            mvc.perform(patch("/admin/user/"+1+"/status")
                            .param("status","Y")
                            .contentType(MediaType.APPLICATION_JSON)
                            .with(authentication(authentication))
                    )
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.statusCode").value(400))
                    .andExpect(jsonPath("$.message").value("변경할 상태가 없습니다."));
        }
    }

}
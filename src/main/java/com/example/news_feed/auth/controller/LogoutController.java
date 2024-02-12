package com.example.news_feed.auth.controller;

import com.example.news_feed.auth.dto.response.LogoutResponseDto;
import com.example.news_feed.auth.redis.service.LogoutService;
import com.example.news_feed.auth.security.UserDetailsImpl;
import com.example.news_feed.user.dto.response.UserResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping
public class LogoutController {
    private final LogoutService logoutService;

    @PostMapping("/user/logout")
    public ResponseEntity<LogoutResponseDto> logout(HttpServletRequest request,
                                                    @AuthenticationPrincipal UserDetailsImpl userdetails){
        logoutService.logout(request, userdetails.getEmail());
        LogoutResponseDto response = LogoutResponseDto.res(HttpStatus.OK.value(), "로그아웃 완료!");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}

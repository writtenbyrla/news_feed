package com.example.news_feed.auth.controller;

import com.example.news_feed.auth.dto.RefreshTokenDto;
import com.example.news_feed.auth.security.jwt.JwtTokenProvider;
import com.example.news_feed.auth.service.RefreshTokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
@RequiredArgsConstructor
@Slf4j
public class RefreshTokenController {

    private final RefreshTokenService refreshTokenService;
    private final JwtTokenProvider jwtTokenProvider;

    // accessToken 재발급
    @PostMapping("/auth/refreshToken")
    public ResponseEntity<RefreshTokenDto> refresh(HttpServletRequest request, HttpServletResponse response){
        RefreshTokenDto accessTokenDto = refreshTokenService.refresh(request);
        String accessToken = accessTokenDto.getAccessToken();
        jwtTokenProvider.accessTokenSetHeader(accessToken, response);
        return ResponseEntity.status(HttpStatus.OK).body(accessTokenDto);
    }
}

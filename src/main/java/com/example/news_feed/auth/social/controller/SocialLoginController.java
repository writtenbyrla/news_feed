package com.example.news_feed.auth.social.controller;

import com.example.news_feed.auth.redis.service.RedisService;
import com.example.news_feed.auth.security.jwt.JwtTokenProvider;
import com.example.news_feed.auth.security.jwt.TokenType;
import com.example.news_feed.auth.social.dto.SocialResponseDto;
import com.example.news_feed.auth.social.service.SocialService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import java.util.concurrent.TimeUnit;

@Controller
@RequiredArgsConstructor
@Slf4j
public class SocialLoginController {

    private final SocialService socialService;
    private final RedisService redisService;
    private final JwtTokenProvider jwtTokenProvider;
    @GetMapping("/kakao/login")
    public RedirectView kakaoLogin(@RequestParam String code, HttpServletResponse response) throws JsonProcessingException {
        SocialResponseDto dto = socialService.kakaoLogin(code, response);
        String accessToken = dto.getAccessToken();
        String refreshToken = dto.getRefreshToken();



        Long expiresTime = jwtTokenProvider.getExpiredTime(refreshToken, TokenType.REFRESH);
        redisService.setValues("RefreshToken:" + dto.getEmail(), refreshToken, expiresTime, TimeUnit.MILLISECONDS);

        // 토큰을 헤더에 넣어서 클라이언트에게 전달
        jwtTokenProvider.accessTokenSetHeader(accessToken, response);
        jwtTokenProvider.refreshTokenSetHeader(refreshToken, response);


        // 페이지 이동을 위해 쿠키에도 담음
        Cookie cookie = new Cookie(JwtTokenProvider.AUTHORIZATION_HEADER, "Bearer%20"+accessToken);
        cookie.setPath("/");
        response.addCookie(cookie);

        return new RedirectView("/home");
    }


}

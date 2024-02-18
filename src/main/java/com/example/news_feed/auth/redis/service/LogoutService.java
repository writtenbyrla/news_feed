package com.example.news_feed.auth.redis.service;

import com.example.news_feed.auth.security.jwt.JwtTokenProvider;
import com.example.news_feed.auth.security.jwt.TokenType;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class LogoutService {
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisService redisService;

    @Transactional
    public void logout(HttpServletRequest request, String userEmail) {
        String accessToken = jwtTokenProvider.resolveAccessToken(request);
//        String accessToken = jwtTokenProvider.getTokenFromRequest(request);

        if(!jwtTokenProvider.isExpired(accessToken, TokenType.ACCESS)){
            throw new IllegalArgumentException("유효하지 않은 토큰입니다.");
        }

        if(redisService.keyExists(accessToken)){
            throw new IllegalArgumentException("이미 로그아웃하셨습니다.");
        }

        String email = jwtTokenProvider.getEmail(accessToken, TokenType.ACCESS);
        if (!email.equals(userEmail)){
            throw new IllegalArgumentException("이메일 정보가 일치하지 않습니다.");
        }

        String redisKey = "RefreshToken:" + email;
        if(redisService.getValue(redisKey) != null){
            redisService.deleteKey(redisKey);
        }

        Long expiration = jwtTokenProvider.getExpiredTime(accessToken, TokenType.ACCESS);
        redisService.setValues(accessToken, "", expiration, TimeUnit.MILLISECONDS);
    }
}

package com.example.news_feed.auth.service;

import com.example.news_feed.auth.dto.response.RefreshTokenDto;
import com.example.news_feed.auth.redis.service.RedisService;
import com.example.news_feed.auth.security.jwt.JwtTokenProvider;
import com.example.news_feed.auth.security.jwt.TokenType;
import com.example.news_feed.user.domain.User;
import com.example.news_feed.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final RedisService redisService;

    @Transactional
    public RefreshTokenDto refresh(HttpServletRequest request) {
        String refreshToken = jwtTokenProvider.resolveRefreshToken(request);

        if (refreshToken == null || refreshToken.isEmpty()) {
            throw new IllegalArgumentException("refreshToken이 없습니다.");
        }

        String email = jwtTokenProvider.getEmail(refreshToken, TokenType.REFRESH);

        String redisRefreshToken = redisService.getValue("RefreshToken:" + email);
        if(!redisService.checkExistsValue(redisRefreshToken)){
            throw new IllegalArgumentException("refreshToken이 유효하지 않습니다.");
        }

        if(refreshToken.equals(redisRefreshToken)){
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new IllegalArgumentException("유저 정보를 찾을 수 없습니다."));
            String accessToken = jwtTokenProvider.createToken(user.getEmail(), user.getUsername(), user.getRole(), TokenType.ACCESS);
            return new RefreshTokenDto(accessToken, refreshToken, email, user.getUsername(), user.getRole());

        } else{
            throw new IllegalArgumentException("refreshToken이 일치하지 않습니다.");
        }

    }
}

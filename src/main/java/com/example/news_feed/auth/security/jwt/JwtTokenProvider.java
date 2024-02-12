package com.example.news_feed.auth.security.jwt;

import com.example.news_feed.user.domain.UserRoleEnum;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtTokenProvider {
    // Header KEY 값
    public static final String AUTHORIZATION_HEADER = "Authorization";
    // Token 식별자
    public static final String BEARER_PREFIX = "Bearer ";
    // refreshToken 식별자
    public static final String REFRESH_HEADER = "Refresh";


    // accessToken Key
    @Value("${jwt.secret.accessSecretKey}")
    private String accessSecretKey;

    // refreshToken key
    @Value("${jwt.secret.refreshSecretKey}")
    private String refreshSecretKey;

    // accessToken 만료시간
    @Value("${jwt.secret.accessTokenExpiredTime}")
    private Long accessTokenExpiredTime;

    // refreshToken 만료 시간
    @Value("${jwt.secret.refreshTokenExpiredTime}")
    private Long refreshTokenExpiredTime;


    private Key getKey(String key) {
//        byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);
        byte[] keyBytes = Base64.getDecoder().decode(key.getBytes(StandardCharsets.UTF_8));
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // 토큰 생성
    public String createToken(String email, String username, UserRoleEnum role, TokenType type) {
        // 1. 클레임에 정보 담기
        Claims claims = Jwts.claims();
        claims.put("username", username);
        claims.put("email", email);
        claims.put("auth", role);

        String secretKey;
        Long expiredTime;

        // 2. 토큰 타입에 따른 만료시간 설정
        if(type.equals(TokenType.ACCESS)){
            secretKey = accessSecretKey;
            expiredTime = accessTokenExpiredTime;
        } else{
            secretKey = refreshSecretKey;
            expiredTime = refreshTokenExpiredTime;
        }

        // jwts.builder로 claims, 토큰생성시간, 만료시간 등 담기

        Date date = new Date();
        // Create JWT header
        Map<String, Object> header = new HashMap<>();
        header.put("typ", "JWT");
        return Jwts.builder()
                .setHeader(header)
                .setSubject(email) // 사용자 식별자값 (username vs email?)
                .setClaims(claims) // username, email, role 포함
                .setIssuedAt(date) // 발급시간
                .setExpiration(new Date(date.getTime() + expiredTime)) // 만료 시간
                .signWith(getKey(secretKey), SignatureAlgorithm.HS256) // 암호화 알고리즘
                .compact();
    };

    // 만료 여부 확인
    public boolean isExpired(String token, TokenType type) {
        Date expiredDate = extractClaims(token, type).getExpiration();
        return expiredDate.after(new Date());
    }

    // 만료 시간
    public long getExpiredTime(String token, TokenType type) {
        Date expiredDate = extractClaims(token, type).getExpiration();
        Date currentDate = new Date();
        return expiredDate.getTime() - currentDate.getTime();
    }


    // 토큰에서 사용자 정보 가져오기
    // (= getUserInfoFromToken)

    // 토큰에서 name 정보 받아오기
    public String getUsername(String token, TokenType type) {
        return extractClaims(token, type).get("username", String.class);
    }

    // 토큰에서 email 정보 받아오기
    public String getEmail(String token, TokenType type) {
        return extractClaims(token, type).get("email", String.class);
    }

    // claims 정보 추출
    private Claims extractClaims(String token, TokenType type) {

        // access 토큰
        if (type.equals(TokenType.ACCESS)) {
            return Jwts.parserBuilder().setSigningKey(getKey(accessSecretKey))
                    .build().parseClaimsJws(token).getBody();
        }
        // refresh 토큰
        return Jwts.parserBuilder().setSigningKey(getKey(refreshSecretKey))
                .build().parseClaimsJws(token).getBody();
    }

    public void accessTokenSetHeader(String accessToken, HttpServletResponse response){
        String headerValue = BEARER_PREFIX + accessToken;
        response.setHeader(AUTHORIZATION_HEADER, headerValue);
    }

    public void refreshTokenSetHeader(String refreshToken, HttpServletResponse response) {
        response.setHeader(REFRESH_HEADER, refreshToken);
    }

    //AccessToken 반환
    public String resolveAccessToken(HttpServletRequest request) {
        String header = request.getHeader(AUTHORIZATION_HEADER);
        if (header != null && header.startsWith(BEARER_PREFIX)) {
            return header.substring(BEARER_PREFIX.length()); // "Bearer " 부분을 제외한 토큰 값 반환
        }
        return null;
    }

    // RefreshToken 반환
    public String resolveRefreshToken(HttpServletRequest request) {
        String header = request.getHeader(REFRESH_HEADER);
        if (StringUtils.hasText(header)) {
            return header;
        }
        return null;
    }


}

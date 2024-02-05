package com.example.news_feed.security.jwt;

import com.example.news_feed.user.domain.UserRoleEnum;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {

    // accessToken Key
    private String accessSecretKey = "FlRpX30MqDbiAkmlfArbrmVkDD4RqISskGZmBFax5oGVxzXXWUzTR5JyskiHMIV9M10icegkpi46AdvrcX1E6CmTUBc6";

    // refreshToken key
    private String refreshSecretKey = "7Iqk7YyM66W07YOA7L2U65Sp7YG065+9U3ByaW5n6rCV7J2Y7Yqc7YSw7LWc7JuQ67mI7J6F64uI64ukLgbTPiDc6IF";

    // accessToken 만료시간
    private Long accessTokenExpiredTime = 24 * 60 * 60 * 1000L;

    // refreshToken 만료 시간
    private Long refreshTokenExpiredTime = 24 * 60 * 60 * 1000L;


    private Key getKey(String key) {
        byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);
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
        return Jwts.builder()
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
        return expiredDate.before(new Date());
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
}

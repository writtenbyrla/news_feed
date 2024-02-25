package com.example.news_feed.auth.social.service;

import com.example.news_feed.auth.security.jwt.JwtTokenProvider;
import com.example.news_feed.auth.security.jwt.TokenType;
import com.example.news_feed.auth.social.dto.SocialLoginDto;
import com.example.news_feed.auth.social.dto.SocialResponseDto;
import com.example.news_feed.user.domain.User;
import com.example.news_feed.user.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.*;
import java.net.*;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class SocialService {
    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    String clientId;

    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    String redirectUrl;

    @Value("${spring.security.oauth2.client.provider.kakao.authorization-uri}")
    String authorizationUrl;

    @Value("${spring.security.oauth2.client.provider.kakao.token-uri}")
    String tokenUrl;

    @Value("${spring.security.oauth2.client.provider.kakao.user-info-uri}")
    String userInfoUrl;

    private final UserRepository userRepository;

    private final JwtTokenProvider jwtTokenProvider;

    private final PasswordEncoder passwordEncoder;


    // 로그인
    public SocialResponseDto kakaoLogin(String code, HttpServletResponse response) throws JsonProcessingException {
        String accessToken = getKakaoAccessToken(code).getAccessToken();
        SocialLoginDto socialLoginDto = getUserInfo(accessToken);

        // 기존 회원이 아닌 경우 회원가입 먼저
        User currentUser = userRepository.findByEmail(socialLoginDto.getEmail()).orElse(null);
        if(currentUser==null) {
            currentUser = register(socialLoginDto);
        }

        // 토큰 생성
        String newAccessToken =  jwtTokenProvider.createToken(currentUser.getEmail(), currentUser.getUsername(), currentUser.getRole(), TokenType.ACCESS);
        String newRefreshToken =  jwtTokenProvider.createToken(currentUser.getEmail(), currentUser.getUsername(), currentUser.getRole(), TokenType.REFRESH);

        return new SocialResponseDto(newAccessToken, newRefreshToken);
    }

    // 회원가입
    private User register(SocialLoginDto socialLoginDto){
        String pwd = passwordEncoder.encode(UUID.randomUUID().toString());
        User user = new User(socialLoginDto.getUsername(), pwd, socialLoginDto.getEmail());
        return userRepository.save(user);
    }

    private SocialResponseDto getKakaoAccessToken(String code) throws JsonProcessingException {
        URI uri = UriComponentsBuilder
                .fromUriString("https://kauth.kakao.com")
                .path("/oauth/token")
                .encode()
                .build()
                .toUri();

        RestTemplate rt = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", clientId);
        body.add("redirect_uri", redirectUrl);
        body.add("code", code);

        RequestEntity<MultiValueMap<String, String>> requestEntity = RequestEntity
                .post(uri)
                .headers(headers)
                .body(body);

        ResponseEntity<String> response = rt.exchange(
                requestEntity,
                String.class
        );


        JsonNode jsonNode = new ObjectMapper().readTree(response.getBody());

        String access_token = jsonNode.get("access_token").asText();
        String refresh_token = jsonNode.get("refresh_token").asText();
        return new SocialResponseDto(access_token, refresh_token);
    }




    public SocialLoginDto getUserInfo(String accessToken) throws JsonProcessingException {

        // HttpHeader 오브젝트 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HttpHeader와 HttpBody를 하나의 오브젝트에 담기
        RestTemplate rt = new RestTemplate();

        RequestEntity<MultiValueMap<String, String>> requestEntity = RequestEntity
                .post(userInfoUrl)
                .headers(headers)
                .body(new LinkedMultiValueMap<>());

        ResponseEntity<String> response = rt.exchange(
                requestEntity,
                String.class
        );

        JsonNode body = new ObjectMapper().readTree(response.getBody());
        Long id = body.get("id").asLong();
        log.info(String.valueOf(id));
        String nickname = body.get("properties")
                .get("nickname").asText();
        String email = body.get("kakao_account")
                .get("email").asText();

        return new SocialLoginDto(nickname, email);
    }
}
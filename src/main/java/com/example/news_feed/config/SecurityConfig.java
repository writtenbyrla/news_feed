package com.example.news_feed.config;

import com.example.news_feed.security.filter.JwtAuthenticationFilter;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(final HttpSecurity http) throws Exception {
        // CSRF 설정
        // CSRF: 사이트 간 요청 위조, 공격자가 인증된 브라우저에 저장된 쿠키의 세션 정보를 활용하여
        // 웹 서버에 사용자가 의도하지 않은 요청을 전달하는 것
        // CSRF 설정이 되어있는 경우 html에서 CSRF 토큰 값을 넘겨주어야 요청 수신 가능
        http.csrf((csrf) -> csrf.disable());

        // 기본 설정인 Session 방식은 사용하지 않고 JWT 방식을 사용하기 위한 설정
        http.sessionManagement((sessionManagement) ->
                sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );

        // authorizeRequests: HttpSecurity 메소드, 주로 URL 패턴에 따라 특정 경로에 대한 권한을 설정
        // auth: HttpSecurity의 다른 메소드에서 사용되는 함수형 인터페이스
        http
                .authorizeRequests((auth) ->
                        auth
                                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll() //정적 리소스
                                .requestMatchers("/").permitAll()
                                .requestMatchers(HttpMethod.POST,"/user/signup").permitAll()
                                .requestMatchers(HttpMethod.POST,"/user/login").permitAll()
                                .anyRequest().authenticated()
                );

        // 필터 관리
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        // 접근 불가 페이지
//        http.exceptionHandling((exceptionHandling) ->
//                exceptionHandling
//                        // "접근 불가" 페이지 URL 설정
//                        .accessDeniedPage("/forbidden.html")
//        );

        return http.build();
    }
}

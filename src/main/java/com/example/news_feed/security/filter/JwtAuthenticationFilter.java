package com.example.news_feed.security.filter;

import com.example.news_feed.security.UserDetailsServiceImpl;
import com.example.news_feed.security.jwt.JwtTokenProvider;
import com.example.news_feed.security.jwt.TokenType;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsServiceImpl userDetailsService;
    public JwtAuthenticationFilter(UserDetailsServiceImpl userDetailsService, JwtTokenProvider jwtTokenProvider) {
        this.userDetailsService = userDetailsService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            // authorization 헤더에서 JWT 토큰 받아오기
            final String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
            final String token = parseBearerToken(authorizationHeader);

            // 토큰 유효성 검사(email, userDetails)
            String email = jwtTokenProvider.getEmail(token, TokenType.ACCESS);
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);

            // 인증 객체 생성
            AbstractAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    token,
                    userDetails.getAuthorities()
            );

            // 사용자 인증 세부 정보 생성 - request에 대한 세부정보 담음
            // 현재 사용자의 인증 객체 설정
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            filterChain.doFilter(request, response);
        }

    }

    private String parseBearerToken(final String header) {
        return header.split(" ")[1].trim();
    }

}

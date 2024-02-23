package com.example.news_feed.auth.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum AuthErrorCode {
    INVALID_TOKEN(HttpStatus.BAD_REQUEST, "유효하지 않은 토큰입니다."),
    NOT_FOUND_TOKEN(HttpStatus.NOT_FOUND, "토큰 정보가 없습니다."),
    MISMATCH_REFRESH_TOKEN(HttpStatus.BAD_REQUEST, "refreshToken이 일치하지 않습니다."),
    ALREADY_LOGOUT(HttpStatus.BAD_REQUEST, "이미 로그아웃하셨습니다."),
    INVALID_EMAIL(HttpStatus.BAD_REQUEST, "이메일 정보가 일치하지 않습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String message;
}

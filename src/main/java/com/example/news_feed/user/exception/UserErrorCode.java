package com.example.news_feed.user.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum UserErrorCode {

    DUPLICATE_USERNAME_FOUND(HttpStatus.BAD_REQUEST, "중복된 사용자가 존재합니다."),
    DUPLICATE_EMAIL_FOUND(HttpStatus.BAD_REQUEST, "중복된 이메일이 존재합니다."),
    USER_NOT_EXIST(HttpStatus.NOT_FOUND, "등록된 사용자가 없습니다."),
    USER_DELETED(HttpStatus.NOT_FOUND, "이미 탈퇴한 회원입니다."),
    MISMATCH_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),
    RECENTLY_USED_PASSWORD(HttpStatus.BAD_REQUEST, "최근에 사용한 비밀번호입니다."),
    UNAUTHORIZED_USER(HttpStatus.BAD_REQUEST, "본인이 아닙니다.");

    private final HttpStatus httpStatus;
    private final String message;
}

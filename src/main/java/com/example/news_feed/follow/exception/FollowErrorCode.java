package com.example.news_feed.follow.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum FollowErrorCode {
    IS_SELF_FOLLOW(HttpStatus.BAD_REQUEST, "자신을 팔로우할 수 없습니다."),
    ALREADY_FOLLOWING(HttpStatus.BAD_REQUEST, "이미 팔로우중입니다."),
    NOT_FOUND_FOLLOW(HttpStatus.BAD_REQUEST, "팔로우 정보가 없습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String message;
}

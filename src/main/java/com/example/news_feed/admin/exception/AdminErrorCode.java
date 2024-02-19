package com.example.news_feed.admin.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum AdminErrorCode {
    ALREADY_HAS_ROLE(HttpStatus.BAD_REQUEST, "이미 해당 권한을 가지고 있습니다."),
    ALREADY_HAS_STATUS(HttpStatus.BAD_REQUEST, "변경할 상태가 없습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String message;
}

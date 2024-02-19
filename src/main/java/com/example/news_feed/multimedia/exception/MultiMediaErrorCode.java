package com.example.news_feed.multimedia.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum MultiMediaErrorCode {
    NOT_FOUND_FILE(HttpStatus.BAD_REQUEST, "파일을 찾을 수 없습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}

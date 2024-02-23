package com.example.news_feed.post.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
@Getter
@AllArgsConstructor
public enum PostErrorCode {

    POST_NOT_EXIST(HttpStatus.NOT_FOUND, "게시글 정보가 없습니다."),
    POSTLIKE_NOT_EXIST(HttpStatus.NOT_FOUND, "게시글 좋아요 정보가 없습니다."),
    IS_SELF_LIKE(HttpStatus.BAD_REQUEST, "본인의 글에는 좋아요를 할 수 없습니다."),
    ALREADY_LIKE(HttpStatus.BAD_REQUEST, "이미 이 게시글을 좋아합니다."),
    NOT_NULL_TITLE(HttpStatus.BAD_REQUEST, "제목을 입력해주세요."),
    NOT_NULL_CONTENT(HttpStatus.BAD_REQUEST, "내용을 입력해주세요.");

    private final HttpStatus httpStatus;
    private final String message;
}

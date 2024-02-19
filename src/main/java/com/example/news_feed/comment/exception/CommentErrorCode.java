package com.example.news_feed.comment.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum CommentErrorCode {
    NOT_FOUND_COMMENT(HttpStatus.BAD_REQUEST, "댓글 정보가 없습니다."),
    NOT_FOUND_COMMENT_LIKE(HttpStatus.BAD_REQUEST, "댓글 좋아요 정보가 없습니다."),
    IS_SELF_LIKE(HttpStatus.BAD_REQUEST, "본인의 댓글에는 좋아요를 할 수 없습니다."),
    ALREADY_LIKE(HttpStatus.BAD_REQUEST, "이미 이 댓글을 좋아합니다."),
    ;

    private final HttpStatus httpStatus;
    private final String message;
}

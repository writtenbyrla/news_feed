package com.example.news_feed.comment.exception;

import com.example.news_feed.common.exception.HttpException;
import org.springframework.http.HttpStatus;

public class CommentException extends HttpException {
    public CommentException(String message, HttpStatus httpStatus) {
        super(message, httpStatus);
    }

    public CommentException(CommentErrorCode commentErrorCode){
        super(commentErrorCode.getMessage(), commentErrorCode.getHttpStatus());
    }
}

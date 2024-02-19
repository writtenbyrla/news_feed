package com.example.news_feed.post.exception;

import com.example.news_feed.common.exception.HttpException;
import org.springframework.http.HttpStatus;

public class PostException extends HttpException {

    public PostException(String message, HttpStatus httpStatus) {
        super(message, httpStatus);
    }

    public PostException(PostErrorCode postErrorCode){
        super(postErrorCode.getMessage(), postErrorCode.getHttpStatus());
    }
}

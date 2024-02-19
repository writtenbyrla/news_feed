package com.example.news_feed.follow.exception;

import com.example.news_feed.common.exception.HttpException;
import org.springframework.http.HttpStatus;


public class FollowException extends HttpException {

    public FollowException(String message, HttpStatus httpStatus) {
        super(message, httpStatus);
    }

    public FollowException(FollowErrorCode followErrorCode){
        super(followErrorCode.getMessage(), followErrorCode.getHttpStatus());
    }
}

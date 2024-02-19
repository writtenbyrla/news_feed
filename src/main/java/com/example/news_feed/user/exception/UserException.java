package com.example.news_feed.user.exception;

import com.example.news_feed.common.exception.HttpException;
import org.springframework.http.HttpStatus;


public class UserException extends HttpException {

    public UserException(String message, HttpStatus httpStatus) {
        super(message, httpStatus);
    }

    public UserException(UserErrorCode userErrorCode) {
        super(userErrorCode.getMessage(), userErrorCode.getHttpStatus());
    }
}

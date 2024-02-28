package com.example.news_feed.user.exception;

import com.example.news_feed.common.exception.HttpException;

public class UserException extends HttpException {

    public UserException(UserErrorCode userErrorCode) {
        super(userErrorCode.getMessage(), userErrorCode.getHttpStatus());
    }
}

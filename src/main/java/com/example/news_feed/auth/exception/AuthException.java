package com.example.news_feed.auth.exception;
import com.example.news_feed.common.exception.HttpException;

public class AuthException extends HttpException  {

    public AuthException(AuthErrorCode authErrorCode){
        super(authErrorCode.getMessage(), authErrorCode.getHttpStatus());
    }
}

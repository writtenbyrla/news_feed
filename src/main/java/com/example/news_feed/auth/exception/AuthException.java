package com.example.news_feed.auth.exception;
import com.example.news_feed.common.exception.HttpException;
import org.springframework.http.HttpStatus;

public class AuthException extends HttpException  {

    public AuthException(String message, HttpStatus httpStatus) {
        super(message, httpStatus);
    }

    public AuthException(AuthErrorCode authErrorCode){
        super(authErrorCode.getMessage(), authErrorCode.getHttpStatus());
    }
}

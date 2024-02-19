package com.example.news_feed.admin.exception;

import com.example.news_feed.common.exception.HttpException;
import org.springframework.http.HttpStatus;

public class AdminException extends HttpException {
    public AdminException(String message, HttpStatus httpStatus) {
        super(message, httpStatus);
    }

    public AdminException(AdminErrorCode adminErrorCode){
        super(adminErrorCode.getMessage(), adminErrorCode.getHttpStatus());
    }
}

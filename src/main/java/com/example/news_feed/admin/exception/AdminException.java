package com.example.news_feed.admin.exception;

import com.example.news_feed.common.exception.HttpException;

public class AdminException extends HttpException {

    public AdminException(AdminErrorCode adminErrorCode){
        super(adminErrorCode.getMessage(), adminErrorCode.getHttpStatus());
    }
}

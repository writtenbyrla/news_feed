package com.example.news_feed.follow.exception;

import com.example.news_feed.common.exception.HttpException;


public class FollowException extends HttpException {

    public FollowException(FollowErrorCode followErrorCode){
        super(followErrorCode.getMessage(), followErrorCode.getHttpStatus());
    }
}

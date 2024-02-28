package com.example.news_feed.post.exception;

import com.example.news_feed.common.exception.HttpException;

public class PostException extends HttpException {

    public PostException(PostErrorCode postErrorCode){
        super(postErrorCode.getMessage(), postErrorCode.getHttpStatus());
    }
}

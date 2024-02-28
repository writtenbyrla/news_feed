package com.example.news_feed.comment.exception;

import com.example.news_feed.common.exception.HttpException;

public class CommentException extends HttpException {

    public CommentException(CommentErrorCode commentErrorCode){
        super(commentErrorCode.getMessage(), commentErrorCode.getHttpStatus());
    }
}

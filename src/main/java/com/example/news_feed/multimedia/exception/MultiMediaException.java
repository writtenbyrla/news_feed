package com.example.news_feed.multimedia.exception;

import com.example.news_feed.common.exception.HttpException;

public class MultiMediaException extends HttpException {

    public MultiMediaException(MultiMediaErrorCode multiMediaErrorCode){
        super(multiMediaErrorCode.getMessage(), multiMediaErrorCode.getHttpStatus());
    }
}

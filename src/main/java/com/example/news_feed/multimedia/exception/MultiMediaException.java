package com.example.news_feed.multimedia.exception;

import com.example.news_feed.common.exception.HttpException;
import org.springframework.http.HttpStatus;

public class MultiMediaException extends HttpException {

    public MultiMediaException(String message, HttpStatus httpStatus) {
        super(message, httpStatus);
    }

    public MultiMediaException(MultiMediaErrorCode multiMediaErrorCode){
        super(multiMediaErrorCode.getMessage(), multiMediaErrorCode.getHttpStatus());
    }
}

package com.example.news_feed.common.exception.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ExceptionResponse {
    private int statusCode;
    private String message;

    public ExceptionResponse(String message, int statusCode) {
        this.message = message;
        this.statusCode = statusCode;
    }

}

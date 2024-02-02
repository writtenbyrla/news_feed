package com.example.news_feed.user.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponseDto {

    private int statusCode;
    private String Message;


    public static UserResponseDto res(int statusCode, String message) {
        UserResponseDto response = new UserResponseDto();
        response.setStatusCode(statusCode);
        response.setMessage(message);
        return response;
    }
}

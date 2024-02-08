package com.example.news_feed.admin.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdateResponseDto {

    private int statusCode;
    private String Message;

    public static UserUpdateResponseDto res(int statusCode, String message) {
        UserUpdateResponseDto response = new UserUpdateResponseDto();
        response.setStatusCode(statusCode);
        response.setMessage(message);
        return response;
    }
}

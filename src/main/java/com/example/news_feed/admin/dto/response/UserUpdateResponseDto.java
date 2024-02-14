package com.example.news_feed.admin.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserUpdateResponseDto {

    private int statusCode;
    private String Message;

    public static UserUpdateResponseDto res(int statusCode, String message) {
        return new UserUpdateResponseDto(
                statusCode,
                message
        );
    }
}

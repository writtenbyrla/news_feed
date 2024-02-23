package com.example.news_feed.user.dto.response;

import com.example.news_feed.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDto {

    private int statusCode;
    private String Message;
    public static UserResponseDto res(int statusCode, String message) {
        return new UserResponseDto(
                statusCode,
                message
        );
    }
}

package com.example.news_feed.post.dto.response;

import com.example.news_feed.user.dto.response.UserResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostResponseDto {

    private int statusCode;
    private String Message;

    public static PostResponseDto res(int statusCode, String message) {
        return new PostResponseDto(
                statusCode,
                message
        );
    }
}

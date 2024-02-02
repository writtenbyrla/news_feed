package com.example.news_feed.post.dto.response;

import com.example.news_feed.user.dto.response.UserResponseDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostResponseDto {

    private int statusCode;
    private String Message;


    public static PostResponseDto res(int statusCode, String message) {
        PostResponseDto response = new PostResponseDto();
        response.setStatusCode(statusCode);
        response.setMessage(message);
        return response;
    }
}

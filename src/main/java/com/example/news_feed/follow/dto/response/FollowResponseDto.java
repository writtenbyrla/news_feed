package com.example.news_feed.follow.dto.response;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FollowResponseDto {

    private int statusCode;
    private String Message;

    public static FollowResponseDto res(int statusCode, String message) {
        FollowResponseDto response = new FollowResponseDto();
        response.setStatusCode(statusCode);
        response.setMessage(message);
        return response;
    }
}

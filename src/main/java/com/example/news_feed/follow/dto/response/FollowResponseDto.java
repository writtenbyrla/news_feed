package com.example.news_feed.follow.dto.response;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class FollowResponseDto {

    private int statusCode;
    private String Message;

    public static FollowResponseDto res(int statusCode, String message) {
        return new FollowResponseDto(
                statusCode,
                message
        );
    }
}

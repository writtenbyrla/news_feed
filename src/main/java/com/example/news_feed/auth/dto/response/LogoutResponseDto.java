package com.example.news_feed.auth.dto.response;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class LogoutResponseDto {

    private int statusCode;
    private String Message;
    public static LogoutResponseDto res(int statusCode, String message) {
        return new LogoutResponseDto(
          statusCode,
          message
        );
    }
}

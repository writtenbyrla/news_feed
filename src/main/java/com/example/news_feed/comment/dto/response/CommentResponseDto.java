package com.example.news_feed.comment.dto.response;

import com.example.news_feed.post.dto.response.PostResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CommentResponseDto {
    private int statusCode;
    private String Message;

    public static CommentResponseDto res(int statusCode, String message) {
        return new CommentResponseDto(
                statusCode,
                message
        );
    }
}

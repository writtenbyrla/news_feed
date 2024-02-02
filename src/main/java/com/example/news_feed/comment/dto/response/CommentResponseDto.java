package com.example.news_feed.comment.dto.response;

import com.example.news_feed.post.dto.response.PostResponseDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentResponseDto {
    private int statusCode;
    private String Message;

    public static CommentResponseDto res(int statusCode, String message) {
        CommentResponseDto response = new CommentResponseDto();
        response.setStatusCode(statusCode);
        response.setMessage(message);
        return response;
    }
}

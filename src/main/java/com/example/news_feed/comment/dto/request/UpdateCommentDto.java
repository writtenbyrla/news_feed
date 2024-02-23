package com.example.news_feed.comment.dto.request;

import com.example.news_feed.comment.domain.Comment;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Date;

@AllArgsConstructor
@ToString
@Getter
@Setter
@Builder
public class UpdateCommentDto {

    private Long commentId;

    private String content;

    @JsonProperty("user_id")
    private Long userId;

    @JsonProperty("post_id")
    private Long postId;


    public static UpdateCommentDto updateCommentDto(Comment comment){
        return new UpdateCommentDto(
                comment.getCommentId(),
                comment.getContent(),
                comment.getPost().getPostId(),
                comment.getUser().getUserId()
        );
    }
}

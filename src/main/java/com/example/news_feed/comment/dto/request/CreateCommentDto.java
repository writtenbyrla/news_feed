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
public class CreateCommentDto {

    private Long commentId;

    private String content;

    @JsonProperty("user_id")
    private Long userId;

    @JsonProperty("post_id")
    private Long postId;

    public static CreateCommentDto createCommentDto(Comment comment) {
        return new CreateCommentDto(
                comment.getCommentId(),
                comment.getContent(),
                comment.getPost().getPostId(),
                comment.getUser().getUserId()
        );
    }



}

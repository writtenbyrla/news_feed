package com.example.news_feed.comment.dto.request;

import com.example.news_feed.comment.domain.Comment;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@AllArgsConstructor
@ToString
@Getter
@Setter
public class UpdateCommentDto {

    private Long commentId;

    private String content;

    @JsonProperty("user_id")
    private Long userId;

    @JsonProperty("post_id")
    private Long postId;

    private Date updated_at;

    public static UpdateCommentDto updateCommentDto(Comment comment){
        return new UpdateCommentDto(
                comment.getCommentId(),
                comment.getContent(),
                comment.getPost().getPostId(),
                comment.getUser().getUserId(),
                comment.getUpdatedAt()
        );
    }
}

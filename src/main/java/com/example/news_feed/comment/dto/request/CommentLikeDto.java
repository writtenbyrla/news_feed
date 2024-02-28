package com.example.news_feed.comment.dto.request;

import com.example.news_feed.comment.domain.CommentLike;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@ToString
@Getter
@Setter
public class CommentLikeDto {

    private Long commentLikeId;

    @JsonProperty("user_id")
    private Long userId;

    @JsonProperty("comment_id")
    private Long commentId;

    public static CommentLikeDto createCommentLikeDto(CommentLike commentLike) {
        return new CommentLikeDto(
                commentLike.getCommentlikeId(),
                commentLike.getUser().getUserId(),
                commentLike.getComment().getCommentId()
        );
    }
}

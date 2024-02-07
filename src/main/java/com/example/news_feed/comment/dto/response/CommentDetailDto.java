package com.example.news_feed.comment.dto.response;

import com.example.news_feed.comment.domain.Comment;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class CommentDetailDto {

    @JsonProperty("post_id")
    private Long postId;
    private Long commentId;
    private String content;

    @JsonProperty("user_id")
    private Long userId;

    private String username;

    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;



    public static CommentDetailDto createCommentDetailDto(Comment comment) {
        return new CommentDetailDto(
                comment.getPost().getPostId(),
                comment.getCommentId(),
                comment.getContent(),
                comment.getUser().getUserId(),
                comment.getUser().getUsername(),
                comment.getCreatedAt(),
                comment.getModifiedAt()
        );
    }




}

package com.example.news_feed.post.dto.request;

import com.example.news_feed.post.domain.Post;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

@AllArgsConstructor
@ToString
@Getter
@Setter
@Builder
public class UpdatePostDto {

    private Long postId;
    private String title;
    private String content;

    @JsonProperty("user_id")
    private Long userId;

    public static UpdatePostDto updatePostDto(Post post) {
        return new UpdatePostDto(
                post.getPostId(),
                post.getTitle(),
                post.getContent(),
                post.getUser().getUserId()
        );
    }


}
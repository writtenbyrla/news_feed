package com.example.news_feed.post.dto.request;

import com.example.news_feed.post.domain.Post;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@AllArgsConstructor
@ToString
@Getter
@Setter
@Builder
public class CreatePostDto {

    private Long postId;

    private String title;
    private String content;

    @JsonProperty("user_id")
    private Long userId;

    public static CreatePostDto createPostDto(Post post) {
        return new CreatePostDto(
                post.getPostId(),
                post.getTitle(),
                post.getContent(),
                post.getUser().getUserId()
        );
    }
}

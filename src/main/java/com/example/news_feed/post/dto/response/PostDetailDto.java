package com.example.news_feed.post.dto.response;

import com.example.news_feed.post.domain.Post;
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
public class PostDetailDto {

    private Long postId;
    private String content;
    @JsonProperty("user_id")
    private Long userId;
    private String username;
    private Date created_at;
    private Date updated_at;

    public static PostDetailDto createPostDto(Post post) {
        return new PostDetailDto(
                post.getPostId(),
                post.getContent(),
                post.getUser().getUserId(),
                post.getUser().getUsername(),
                post.getCreatedAt(),
                post.getUpdatedAt()
        );
    }

}

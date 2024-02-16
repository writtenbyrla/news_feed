package com.example.news_feed.post.dto.response;

import com.example.news_feed.multimedia.domain.MultiMedia;
import com.example.news_feed.post.domain.Post;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@ToString
@Getter
@Setter
public class PostDetailDto {

    private Long postId;
    private String title;
    private String content;
    @JsonProperty("user_id")
    private Long userId;
    private String username;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAT;

    public static PostDetailDto createPostDto(Post post) {
        return new PostDetailDto(
                post.getPostId(),
                post.getTitle(),
                post.getContent(),
                post.getUser().getUserId(),
                post.getUser().getUsername(),
                post.getCreatedAt(),
                post.getModifiedAt()
        );
    }

}

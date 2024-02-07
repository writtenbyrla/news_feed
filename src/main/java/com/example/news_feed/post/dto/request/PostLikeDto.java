package com.example.news_feed.post.dto.request;

import com.example.news_feed.post.domain.PostLike;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@ToString
@Getter
@Setter
public class PostLikeDto {

    private Long postLikeId;

    @JsonProperty("post_id")
    private Long postId;

    @JsonProperty("user_id")
    private Long userId;


    public static PostLikeDto createPostLikeDto(PostLike postlike) {
        return new PostLikeDto(
                postlike.getPostlikeId(),
                postlike.getPost().getPostId(),
                postlike.getUser().getUserId()
        );
    }
}

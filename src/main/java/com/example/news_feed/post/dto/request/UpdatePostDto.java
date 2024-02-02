package com.example.news_feed.post.dto.request;

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
public class UpdatePostDto {

    private Long postId;
    private String content;

    @JsonProperty("user_id")
    private Long userId;

    private Date updated_at;

    public static UpdatePostDto updatePostDto(Post post) {
        return new UpdatePostDto(
                post.getPostId(),
                post.getContent(),
                post.getUser().getUserId(),
                post.getUpdatedAt()
        );
    }


}
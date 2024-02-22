package com.example.news_feed.post.dto.request;

import com.example.news_feed.common.exception.HttpException;
import com.example.news_feed.post.domain.Post;
import com.example.news_feed.post.exception.PostErrorCode;
import com.example.news_feed.post.exception.PostException;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.http.HttpStatus;

import java.util.Date;

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
        if(post.getTitle().isEmpty()){
            throw new PostException(PostErrorCode.NOT_NULL_TITLE);
        }
        if(post.getContent().isEmpty()){
            throw new PostException(PostErrorCode.NOT_NULL_CONTENT);
        }
        return new CreatePostDto(
                post.getPostId(),
                post.getTitle(),
                post.getContent(),
                post.getUser().getUserId()
        );
    }
}

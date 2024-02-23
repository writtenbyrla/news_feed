package com.example.news_feed.follow.dto.response;

import com.example.news_feed.post.domain.Post;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class FollowingPostDto {

    @JsonProperty("post_id")
    private Long postId;

    @JsonProperty("following_Id")
    private Long followingId; // 팔로우 당한 사람

    @JsonProperty("user_id")
    private Long userId; // 게시물을 작성한 사용자의 ID

    private String username; // 팔로우 당한 사람의 username

    private String title;
    private String content;

    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public static FollowingPostDto createFollowingPostDto(Post post){
        return new FollowingPostDto(
          post.getPostId(),
          post.getUser().getUserId(),
          post.getUser().getUserId(),
          post.getUser().getUsername(),
          post.getTitle(),
          post.getContent(),
          post.getCreatedAt(),
          post.getModifiedAt()
        );
    }
}

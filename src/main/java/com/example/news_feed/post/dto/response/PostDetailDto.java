package com.example.news_feed.post.dto.response;

import com.example.news_feed.comment.domain.Comment;
import com.example.news_feed.multimedia.domain.MultiMedia;
import com.example.news_feed.post.domain.Post;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime createdAt;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime modifiedAT;
    @JsonProperty("like")
    private int like;
    private List<String> fileUrls;
    private String profileImg;

    public static PostDetailDto createPostDto(Post post) {
        List<String> fileUrls = post.getMultiMedia().stream()
                .map(MultiMedia::getFileUrl)
                .collect(Collectors.toList());

        return new PostDetailDto(
                post.getPostId(),
                post.getTitle(),
                post.getContent(),
                post.getUser().getUserId(),
                post.getUser().getUsername(),
                post.getCreatedAt(),
                post.getModifiedAt(),
                post.getPostLike().size(),
                fileUrls,
                post.getUser().getProfileImg()
        );
    }


}

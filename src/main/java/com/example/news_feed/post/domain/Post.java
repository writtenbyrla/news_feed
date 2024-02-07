package com.example.news_feed.post.domain;

import com.example.news_feed.post.dto.request.CreatePostDto;
import com.example.news_feed.post.dto.request.UpdatePostDto;
import com.example.news_feed.timestamp.TimeStamp;
import com.example.news_feed.user.domain.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@Table(name="post")
public class Post extends TimeStamp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long postId;

    @Column(name = "content", nullable = false)
    private String content;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Post(CreatePostDto createPostDto, User user) {
        this.postId = createPostDto.getPostId();
        this.content = createPostDto.getContent();
        this.user = user;
    }

    public void patch(UpdatePostDto updatePostDto) {
        if (this.postId != updatePostDto.getPostId())
            throw new IllegalArgumentException("게시글 수정 실패! postId가 잘못 입력되었습니다.");

        if (updatePostDto.getContent() != null)
            this.content = updatePostDto.getContent();
    }

}

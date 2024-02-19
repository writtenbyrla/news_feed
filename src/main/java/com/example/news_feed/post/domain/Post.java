package com.example.news_feed.post.domain;

import com.example.news_feed.comment.domain.Comment;
import com.example.news_feed.common.exception.HttpException;
import com.example.news_feed.multimedia.domain.MultiMedia;
import com.example.news_feed.post.dto.request.CreatePostDto;
import com.example.news_feed.post.dto.request.UpdatePostDto;
import com.example.news_feed.common.timestamp.TimeStamp;
import com.example.news_feed.user.domain.User;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

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

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", nullable = false)
    private String content;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<PostLike> postLike = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<MultiMedia> multiMedia = new ArrayList<>();

    public Post(CreatePostDto createPostDto, User user) {
        this.postId = createPostDto.getPostId();
        this.title = createPostDto.getTitle();
        this.content = createPostDto.getContent();
        this.user = user;
    }

    public void patch(UpdatePostDto updatePostDto) {
        if (!this.postId.equals(updatePostDto.getPostId()))
            throw new HttpException("게시글 수정 실패! postId가 잘못 입력되었습니다.", HttpStatus.BAD_REQUEST);
        if (updatePostDto.getTitle() != null)
            this.title = updatePostDto.getTitle();
        if (updatePostDto.getContent() != null)
            this.content = updatePostDto.getContent();
    }

}

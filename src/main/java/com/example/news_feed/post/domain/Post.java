package com.example.news_feed.post.domain;

import com.example.news_feed.comment.domain.Comment;
import com.example.news_feed.multimedia.domain.MultiMedia;
import com.example.news_feed.post.dto.request.CreatePostDto;
import com.example.news_feed.post.dto.request.UpdatePostDto;
import com.example.news_feed.common.timestamp.TimeStamp;
import com.example.news_feed.user.domain.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;

import java.util.ArrayList;
import java.util.List;

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

    @BatchSize(size = 10)
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<PostLike> postLike = new ArrayList<>();

    @BatchSize(size = 10)
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<MultiMedia> multiMedia = new ArrayList<>();

    public Post(CreatePostDto createPostDto, User user) {
        this.postId = createPostDto.getPostId();
        this.title = createPostDto.getTitle();
        this.content = createPostDto.getContent();
        this.user = user;
    }

    public Post(Long id, String title, String content, User user) {
        this.postId = id;
        this.title = title;
        this.content = content;
        this.user = user;
    }

    public Post(String title, String content, User user) {
        this.title = title;
        this.content = content;
        this.user = user;
    }

    public void patch(UpdatePostDto updatePostDto) {
        if (updatePostDto.getTitle() != null)
            this.title = updatePostDto.getTitle();
        if (updatePostDto.getContent() != null)
            this.content = updatePostDto.getContent();
    }

}

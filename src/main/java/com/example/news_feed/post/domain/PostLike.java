package com.example.news_feed.post.domain;

import com.example.news_feed.user.domain.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name="postlike")
public class PostLike{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="postlike_id")
    private Long postlikeId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    public PostLike(User user, Post post) {
        this.user = user;
        this.post = post;
    }

    public static PostLike createPostLike(User user, Post post) {
        return new PostLike(
                user,
                post
        );
    }
}

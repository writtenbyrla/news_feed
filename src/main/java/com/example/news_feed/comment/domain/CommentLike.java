package com.example.news_feed.comment.domain;
import com.example.news_feed.post.domain.Post;
import com.example.news_feed.post.domain.PostLike;
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
@Table(name = "commentlike")
public class CommentLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="commentlike_id")
    private Long commentlikeId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "comment_id")
    private Comment comment;

    public static CommentLike createCommentLike(User user, Comment comment) {
        return new CommentLike(
                null,
                user,
                comment
        );
    }
}

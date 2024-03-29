package com.example.news_feed.comment.domain;

import com.example.news_feed.comment.dto.request.CreateCommentDto;
import com.example.news_feed.comment.dto.request.UpdateCommentDto;
import com.example.news_feed.comment.exception.CommentErrorCode;
import com.example.news_feed.comment.exception.CommentException;
import com.example.news_feed.post.domain.Post;
import com.example.news_feed.common.timestamp.TimeStamp;
import com.example.news_feed.user.domain.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@Table(name="comment")
public class Comment extends TimeStamp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="comment_id")
    private Long commentId;

    @Column(name = "content", nullable = false)
    private String content;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL)
    private List<CommentLike> commentLike = new ArrayList<>();

    public Comment(CreateCommentDto createCommentDto, Post post, User user) {
            this.commentId = createCommentDto.getCommentId();
            this.content = createCommentDto.getContent();
            this.post = post;
            this.user = user;
    }

    public void patch(Long commentId, UpdateCommentDto updateCommentDto) {
        if (!this.commentId.equals(commentId)) {
            throw new CommentException(CommentErrorCode.NOT_FOUND_COMMENT);
        }

        if (updateCommentDto.getContent() != null) {
            this.content = updateCommentDto.getContent();
        }
    }
}

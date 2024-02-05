package com.example.news_feed.comment.domain;

import com.example.news_feed.comment.dto.request.CreateCommentDto;
import com.example.news_feed.comment.dto.request.UpdateCommentDto;
import com.example.news_feed.comment.dto.response.CommentDetailDto;
import com.example.news_feed.post.domain.Post;
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
@Table(name="comment")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="comment_id")
    private Long commentId;

    @Column(name = "content")
    private String content;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "updated_at")
    private Date updatedAt;

    @ManyToOne
    @JoinColumn(name = "post_id", referencedColumnName = "post_id")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;

    public Comment(CreateCommentDto createCommentDto, Post post, User user, Date date) {
            this.commentId = createCommentDto.getCommentId();
            this.content = createCommentDto.getContent();
            this.post = post;
            this.user = user;
            this.createdAt = date;

    }

    public void patch(Long commentId, UpdateCommentDto updateCommentDto) {
        if (this.commentId != commentId)
            throw new IllegalArgumentException("댓글 수정 실패! 잘못된 댓글 번호가 입력되었습니다.");

        if (updateCommentDto.getContent() != null) {
            this.content = updateCommentDto.getContent();
        }
    }
}

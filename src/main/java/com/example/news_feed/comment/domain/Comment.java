package com.example.news_feed.comment.domain;

import com.example.news_feed.comment.dto.request.CreateCommentDto;
import com.example.news_feed.comment.dto.request.UpdateCommentDto;
import com.example.news_feed.common.exception.HttpException;
import com.example.news_feed.post.domain.Post;
import com.example.news_feed.common.timestamp.TimeStamp;
import com.example.news_feed.user.domain.User;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
        if (!this.commentId.equals(commentId))
            throw new HttpException(false, "댓글 수정 실패! 잘못된 댓글 번호가 입력되었습니다.", HttpStatus.BAD_REQUEST);

        if (updateCommentDto.getContent() != null) {
            this.content = updateCommentDto.getContent();
        }
    }
}

package com.example.news_feed.comment.service;

import com.example.news_feed.comment.domain.Comment;
import com.example.news_feed.comment.domain.CommentLike;
import com.example.news_feed.comment.dto.request.CommentLikeDto;
import com.example.news_feed.comment.repository.CommentLikeRepository;
import com.example.news_feed.comment.repository.CommentRepository;
import com.example.news_feed.user.domain.User;
import com.example.news_feed.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CommentLikeService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private CommentLikeRepository commentLikeRepository;

    @Transactional
    public CommentLikeDto create(Long commentId, Long userId) {
        // 사용자 정보
        User user = userRepository.findById(userId)
                .orElseThrow( () -> new IllegalArgumentException("등록된 사용자가 없습니다."));

        // 댓글 정보
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("등록된 댓글이 없습니다."));

        // 자신의 댓글인 경우 좋아요 불가능
        if(userId.equals(comment.getUser().getUserId())){
            throw new IllegalArgumentException("자신의 댓글은 좋아요를 할 수 없습니다.");
        }

        // 좋아요 정보
        CommentLike target = commentLikeRepository.findByCommentId(commentId);

        if (target != null){
            throw new IllegalArgumentException("이 댓글을 이미 좋아합니다!");
        }

        CommentLike commentLike = CommentLike.createCommentLike(user, comment);
        CommentLike created = commentLikeRepository.save(commentLike);
        return CommentLikeDto.createCommentLikeDto(created);


    }

    @Transactional
    public CommentLikeDto delete(Long commentId, Long userId) {
        // 사용자 정보
        User user = userRepository.findById(userId)
                .orElseThrow( () -> new IllegalArgumentException("등록된 사용자가 없습니다."));

        // 댓글 정보
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("등록된 댓글이 없습니다."));


        // 좋아요 정보
        CommentLike target = commentLikeRepository.findByCommentId(commentId);

        if (target == null) {
            throw new IllegalArgumentException("삭제할 좋아요가 없습니다.");
        }

        commentLikeRepository.delete(target);
        return CommentLikeDto.createCommentLikeDto(target);

    }
}

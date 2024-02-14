package com.example.news_feed.comment.service;

import com.example.news_feed.comment.domain.Comment;
import com.example.news_feed.comment.domain.CommentLike;
import com.example.news_feed.comment.dto.request.CommentLikeDto;
import com.example.news_feed.comment.repository.CommentLikeRepository;
import com.example.news_feed.comment.repository.CommentRepository;
import com.example.news_feed.common.exception.HttpException;
import com.example.news_feed.user.domain.User;
import com.example.news_feed.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentLikeService {

    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final CommentLikeRepository commentLikeRepository;

    @Transactional
    public CommentLikeDto create(Long commentId, Long userId) {
        // 사용자 정보
        User user = checkUser(userId);

        // 댓글 정보
        Comment comment = checkComment(commentId);

        // 자신의 댓글인 경우 좋아요 불가능
        if(userId.equals(comment.getUser().getUserId())){
            throw new HttpException(false, "자신의 댓글은 좋아요를 할 수 없습니다.", HttpStatus.BAD_REQUEST);
        }

        // 좋아요 정보
        commentLikeRepository.findByCommentId(commentId)
                .ifPresent(commentLike -> {
                    throw new HttpException(false, "이 댓글을 이미 좋아합니다!", HttpStatus.BAD_REQUEST);
                });

        CommentLike commentLike = CommentLike.createCommentLike(user, comment);
        CommentLike created = commentLikeRepository.save(commentLike);
        return CommentLikeDto.createCommentLikeDto(created);


    }

    @Transactional
    public CommentLikeDto delete(Long commentId, Long userId) {
        // 사용자 정보
        User user = checkUser(userId);

        // 댓글 정보
        Comment comment = checkComment(commentId);


        // 좋아요 정보
        CommentLike target = commentLikeRepository.findByCommentId(commentId)
                .orElseThrow(()->new HttpException(false, "삭제할 좋아요가 없습니다.", HttpStatus.BAD_REQUEST));

        commentLikeRepository.delete(target);
        return CommentLikeDto.createCommentLikeDto(target);

    }

    // 유저 정보 확인
    private User checkUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new HttpException(false, "유저 정보가 없습니다.", HttpStatus.BAD_REQUEST));
    }

    // 댓글 정보 확인
    private Comment checkComment(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new HttpException(false, "댓글 정보가 없습니다.", HttpStatus.BAD_REQUEST));
    }

}

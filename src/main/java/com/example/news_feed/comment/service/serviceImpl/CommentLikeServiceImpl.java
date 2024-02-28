package com.example.news_feed.comment.service.serviceImpl;

import com.example.news_feed.comment.domain.Comment;
import com.example.news_feed.comment.domain.CommentLike;
import com.example.news_feed.comment.dto.request.CommentLikeDto;
import com.example.news_feed.comment.exception.CommentErrorCode;
import com.example.news_feed.comment.exception.CommentException;
import com.example.news_feed.comment.repository.CommentLikeRepository;
import com.example.news_feed.comment.repository.CommentRepository;
import com.example.news_feed.comment.service.CommentLikeService;
import com.example.news_feed.user.domain.User;
import com.example.news_feed.user.exception.UserErrorCode;
import com.example.news_feed.user.exception.UserException;
import com.example.news_feed.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentLikeServiceImpl implements CommentLikeService {

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
            throw new CommentException(CommentErrorCode.IS_SELF_LIKE);
        }

        // 좋아요 정보
        commentLikeRepository.findByCommentId(commentId)
                .ifPresent(commentLike -> {
                    throw new CommentException(CommentErrorCode.ALREADY_LIKE);
                });

        CommentLike commentLike = CommentLike.createCommentLike(user, comment);
        CommentLike created = commentLikeRepository.save(commentLike);
        return CommentLikeDto.createCommentLikeDto(created);


    }

    @Transactional
    public CommentLikeDto delete(Long commentLikeId, Long userId) {
        // 사용자 정보
        User user = checkUser(userId);

        // 좋아요 정보
        CommentLike target = commentLikeRepository.findById(commentLikeId)
                .orElseThrow(()-> new CommentException(CommentErrorCode.NOT_FOUND_COMMENT_LIKE));

        commentLikeRepository.delete(target);
        return CommentLikeDto.createCommentLikeDto(target);

    }

    // 유저 정보 확인
    private User checkUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() ->  new UserException(UserErrorCode.USER_NOT_EXIST));
    }

    // 댓글 정보 확인
    private Comment checkComment(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentException(CommentErrorCode.NOT_FOUND_COMMENT));
    }

}

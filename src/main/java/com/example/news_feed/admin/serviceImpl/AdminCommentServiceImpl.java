package com.example.news_feed.admin.serviceImpl;

import com.example.news_feed.admin.service.AdminCommentService;
import com.example.news_feed.comment.domain.Comment;
import com.example.news_feed.comment.dto.request.UpdateCommentDto;
import com.example.news_feed.comment.dto.response.CommentDetailDto;
import com.example.news_feed.comment.repository.CommentRepository;
import com.example.news_feed.common.exception.HttpException;
import com.example.news_feed.user.domain.User;
import com.example.news_feed.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminCommentServiceImpl implements AdminCommentService {

    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public UpdateCommentDto update(Long commentId, UpdateCommentDto updateCommentDto) {

        // 사용자 정보
        User user = checkUser(updateCommentDto.getUserId());

        // 댓글 정보
        Comment target = checkComment(commentId);

        // 댓글 수정
        target.patch(commentId, updateCommentDto);

        // DB로 갱신
        Comment updated = commentRepository.save(target);

        // 댓글 엔티티를 DTO로 변환 및 반환
        return UpdateCommentDto.updateCommentDto(updated);

    }

    @Transactional
    public Comment delete(Long userId, Long commentId) {
        // 기존 유저정보 조회 및 예외 처리
        checkUser(userId);

        // 댓글 정보
        Comment target = checkComment(commentId);

        commentRepository.delete(target);
        return target;
    }

    // 댓글 전체 조회
    public List<CommentDetailDto> showAll() {
        return commentRepository.findAll()
                .stream()
                .map(CommentDetailDto::createCommentDetailDto)
                .collect(Collectors.toList());
    }

    // 유저 정보 확인
    private User checkUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new HttpException("유저 정보가 없습니다.", HttpStatus.BAD_REQUEST));
    }

    // 댓글 정보 확인
    private Comment checkComment(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new HttpException("댓글 정보가 없습니다.", HttpStatus.BAD_REQUEST));
    }

}

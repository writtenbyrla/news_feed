package com.example.news_feed.admin.service;

import com.example.news_feed.comment.domain.Comment;
import com.example.news_feed.comment.dto.request.UpdateCommentDto;
import com.example.news_feed.comment.repository.CommentRepository;
import com.example.news_feed.post.repository.PostRepository;
import com.example.news_feed.user.domain.User;
import com.example.news_feed.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class AdminCommentService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommentRepository commentRepository;


    @Transactional
    public UpdateCommentDto update(Long commentId, UpdateCommentDto updateCommentDto) {

        // 사용자 정보
        User user = userRepository.findById(updateCommentDto.getUserId())
                .orElseThrow( () -> new IllegalArgumentException("등록된 사용자가 없습니다."));


        // 댓글 정보
        Comment target = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("등록된 댓글이 없습니다."));


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
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("게시글 삭제 실패! 유저 정보가 없습니다."));

        // 댓글 정보
        Comment target = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("등록된 댓글이 없습니다."));

        commentRepository.delete(target);
        return target;
    }
}

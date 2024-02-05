package com.example.news_feed.comment.service;

import com.example.news_feed.comment.domain.Comment;
import com.example.news_feed.comment.dto.request.CreateCommentDto;
import com.example.news_feed.comment.dto.request.UpdateCommentDto;
import com.example.news_feed.comment.repository.CommentRepository;
import com.example.news_feed.post.domain.Post;
import com.example.news_feed.post.repository.PostRepository;
import com.example.news_feed.user.domain.User;
import com.example.news_feed.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@Slf4j
public class CommentService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Transactional
    public CreateCommentDto create(Long postId, CreateCommentDto createCommentDto) {

        // 사용자 정보
        User user = userRepository.findById(createCommentDto.getUserId())
                .orElseThrow( () -> new IllegalArgumentException("등록된 사용자가 없습니다."));

        // 게시글 정보
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("등록된 게시물이 없습니다."));


        // 엔티티 생성
        Comment comment = new Comment(createCommentDto, post, user, new Date());

        // 엔티티를 DB로 저장
        Comment created = commentRepository.save(comment);

        // Dto로 변경하여 반환
        return createCommentDto.createCommentDto(created);
    }
    @Transactional
    public UpdateCommentDto update(Long commentId, UpdateCommentDto updateCommentDto) {

        // 사용자 정보
        User user = userRepository.findById(updateCommentDto.getUserId())
                .orElseThrow( () -> new IllegalArgumentException("등록된 사용자가 없습니다."));


        // 댓글 정보
        Comment target = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("등록된 댓글이 없습니다."));

        // 작성자가 일치하거나 ADMIN인 경우에만 수정 가능
        if ((updateCommentDto.getUserId() != target.getUser().getUserId()) && (!user.getRole().toString().equals("ADMIN"))){
            throw new IllegalArgumentException("본인이 작성한 댓글이 아닙니다. 삭제가 불가능합니다.");
        }

        // 댓글 수정
        target.setUpdatedAt(new Date());
        target.patch(commentId, updateCommentDto);

        // DB로 갱신
        Comment updated = commentRepository.save(target);

        // 댓글 엔티티를 DTO로 변환 및 반환
        return UpdateCommentDto.updateCommentDto(updated);

    }

    public Comment delete(Long userId, Long commentId) {
        // 기존 유저정보 조회 및 예외 처리
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("게시글 삭제 실패! 유저 정보가 없습니다."));

        // 댓글 정보
        Comment target = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("등록된 댓글이 없습니다."));

        // 작성자가 일치하거나 ADMIN인 경우에만 삭제 가능
        if ((userId != target.getUser().getUserId()) && (!user.getRole().toString().equals("ADMIN"))){
            throw new IllegalArgumentException("본인이 작성한 댓글이 아닙니다. 삭제가 불가능합니다.");
        }

        commentRepository.delete(target);
        return target;
    }
}

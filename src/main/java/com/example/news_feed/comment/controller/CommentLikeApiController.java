package com.example.news_feed.comment.controller;

import com.example.news_feed.comment.dto.response.CommentResponseDto;
import com.example.news_feed.comment.service.serviceImpl.CommentLikeServiceImpl;
import com.example.news_feed.auth.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class CommentLikeApiController {

    private final CommentLikeServiceImpl commentLikeServiceImpl;

    // 좋아요 등록
    @PostMapping("/comment/{commentId}/like")
    public ResponseEntity<CommentResponseDto> create(@PathVariable Long commentId,
                                                     @AuthenticationPrincipal final UserDetailsImpl userDetails){

        Long userId = userDetails.getId();
        commentLikeServiceImpl.create(commentId, userId);
        CommentResponseDto response = CommentResponseDto.res(HttpStatus.CREATED.value(), "댓글 좋아요 +1");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 좋아요 취소
    @DeleteMapping("/comment/{commentLikeId}/like")
    public ResponseEntity<CommentResponseDto> delete(@PathVariable Long commentLikeId,
                                                     @AuthenticationPrincipal final UserDetailsImpl userDetails){

        Long userId = userDetails.getId();
        commentLikeServiceImpl.delete(commentLikeId, userId);
        CommentResponseDto response = CommentResponseDto.res(HttpStatus.OK.value(), "댓글 좋아요 취소");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}

package com.example.news_feed.comment.controller;

import com.example.news_feed.comment.dto.request.CreateCommentDto;
import com.example.news_feed.comment.dto.request.UpdateCommentDto;
import com.example.news_feed.comment.dto.response.CommentResponseDto;
import com.example.news_feed.comment.service.CommentService;
import com.example.news_feed.security.UserDetailsImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
public class CommentApiController {

    @Autowired
    private CommentService commentService;

    // 댓글 등록
    @PostMapping("/post/{postId}/comment")
    private ResponseEntity<CommentResponseDto> create(@PathVariable Long postId,
                                                      @RequestBody CreateCommentDto createCommentDto,
                                                      @AuthenticationPrincipal final UserDetailsImpl userDetails) {
        createCommentDto.setUserId(userDetails.getId()); // 임의값 -> 나중에 로그인한 정보에서 받아와야 함

        commentService.create(postId, createCommentDto);
        CommentResponseDto response = CommentResponseDto.res(HttpStatus.CREATED.value(), "댓글 등록 완료");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 댓글 수정
    @PatchMapping("/comments/{commentId}")
    private ResponseEntity<CommentResponseDto> create(@PathVariable Long commentId,
                                                      @RequestBody UpdateCommentDto updateCommentDto,
                                                      @AuthenticationPrincipal final UserDetailsImpl userDetails) {
        updateCommentDto.setUserId(userDetails.getId());
        log.info("Controller userId {}", userDetails.getId());
        commentService.update(commentId, updateCommentDto);

        CommentResponseDto response = CommentResponseDto.res(HttpStatus.OK.value(), "댓글 수정 완료");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    };

    // 댓글 삭제
    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<CommentResponseDto> delete(@PathVariable Long commentId,
                                                     @AuthenticationPrincipal final UserDetailsImpl userDetails){
        Long userId = userDetails.getId();
        commentService.delete(userId, commentId);
        CommentResponseDto response =  CommentResponseDto.res(HttpStatus.OK.value(), "댓글 삭제 완료");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // 댓글 전체 조회


    // 댓글 상세 조회
}

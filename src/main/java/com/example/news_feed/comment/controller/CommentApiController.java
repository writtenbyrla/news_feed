package com.example.news_feed.comment.controller;

import com.example.news_feed.comment.dto.request.CreateCommentDto;
import com.example.news_feed.comment.dto.request.UpdateCommentDto;
import com.example.news_feed.comment.dto.response.CommentResponseDto;
import com.example.news_feed.comment.service.CommentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
public class CommentApiController {

    @Autowired
    private CommentService commentService;

    // 댓글 등록
    @PostMapping("/post/{postId}/comment")
    private ResponseEntity<CommentResponseDto> create(@PathVariable Long postId, @RequestBody CreateCommentDto createCommentDto){
        createCommentDto.setUserId(1L); // 임의값 -> 나중에 로그인한 정보에서 받아와야 함

        commentService.create(postId, createCommentDto);
        CommentResponseDto response = CommentResponseDto.res(HttpStatus.CREATED.value(), "댓글 등록 완료");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 댓글 수정
    @PatchMapping("/comments/{commentId}")
    private ResponseEntity<CommentResponseDto> create(@PathVariable Long commentId, @RequestBody UpdateCommentDto updateCommentDto){
        updateCommentDto.setUserId(1L); // 임의값 -> 나중에 로그인한 정보에서 받아와야 함

        commentService.update(commentId, updateCommentDto);

        CommentResponseDto response = CommentResponseDto.res(HttpStatus.OK.value(), "댓글 수정 완료");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    };
}

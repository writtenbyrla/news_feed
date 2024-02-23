package com.example.news_feed.comment.controller;

import com.example.news_feed.comment.dto.request.CreateCommentDto;
import com.example.news_feed.comment.dto.request.UpdateCommentDto;
import com.example.news_feed.comment.dto.response.CommentDetailDto;
import com.example.news_feed.comment.dto.response.CommentResponseDto;
import com.example.news_feed.comment.service.serviceImpl.CommentServiceImpl;
import com.example.news_feed.auth.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
public class CommentApiController {

    private final CommentServiceImpl commentServiceImpl;

    // 댓글 등록
    @PostMapping("/post/{postId}/comment")
    private ResponseEntity<CommentResponseDto> create(@PathVariable Long postId,
                                                      @RequestBody CreateCommentDto createCommentDto,
                                                      @AuthenticationPrincipal final UserDetailsImpl userDetails) {
        createCommentDto.setUserId(userDetails.getId());

        commentServiceImpl.create(postId, createCommentDto);
        CommentResponseDto response = CommentResponseDto.res(HttpStatus.CREATED.value(), "댓글 등록 완료");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 댓글 수정
    @PatchMapping("/comment/{commentId}")
    private ResponseEntity<CommentResponseDto> create(@PathVariable Long commentId,
                                                      @RequestBody UpdateCommentDto updateCommentDto,
                                                      @AuthenticationPrincipal final UserDetailsImpl userDetails) {
        updateCommentDto.setUserId(userDetails.getId());
        commentServiceImpl.update(commentId, updateCommentDto);

        CommentResponseDto response = CommentResponseDto.res(HttpStatus.OK.value(), "댓글 수정 완료");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    };

    // 댓글 삭제
    @DeleteMapping("/comment/{commentId}")
    public ResponseEntity<CommentResponseDto> delete(@PathVariable Long commentId,
                                                     @AuthenticationPrincipal final UserDetailsImpl userDetails){
        Long userId = userDetails.getId();
        commentServiceImpl.delete(userId, commentId);
        CommentResponseDto response =  CommentResponseDto.res(HttpStatus.OK.value(), "댓글 삭제 완료");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // 댓글 목록
    @GetMapping("/post/{postId}/comment")
    public ResponseEntity<Page<CommentDetailDto>> showAll(@PathVariable Long postId,
                                                          @PageableDefault(value=10)
                                                          @SortDefault(sort = "created_at", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<CommentDetailDto> comments = commentServiceImpl.showAll(postId, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(comments);
    }


    // 댓글 상세 조회
    @GetMapping("/comment/{commentId}")
    public ResponseEntity<CommentDetailDto> show(@PathVariable Long commentId){
        return ResponseEntity.status(HttpStatus.OK).body(commentServiceImpl.show(commentId));
    }
}

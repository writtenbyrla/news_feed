package com.example.news_feed.admin.controller;

import com.example.news_feed.admin.service.serviceImpl.AdminCommentServiceImpl;
import com.example.news_feed.comment.dto.request.UpdateCommentDto;
import com.example.news_feed.comment.dto.response.CommentDetailDto;
import com.example.news_feed.comment.dto.response.CommentResponseDto;
import com.example.news_feed.comment.service.serviceImpl.CommentServiceImpl;
import com.example.news_feed.auth.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
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
@RequestMapping("/admin/*")
public class AdminCommentController {

    private final AdminCommentServiceImpl admincommentServiceImpl;
    private final CommentServiceImpl commentServiceImpl;

    // 댓글 수정
    @PatchMapping("/comment/{commentId}")
    private ResponseEntity<CommentResponseDto> updateComment(@PathVariable Long commentId,
                                                             @RequestBody UpdateCommentDto updateCommentDto,
                                                             @AuthenticationPrincipal final UserDetailsImpl userDetails) {
        updateCommentDto.setUserId(userDetails.getId());
        commentServiceImpl.update(commentId, updateCommentDto);

        CommentResponseDto response = CommentResponseDto.res(HttpStatus.OK.value(), "댓글 수정 완료");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    };

    // 댓글 삭제
    @DeleteMapping("/comment/{commentId}")
    public ResponseEntity<CommentResponseDto> deleteComment(@PathVariable Long commentId,
                                                            @AuthenticationPrincipal final UserDetailsImpl userDetails){
        Long userId = userDetails.getId();
        commentServiceImpl.delete(userId, commentId);
        CommentResponseDto response =  CommentResponseDto.res(HttpStatus.OK.value(), "댓글 삭제 완료");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // 특정 게시글의 댓글 목록 조회
    @GetMapping("/post/{postId}/comment")
    public ResponseEntity<Page<CommentDetailDto>> showAll(@PathVariable Long postId,
                                                          @PageableDefault(value=10)
                                                          @SortDefault(sort = "created_at", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<CommentDetailDto> comments = commentServiceImpl.showAll(postId, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(comments);
    }
}

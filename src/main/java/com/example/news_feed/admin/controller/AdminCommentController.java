package com.example.news_feed.admin.controller;

import com.example.news_feed.admin.service.AdminCommentService;
import com.example.news_feed.comment.dto.request.UpdateCommentDto;
import com.example.news_feed.comment.dto.response.CommentDetailDto;
import com.example.news_feed.comment.dto.response.CommentResponseDto;
import com.example.news_feed.comment.service.CommentService;
import com.example.news_feed.auth.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/*")
public class AdminCommentController {

    private final AdminCommentService AdmincommentService;
    private final CommentService commentService;

    // 댓글 수정
    @PatchMapping("/comments/{commentId}")
    private ResponseEntity<CommentResponseDto> updateComment(@PathVariable Long commentId,
                                                             @RequestBody UpdateCommentDto updateCommentDto,
                                                             @AuthenticationPrincipal final UserDetailsImpl userDetails) {
        updateCommentDto.setUserId(userDetails.getId());
        commentService.update(commentId, updateCommentDto);

        CommentResponseDto response = CommentResponseDto.res(HttpStatus.OK.value(), "댓글 수정 완료");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    };

    // 댓글 삭제
    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<CommentResponseDto> deleteComment(@PathVariable Long commentId,
                                                            @AuthenticationPrincipal final UserDetailsImpl userDetails){
        Long userId = userDetails.getId();
        commentService.delete(userId, commentId);
        CommentResponseDto response =  CommentResponseDto.res(HttpStatus.OK.value(), "댓글 삭제 완료");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // 특정 게시글의 댓글 목록 조회
    @GetMapping("/post/{postId}/comment")
    public ResponseEntity<List<CommentDetailDto>> showAll(@PathVariable Long postId){
        List<CommentDetailDto> comments = commentService.showAll(postId);
        return ResponseEntity.status(HttpStatus.OK).body(comments);
    }

    // 전체 댓글 목록 조회
    @GetMapping("/post/comment")
    public ResponseEntity<List<CommentDetailDto>> showAllComment(){
        List<CommentDetailDto> comments = AdmincommentService.showAll();
        return ResponseEntity.status(HttpStatus.OK).body(comments);
    }

}

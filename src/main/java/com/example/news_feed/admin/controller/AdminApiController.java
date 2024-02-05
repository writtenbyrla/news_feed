package com.example.news_feed.admin.controller;

import com.example.news_feed.admin.service.AdminCommentService;
import com.example.news_feed.admin.service.AdminPostService;
import com.example.news_feed.comment.dto.request.UpdateCommentDto;
import com.example.news_feed.comment.dto.response.CommentResponseDto;
import com.example.news_feed.comment.service.CommentService;
import com.example.news_feed.post.dto.request.UpdatePostDto;
import com.example.news_feed.post.dto.response.PostResponseDto;
import com.example.news_feed.security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/*")
public class AdminApiController {

    @Autowired
    private AdminPostService adminPostService;

    @Autowired
    private AdminCommentService AdmincommentService;

    // 게시글 수정
    @PatchMapping("/post/{postId}")
    public ResponseEntity<PostResponseDto> updatePost(@PathVariable Long postId,
                                                  @RequestBody UpdatePostDto updatePostDto,
                                                  @AuthenticationPrincipal final UserDetailsImpl userDetails) {

        updatePostDto.setUserId(userDetails.getId());
        adminPostService.update(postId, updatePostDto);
        PostResponseDto response = PostResponseDto.res(HttpStatus.OK.value(), "게시물 수정 완료");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // 게시글 삭제
    @DeleteMapping("/post/{postId}")
    public ResponseEntity<PostResponseDto> deletePost(@PathVariable Long postId,
                                                  @AuthenticationPrincipal final UserDetailsImpl userDetails) {
        Long userId = userDetails.getId();
        adminPostService.delete(userId, postId);
        PostResponseDto response = PostResponseDto.res(HttpStatus.OK.value(), "게시물 삭제 완료");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // 댓글 수정
    @PatchMapping("/comments/{commentId}")
    private ResponseEntity<CommentResponseDto> updateComment(@PathVariable Long commentId,
                                                      @RequestBody UpdateCommentDto updateCommentDto,
                                                      @AuthenticationPrincipal final UserDetailsImpl userDetails) {
        updateCommentDto.setUserId(userDetails.getId());
        AdmincommentService.update(commentId, updateCommentDto);

        CommentResponseDto response = CommentResponseDto.res(HttpStatus.OK.value(), "댓글 수정 완료");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    };

    // 댓글 삭제
    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<CommentResponseDto> deleteComment(@PathVariable Long commentId,
                                                     @AuthenticationPrincipal final UserDetailsImpl userDetails){
        Long userId = userDetails.getId();
        AdmincommentService.delete(userId, commentId);
        CommentResponseDto response =  CommentResponseDto.res(HttpStatus.OK.value(), "댓글 삭제 완료");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


}

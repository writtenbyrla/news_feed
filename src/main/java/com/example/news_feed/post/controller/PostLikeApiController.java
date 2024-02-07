package com.example.news_feed.post.controller;

import com.example.news_feed.post.dto.request.PostLikeDto;
import com.example.news_feed.post.dto.response.PostResponseDto;
import com.example.news_feed.post.service.PostLikeService;
import com.example.news_feed.security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
public class PostLikeApiController {

    @Autowired
    private PostLikeService postLikeService;

    // 좋아요 등록
    @PostMapping("/post/{postId}/like")
    public ResponseEntity<PostResponseDto> create(@PathVariable Long postId,
                                                  @AuthenticationPrincipal final UserDetailsImpl userDetails){

        Long userId = userDetails.getId();
        postLikeService.create(postId, userId);
        PostResponseDto response = PostResponseDto.res(HttpStatus.CREATED.value(), "게시글 좋아요 +1");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 좋아요 취소
    @DeleteMapping("/post/{postId}/like")
    public ResponseEntity<PostResponseDto> delete(@PathVariable Long postId,
                                                  @AuthenticationPrincipal final UserDetailsImpl userDetails) {

        Long userId = userDetails.getId();
        postLikeService.delete(userId, postId);
        PostResponseDto response = PostResponseDto.res(HttpStatus.OK.value(), "게시글 좋아요 취소");
        return ResponseEntity.status(HttpStatus.OK).body(response);

    }




}

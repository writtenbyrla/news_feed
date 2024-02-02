package com.example.news_feed.post.controller;

import com.example.news_feed.post.dto.request.CreatePostDto;
import com.example.news_feed.post.dto.request.UpdatePostDto;
import com.example.news_feed.post.dto.response.PostResponseDto;
import com.example.news_feed.post.service.PostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
public class PostApiController {

    @Autowired
    private PostService postService;

    // 게시글 등록
    @PostMapping("/post")
    public ResponseEntity<PostResponseDto> create(@RequestBody CreatePostDto createPostDto) {

        createPostDto.setUserId(1L); // 임의값 -> 나중에 로그인한 정보에서 받아와야 함

        postService.create(createPostDto);
        PostResponseDto response = PostResponseDto.res(HttpStatus.CREATED.value(), "게시물 등록 완료");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 게시글 수정
    @PatchMapping("/post/{postId}")
    public ResponseEntity<PostResponseDto> create(@PathVariable Long postId,
                                                  @RequestBody UpdatePostDto updatePostDto) {

        updatePostDto.setUserId(1L); // 임의값 -> 나중에 로그인한 정보에서 받아와야 함
        postService.update(postId, updatePostDto);
        PostResponseDto response = PostResponseDto.res(HttpStatus.OK.value(), "게시물 수정 완료");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}

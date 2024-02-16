package com.example.news_feed.post.controller;

import com.example.news_feed.post.dto.request.CreatePostDto;
import com.example.news_feed.post.dto.request.UpdatePostDto;
import com.example.news_feed.post.dto.response.PostDetailDto;
import com.example.news_feed.post.dto.response.PostResponseDto;
import com.example.news_feed.post.service.PostService;
import com.example.news_feed.auth.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class PostApiController {

    private final PostService postService;


    @PostMapping("/post")
    public ResponseEntity<PostResponseDto> create(@RequestPart(value="data") CreatePostDto createPostDto,
                                                  @RequestPart(value="files", required = false) List<MultipartFile> files,
                                                  @AuthenticationPrincipal final UserDetailsImpl userDetails) {
        // 로그인한 유저 정보 담기
        createPostDto.setUserId(userDetails.getId());

        // 첨부파일 있을 경우, 없을 경우 service단 분리
        if(files != null && !files.isEmpty()){
            postService.createWithFile(files, createPostDto);
        } else{
            postService.createPost(createPostDto);
        }

        PostResponseDto response = PostResponseDto.res(HttpStatus.CREATED.value(), "게시물 등록 완료");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    // 게시글 수정
    @PatchMapping("/post/{postId}")
    public ResponseEntity<PostResponseDto> update(@PathVariable Long postId,
                                                  @RequestPart(value="data",  required = false) UpdatePostDto updatePostDto,
                                                  @RequestPart(value ="files", required = false) List<MultipartFile> files,
                                                  @AuthenticationPrincipal final UserDetailsImpl userDetails) {

        updatePostDto.setUserId(userDetails.getId());

        // 첨부파일 있을 경우, 없을 경우 service단 분리
        if(files != null && !files.isEmpty()){
            postService.updateWithFile(postId, updatePostDto, files);
        } else{
            postService.update(postId, updatePostDto);
        }

        PostResponseDto response = PostResponseDto.res(HttpStatus.OK.value(), "게시물 수정 완료");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // 게시글 삭제
    @DeleteMapping("/post/{postId}")
    public ResponseEntity<PostResponseDto> delete(@PathVariable Long postId,
                                       @AuthenticationPrincipal final UserDetailsImpl userDetails) {
        Long userId = userDetails.getId();
        postService.delete(userId, postId);
        PostResponseDto response = PostResponseDto.res(HttpStatus.OK.value(), "게시물 삭제 완료");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    // 게시글 목록
    @GetMapping("/post")
    public ResponseEntity<List<PostDetailDto>> showAll(){
        List<PostDetailDto> posts = postService.showAll();
        return ResponseEntity.status(HttpStatus.OK).body(posts);
    }


    // 게시글 상세
    @GetMapping("/post/{postId}")
    public ResponseEntity<PostDetailDto> show(@PathVariable Long postId){
        return ResponseEntity.status(HttpStatus.OK).body(postService.show(postId));
    }

}

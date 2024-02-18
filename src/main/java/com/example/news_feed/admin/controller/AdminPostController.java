package com.example.news_feed.admin.controller;

import com.example.news_feed.admin.service.AdminPostService;
import com.example.news_feed.multimedia.service.MultiMediaService;
import com.example.news_feed.post.dto.request.UpdatePostDto;
import com.example.news_feed.post.dto.response.PostDetailDto;
import com.example.news_feed.post.dto.response.PostResponseDto;
import com.example.news_feed.post.service.PostService;
import com.example.news_feed.auth.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/*")
public class AdminPostController {

    private final PostService postService;
    private final MultiMediaService multiMediaService;


    // 게시글 수정
    @PatchMapping("/post/{postId}")
    public ResponseEntity<PostResponseDto> updatePost(@PathVariable Long postId,
                                                  @RequestBody UpdatePostDto updatePostDto,
                                                  @AuthenticationPrincipal final UserDetailsImpl userDetails) {
        updatePostDto.setUserId(userDetails.getId());
        postService.update(postId, updatePostDto);
        PostResponseDto response = PostResponseDto.res(HttpStatus.OK.value(), "게시물 수정 완료");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // 게시글 삭제
    @DeleteMapping("/post/{postId}")
    public ResponseEntity<PostResponseDto> deletePost(@PathVariable Long postId,
                                                  @AuthenticationPrincipal final UserDetailsImpl userDetails) {
        Long userId = userDetails.getId();
        postService.delete(userId, postId);
        PostResponseDto response = PostResponseDto.res(HttpStatus.OK.value(), "게시물 삭제 완료");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // 게시글 목록 조회
    @GetMapping("/post")
    public ResponseEntity<List<PostDetailDto>> showAll(){
        List<PostDetailDto> posts = postService.showAll();
        return ResponseEntity.status(HttpStatus.OK).body(posts);
    }

    // 멀티미디어 수정
    @PatchMapping("/post/{postId}/file")
    public ResponseEntity<PostResponseDto> updateFile(@PathVariable Long postId,
                                                      @RequestPart(value ="files", required = false) List<MultipartFile> files) {

        multiMediaService.updateFile(postId, files);

        PostResponseDto response = PostResponseDto.res(HttpStatus.OK.value(), "게시물 수정 완료");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // 멀티미디어 삭제
    @DeleteMapping("/post/{multiMediaId}/file")
    public ResponseEntity<PostResponseDto> deleteFile(@PathVariable Long multiMediaId,
                                                      @AuthenticationPrincipal final UserDetailsImpl userDetails) {
        Long userId = userDetails.getId();
        multiMediaService.deleteFiles(userId, multiMediaId);
        PostResponseDto response = PostResponseDto.res(HttpStatus.OK.value(), "게시물 삭제 완료");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}

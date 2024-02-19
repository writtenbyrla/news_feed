package com.example.news_feed.admin.controller;

import com.example.news_feed.multimedia.serviceImpl.MultiMediaServiceImpl;
import com.example.news_feed.post.dto.request.UpdatePostDto;
import com.example.news_feed.post.dto.response.PostDetailDto;
import com.example.news_feed.post.dto.response.PostResponseDto;
import com.example.news_feed.post.serviceImpl.PostServiceImpl;
import com.example.news_feed.auth.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
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

    private final PostServiceImpl postServiceImpl;
    private final MultiMediaServiceImpl multiMediaServiceImpl;


    // 게시글 수정
    @PatchMapping("/post/{postId}")
    public ResponseEntity<PostResponseDto> updatePost(@PathVariable Long postId,
                                                  @RequestBody UpdatePostDto updatePostDto,
                                                  @AuthenticationPrincipal final UserDetailsImpl userDetails) {
        updatePostDto.setUserId(userDetails.getId());
        postServiceImpl.update(postId, updatePostDto);
        PostResponseDto response = PostResponseDto.res(HttpStatus.OK.value(), "게시물 수정 완료");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // 게시글 삭제
    @DeleteMapping("/post/{postId}")
    public ResponseEntity<PostResponseDto> deletePost(@PathVariable Long postId,
                                                  @AuthenticationPrincipal final UserDetailsImpl userDetails) {
        Long userId = userDetails.getId();
        postServiceImpl.delete(userId, postId);
        PostResponseDto response = PostResponseDto.res(HttpStatus.OK.value(), "게시물 삭제 완료");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // 게시글 목록 조회
    @GetMapping("/post")
    public ResponseEntity<List<PostDetailDto>> showAll(){
        List<PostDetailDto> posts = postServiceImpl.showAll();
        return ResponseEntity.status(HttpStatus.OK).body(posts);
    }

    // 멀티미디어 수정
    @PatchMapping("/post/{postId}/file")
    public ResponseEntity<PostResponseDto> updateFile(@PathVariable Long postId,
                                                      @RequestPart(value ="files", required = false) List<MultipartFile> files,
                                                      @AuthenticationPrincipal final UserDetailsImpl userDetails) {


        multiMediaServiceImpl.updateFile(userDetails, postId, files);

        PostResponseDto response = PostResponseDto.res(HttpStatus.OK.value(), "게시물 수정 완료");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // 멀티미디어 삭제
    @DeleteMapping("/post/{multiMediaId}/file")
    public ResponseEntity<PostResponseDto> deleteFile(@PathVariable Long multiMediaId,
                                                      @AuthenticationPrincipal final UserDetailsImpl userDetails) {
        Long userId = userDetails.getId();
        multiMediaServiceImpl.deleteFiles(userId, multiMediaId);
        PostResponseDto response = PostResponseDto.res(HttpStatus.OK.value(), "게시물 삭제 완료");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}

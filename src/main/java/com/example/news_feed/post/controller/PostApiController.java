package com.example.news_feed.post.controller;

import com.example.news_feed.common.aop.annotation.RunningTime;
import com.example.news_feed.multimedia.dto.MultiMediaDto;
import com.example.news_feed.multimedia.service.serviceImpl.MultiMediaServiceImpl;
import com.example.news_feed.post.dto.request.CreatePostDto;
import com.example.news_feed.post.dto.request.UpdatePostDto;
import com.example.news_feed.post.dto.response.PostDetailDto;
import com.example.news_feed.post.dto.response.PostResponseDto;
import com.example.news_feed.post.service.serviceImpl.PostServiceImpl;
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
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class PostApiController {

    private final PostServiceImpl postServiceImpl;
    private final MultiMediaServiceImpl multiMediaServiceImpl;

    // 게시글 작성
    @PostMapping("/post")
    public ResponseEntity<PostResponseDto> create(@RequestBody CreatePostDto createPostDto,
                                                  @AuthenticationPrincipal final UserDetailsImpl userDetails) {

        createPostDto.setUserId(userDetails.getId());
        postServiceImpl.createPost(createPostDto);
        PostResponseDto response = PostResponseDto.res(HttpStatus.CREATED.value(), "게시물 등록 완료");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 멀티미디어 추가
    @PostMapping("/post/{postId}/file")
    public ResponseEntity<PostResponseDto> createFile(@RequestPart(value="files", required = false) List<MultipartFile> files,
                                                      @PathVariable Long postId) {
        multiMediaServiceImpl.createFile(files, postId);
        PostResponseDto response = PostResponseDto.res(HttpStatus.CREATED.value(), "게시물 등록 완료");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 게시글 수정
    @PatchMapping("/post/{postId}")
    public ResponseEntity<PostResponseDto> update(@PathVariable Long postId,
                                                  @RequestBody UpdatePostDto updatePostDto,
                                                  @AuthenticationPrincipal final UserDetailsImpl userDetails) {

        updatePostDto.setUserId(userDetails.getId());
        postServiceImpl.update(postId, updatePostDto);
        PostResponseDto response = PostResponseDto.res(HttpStatus.OK.value(), "게시물 수정 완료");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // 멀티미디어 수정
    @PatchMapping("/post/{postId}/file")
    public ResponseEntity<PostResponseDto> updateFile(@PathVariable Long postId,
                                                      @AuthenticationPrincipal final UserDetailsImpl userDetails,
                                                      @RequestPart(value ="files", required = false) List<MultipartFile> files) {

        multiMediaServiceImpl.updateFile(userDetails, postId, files);

        PostResponseDto response = PostResponseDto.res(HttpStatus.OK.value(), "게시물 수정 완료");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // 게시글 삭제
    @DeleteMapping("/post/{postId}")
    public ResponseEntity<PostResponseDto> delete(@PathVariable Long postId,
                                       @AuthenticationPrincipal final UserDetailsImpl userDetails) {
        Long userId = userDetails.getId();
        postServiceImpl.delete(userId, postId);
        PostResponseDto response = PostResponseDto.res(HttpStatus.OK.value(), "게시물 삭제 완료");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // 멀티미디어 삭제
    @DeleteMapping("/post/{multiMediaId}/file")
    public ResponseEntity<PostResponseDto> deleteFile(@PathVariable Long multiMediaId,
                                                      @AuthenticationPrincipal final UserDetailsImpl userDetails) {
        Long userId = userDetails.getId();
        multiMediaServiceImpl.deleteFiles(userId, multiMediaId);
        PostResponseDto response = PostResponseDto.res(HttpStatus.OK.value(), "멀티미디어 삭제 완료");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    // 게시글 목록
//    @RunningTime
    @GetMapping("/post")
    public ResponseEntity<Page<PostDetailDto>> showAll(@PageableDefault(value=10)
                                                           @SortDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<PostDetailDto> posts = postServiceImpl.showAll(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(posts);
    }

    // 게시글 상세
    @GetMapping("/post/{postId}")
    public ResponseEntity<PostDetailDto> show(@PathVariable Long postId){
        return ResponseEntity.status(HttpStatus.OK).body(postServiceImpl.show(postId));
    }


    // 멀티미디어 조회
    @GetMapping("/post/{postId}/file")
    public ResponseEntity<List<MultiMediaDto>> showFile(@PathVariable Long postId){
        return ResponseEntity.status(HttpStatus.OK).body(multiMediaServiceImpl.showFiles(postId));
    }

    // 게시글 검색(제목, 내용)
    @RunningTime
    @GetMapping("/post/search")
    public ResponseEntity<Page<PostDetailDto>> searchByKeyword(@RequestParam("keyword") String keyword, @PageableDefault(size = 10) Pageable pageable){
        Page<PostDetailDto> posts = postServiceImpl.findByOption(keyword, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(posts);
    }

    // 게시글 검색(작성자)
    @GetMapping("/post/search/user")
    public ResponseEntity<Page<PostDetailDto>> searchByUser(@RequestParam("username") String username, @PageableDefault(size = 10) Pageable pageable){
        Page<PostDetailDto> posts = postServiceImpl.findByUser(username, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(posts);
    }

}

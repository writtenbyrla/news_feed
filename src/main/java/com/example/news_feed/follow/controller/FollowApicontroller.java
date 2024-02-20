package com.example.news_feed.follow.controller;

import com.example.news_feed.follow.dto.response.FollowResponseDto;
import com.example.news_feed.follow.dto.response.FollowingPostDto;
import com.example.news_feed.follow.serviceImpl.FollowServiceImpl;
import com.example.news_feed.auth.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class FollowApicontroller {

    private final FollowServiceImpl followServiceImpl;

    // 팔로우 등록
    @PostMapping("/follow/{followingId}")
    public ResponseEntity<FollowResponseDto> create(@PathVariable Long followingId,
                                                    @AuthenticationPrincipal final UserDetailsImpl userDetails) {
        Long followerId = userDetails.getId();
        followServiceImpl.create(followingId, followerId);
        FollowResponseDto response = FollowResponseDto.res(HttpStatus.CREATED.value(), "팔로우 완료");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 팔로우 삭제
    @DeleteMapping("/follow/{followingId}")
    public ResponseEntity<FollowResponseDto> delete(@PathVariable Long followingId,
                                                    @AuthenticationPrincipal final UserDetailsImpl userDetails) {
        Long followerId = userDetails.getId();
        followServiceImpl.delete(followingId, followerId);
        FollowResponseDto response = FollowResponseDto.res(HttpStatus.CREATED.value(), "팔로우 취소 완료");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 팔로우하는 사용자의 게시물 보기
    @GetMapping("/follow/post")
    public ResponseEntity<Page<FollowingPostDto>> show(@AuthenticationPrincipal final UserDetailsImpl userDetails,
                                                       @PageableDefault(value=10)
                                                       @SortDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Long followerId = userDetails.getId();
        Page<FollowingPostDto> posts = followServiceImpl.showAll(followerId, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(posts);
    }



}

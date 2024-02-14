package com.example.news_feed.follow.controller;

import com.example.news_feed.follow.dto.response.FollowResponseDto;
import com.example.news_feed.follow.dto.response.FollowingPostDto;
import com.example.news_feed.follow.service.FollowService;
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
public class FollowApicontroller {

    private final FollowService followService;

    // 팔로우 등록
    @PostMapping("/follow/{followingId}")
    public ResponseEntity<FollowResponseDto> create(@PathVariable Long followingId,
                                                    @AuthenticationPrincipal final UserDetailsImpl userDetails) {
        Long followerId = userDetails.getId();
        followService.create(followingId, followerId);
        FollowResponseDto response = FollowResponseDto.res(HttpStatus.CREATED.value(), "팔로우 완료");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 팔로우 삭제
    @DeleteMapping("/follow/{followingId}")
    public ResponseEntity<FollowResponseDto> delete(@PathVariable Long followingId,
                                                    @AuthenticationPrincipal final UserDetailsImpl userDetails) {
        Long followerId = userDetails.getId();
        followService.delete(followingId, followerId);
        FollowResponseDto response = FollowResponseDto.res(HttpStatus.CREATED.value(), "팔로우 취소 완료");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 팔로우하는 사용자의 게시물 보기
    @GetMapping("/follow/post")
    public ResponseEntity<List<FollowingPostDto>> show(@AuthenticationPrincipal final UserDetailsImpl userDetails) {
        Long followerId = userDetails.getId();
        List<FollowingPostDto> posts = followService.showAll(followerId);
        return ResponseEntity.status(HttpStatus.OK).body(posts);
    }
}

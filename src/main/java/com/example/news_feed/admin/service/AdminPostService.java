package com.example.news_feed.admin.service;

import com.example.news_feed.common.exception.HttpException;
import com.example.news_feed.post.domain.Post;
import com.example.news_feed.post.dto.request.UpdatePostDto;
import com.example.news_feed.post.repository.PostRepository;
import com.example.news_feed.user.domain.User;
import com.example.news_feed.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class AdminPostService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @Transactional
    // 게시글 수정
    public UpdatePostDto update(Long postId, UpdatePostDto updatePostDto) {

        updatePostDto.setPostId(postId);

        // 기존 유저정보 조회 및 예외 처리
        checkUser(updatePostDto.getUserId());

        // 수정하고자 하는 게시글 정보 조회
        Post target = checkPost(postId);

        // 게시글 수정
        target.patch(updatePostDto);

        // 엔티티 db 저장
        Post updated = postRepository.save(target);

        // dto로 변환하여 반환
        return UpdatePostDto.updatePostDto(updated);

    }

    @Transactional
    // 게시글 삭제
    public Post delete(Long userId, Long postId) {
        // 기존 유저정보 조회 및 예외 처리
        checkUser(userId);

        // 게시글 찾기
        Post target = checkPost(postId);

        postRepository.delete(target);
        return target;
    }

    // 유저 정보 확인
    private User checkUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new HttpException(false, "유저 정보가 없습니다.", HttpStatus.BAD_REQUEST));
    }

    // 게시글 정보 확인
    private Post checkPost(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new HttpException(false, "게시글 정보가 없습니다.", HttpStatus.BAD_REQUEST));
    }


}

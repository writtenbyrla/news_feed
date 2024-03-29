package com.example.news_feed.admin.service.serviceImpl;

import com.example.news_feed.admin.service.AdminPostService;
import com.example.news_feed.post.domain.Post;
import com.example.news_feed.post.dto.request.UpdatePostDto;
import com.example.news_feed.post.exception.PostErrorCode;
import com.example.news_feed.post.exception.PostException;
import com.example.news_feed.post.repository.PostRepository;
import com.example.news_feed.user.domain.User;
import com.example.news_feed.user.exception.UserErrorCode;
import com.example.news_feed.user.exception.UserException;
import com.example.news_feed.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminPostServiceImpl implements AdminPostService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @Transactional
    // 게시글 수정
    public UpdatePostDto update(Long postId, UpdatePostDto updatePostDto) {

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
                .orElseThrow(() ->  new UserException(UserErrorCode.USER_NOT_EXIST));
    }

    // 게시글 정보 확인
    private Post checkPost(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new PostException(PostErrorCode.POST_NOT_EXIST));
    }


}

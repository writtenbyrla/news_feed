package com.example.news_feed.post.service;

import com.example.news_feed.common.exception.HttpException;
import com.example.news_feed.post.domain.Post;
import com.example.news_feed.post.dto.request.CreatePostDto;
import com.example.news_feed.post.dto.request.UpdatePostDto;
import com.example.news_feed.post.dto.response.PostDetailDto;
import com.example.news_feed.post.repository.PostRepository;
import com.example.news_feed.user.domain.User;
import com.example.news_feed.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @Transactional
    public CreatePostDto create(CreatePostDto createPostDto) {

        // 사용자 정보
        User user = checkUser(createPostDto.getUserId());

        // 엔티티 생성
        Post post = new Post(createPostDto, user);

        // 엔티티를 DB로 저장
        Post created = postRepository.save(post);

        // DTO로 변경하여 반환
        return CreatePostDto.createPostDto(created);
    }

    @Transactional
    // 게시글 수정
    public UpdatePostDto update(Long postId, UpdatePostDto updatePostDto) {

        updatePostDto.setPostId(postId);

        // 기존 유저정보 조회 및 예외 처리
        checkUser(updatePostDto.getUserId());

        // 수정하고자 하는 게시글 정보 조회
        Post target = checkPost(postId);

        // 작성자 여부 확인
        isWrittenbyUser(updatePostDto.getUserId(), target.getUser().getUserId());

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

        // 게시글 확인
        Post target = checkPost(postId);

        // 작성자 일치 여부 확인
        isWrittenbyUser(userId,target.getUser().getUserId());

        postRepository.delete(target);
        return target;
    }

    // 게시글 목록
    public List<PostDetailDto> showAll() {
        return postRepository.findAll()
                .stream()
                .map(PostDetailDto::createPostDto)
                .collect(Collectors.toList());

    }

    // 게시글 상세
    public PostDetailDto show(Long postId) {
        return postRepository.findById(postId)
                .map(PostDetailDto::createPostDto)
                .orElse(null);
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

    // 본인 작성 여부 확인
    private void isWrittenbyUser(Long userId, Long postUserId) {
        if (!userId.equals(postUserId)) {
            throw new HttpException(false, "본인이 작성한 게시글이 아닙니다.", HttpStatus.BAD_REQUEST);
        }
    }


}

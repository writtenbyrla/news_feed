package com.example.news_feed.post.service;

import com.example.news_feed.post.domain.Post;
import com.example.news_feed.post.dto.request.CreatePostDto;
import com.example.news_feed.post.dto.request.UpdatePostDto;
import com.example.news_feed.post.repository.PostRepository;
import com.example.news_feed.user.domain.User;
import com.example.news_feed.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@Slf4j
public class PostService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Transactional
    public CreatePostDto create(CreatePostDto createPostDto) {

        // 사용자 정보
        User user = userRepository.findById(createPostDto.getUserId())
                .orElseThrow( () -> new IllegalArgumentException("등록된 사용자가 없습니다."));

        // 엔티티 생성
        Post post = new Post(createPostDto, user, new Date());

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
        User user = userRepository.findById(updatePostDto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("게시글 수정 실패! 유저 정보가 없습니다."));

        // 수정하고자 하는 게시글 정보 조회
        Post target = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글 정보가 없습니다."));

        // 작성자가 일치하거나 ADMIN인 경우에만 수정 가능
        if ((updatePostDto.getUserId() != target.getUser().getUserId()) && (!user.getRole().toString().equals("ADMIN"))){
            throw new IllegalArgumentException("본인이 작성한 게시글이 아닙니다. 수정이 불가능합니다.");
        }

        // 게시글 수정
        target.setUpdatedAt(new Date());
        target.patch(updatePostDto);

        // 엔티티 db 저장
        Post updated = postRepository.save(target);

        // dto로 변환하여 반환
        return UpdatePostDto.updatePostDto(updated);

    }

    // 게시글 삭제
    public Post delete(Long userId, Long postId) {
        // 기존 유저정보 조회 및 예외 처리
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("게시글 삭제 실패! 유저 정보가 없습니다."));

        // 게시글 찾기
        Post target = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글 정보가 없습니다."));

        // 작성자가 일치하거나 ADMIN인 경우에만 삭제 가능
        if ((userId != target.getUser().getUserId()) && (!user.getRole().toString().equals("ADMIN"))){
            throw new IllegalArgumentException("본인이 작성한 게시글이 아닙니다. 삭제가 불가능합니다.");
        }

        postRepository.delete(target);
        return target;
    }


}

package com.example.news_feed.post.service;

import com.example.news_feed.post.domain.Post;
import com.example.news_feed.post.domain.PostLike;
import com.example.news_feed.post.dto.request.PostLikeDto;
import com.example.news_feed.post.repository.PostLikeRepository;
import com.example.news_feed.post.repository.PostRepository;
import com.example.news_feed.user.domain.User;
import com.example.news_feed.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostLikeService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostLikeRepository postLikeRepository;


    // 좋아요 등록
    // 사용자 정보 확인, 대상 게시글 확인
    // 이미 좋아요 눌렀는지 확인
    @Transactional
    public PostLikeDto create(Long postId, Long userId) {

        // 사용자 정보
        User user = userRepository.findById(userId)
                .orElseThrow( () -> new IllegalArgumentException("등록된 사용자가 없습니다."));

        // 게시글 정보
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("등록된 게시물이 없습니다."));

        // 자신의 게시글인 경우 좋아요 불가능
        if (userId.equals(post.getUser().getUserId())){
            throw new IllegalArgumentException("자신의 게시글은 좋아요를 할 수 없습니다.");
        }

        // 좋아요 정보
        PostLike target = postLikeRepository.findByUserIdPostId(userId, postId);

        if (target != null){
            throw new IllegalArgumentException("이 게시글을 이미 좋아합니다!");
        }

        // 엔티티 생성
        PostLike postLike = PostLike.createPostLike(user, post);

        // db 저장
        PostLike created = postLikeRepository.save(postLike);

        // dto 반환
       return PostLikeDto.createPostLikeDto(created);
    }

    // 좋아요 취소
    @Transactional
    public PostLikeDto delete(Long userId, Long postId) {
        // 사용자 정보
        User user = userRepository.findById(userId)
                .orElseThrow( () -> new IllegalArgumentException("등록된 사용자가 없습니다."));

        // 게시글 정보
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("등록된 게시물이 없습니다."));

        // 좋아요 정보
        PostLike target = postLikeRepository.findByUserIdPostId(userId, postId);

        if (target == null) {
            throw new IllegalArgumentException("삭제할 좋아요가 없습니다.");
        }

        postLikeRepository.delete(target);
        return PostLikeDto.createPostLikeDto(target);
    }
}

package com.example.news_feed.post.service;

import com.example.news_feed.common.exception.HttpException;
import com.example.news_feed.post.domain.Post;
import com.example.news_feed.post.domain.PostLike;
import com.example.news_feed.post.dto.request.PostLikeDto;
import com.example.news_feed.post.repository.PostLikeRepository;
import com.example.news_feed.post.repository.PostRepository;
import com.example.news_feed.user.domain.User;
import com.example.news_feed.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostLikeService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final PostLikeRepository postLikeRepository;


    // 좋아요 등록
    // 사용자 정보 확인, 대상 게시글 확인
    // 이미 좋아요 눌렀는지 확인
    @Transactional
    public PostLikeDto create(Long postId, Long userId) {

        // 사용자 정보
        User user = checkUser(userId);

        // 게시글 정보
        Post post = checkPost(postId);

        // 자신의 게시글인 경우 좋아요 불가능
        if (userId.equals(post.getUser().getUserId())){
            throw new HttpException(false, "자신의 게시글은 좋아요를 할 수 없습니다.", HttpStatus.BAD_REQUEST);
        }

        // 이미 좋아요 한 경우 다시 좋아요 불가능
        postLikeRepository.findByUserIdPostId(userId, postId)
                .ifPresent(like -> {
                    throw new HttpException(false, "이 게시글을 이미 좋아합니다!", HttpStatus.BAD_REQUEST);});

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
        checkUser(userId);

        // 게시글 정보
        checkPost(postId);

        // 좋아요 정보
        PostLike target = checkPostLike(userId, postId);

        postLikeRepository.delete(target);
        return PostLikeDto.createPostLikeDto(target);
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

    // 게시글 좋아요 정보 확인
    private PostLike checkPostLike(Long userId, Long postId) {
        return postLikeRepository.findByUserIdPostId(userId, postId)
                .orElseThrow(() -> new HttpException(false, "게시글 좋아요 정보가 없습니다.", HttpStatus.BAD_REQUEST));
    }


}

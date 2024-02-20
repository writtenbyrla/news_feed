package com.example.news_feed.post.service.serviceImpl;

import com.example.news_feed.post.domain.Post;
import com.example.news_feed.post.domain.PostLike;
import com.example.news_feed.post.dto.request.PostLikeDto;
import com.example.news_feed.post.exception.PostErrorCode;
import com.example.news_feed.post.exception.PostException;
import com.example.news_feed.post.repository.PostLikeRepository;
import com.example.news_feed.post.repository.PostRepository;
import com.example.news_feed.post.service.PostLikeService;
import com.example.news_feed.user.domain.User;
import com.example.news_feed.user.exception.UserErrorCode;
import com.example.news_feed.user.exception.UserException;
import com.example.news_feed.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostLikeServiceImpl implements PostLikeService {

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
            throw new PostException(PostErrorCode.IS_SELF_LIKE);
        }

        // 이미 좋아요 한 경우 다시 좋아요 불가능
        postLikeRepository.findByUserIdPostId(userId, postId)
                .ifPresent(like -> {
                    throw new PostException(PostErrorCode.ALREADY_LIKE);
                });

        // 엔티티 생성
        PostLike postLike = PostLike.createPostLike(user, post);

        // db 저장
        PostLike created = postLikeRepository.save(postLike);

        // dto 반환
       return PostLikeDto.createPostLikeDto(created);
    }

    // 좋아요 취소
    @Transactional
    public PostLikeDto delete(Long userId, Long postLikeId) {
        // 사용자 정보
        checkUser(userId);

        // 좋아요 정보
        PostLike target = checkPostLike(postLikeId);

        postLikeRepository.delete(target);
        return PostLikeDto.createPostLikeDto(target);
    }


    // 게시글 좋아요 정보 확인
    private PostLike checkPostLike(Long postLikeId) {
        return postLikeRepository.findById(postLikeId)
                .orElseThrow(() ->  new PostException(PostErrorCode.POSTLIKE_NOT_EXIST));
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

package com.example.news_feed.follow.service.serviceImpl;

import com.example.news_feed.common.exception.HttpException;
import com.example.news_feed.follow.domain.Follow;
import com.example.news_feed.follow.dto.request.CreateFollowDto;
import com.example.news_feed.follow.dto.response.FollowingPostDto;
import com.example.news_feed.follow.exception.FollowErrorCode;
import com.example.news_feed.follow.exception.FollowException;
import com.example.news_feed.follow.repository.FollowRepository;
import com.example.news_feed.follow.service.FollowService;
import com.example.news_feed.post.domain.Post;
import com.example.news_feed.post.dto.response.PostDetailDto;
import com.example.news_feed.post.exception.PostErrorCode;
import com.example.news_feed.post.exception.PostException;
import com.example.news_feed.post.repository.PostRepository;
import com.example.news_feed.user.domain.User;
import com.example.news_feed.user.exception.UserErrorCode;
import com.example.news_feed.user.exception.UserException;
import com.example.news_feed.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class FollowServiceImpl implements FollowService {

    private final FollowRepository followRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    public CreateFollowDto create(Long followingId, Long followerId) {
        // 팔로잉 사용자 정보
        User following = checkUser(followingId);

        // 팔로워 사용자 정보
        User follower =  checkUser(followerId);

        // 팔로우 자신을 팔로우하려는지 확인
        if (followingId.equals(followerId)) {
            throw new FollowException(FollowErrorCode.IS_SELF_FOLLOW);
        }

        // 팔로우 정보
        followRepository.findByBothId(followingId, followerId)
                .ifPresent(follow -> {
                    throw new FollowException(FollowErrorCode.ALREADY_FOLLOWING);
                });

        Follow follow = Follow.createFollow(following, follower);
        Follow created = followRepository.save(follow);
        return CreateFollowDto.createFollowDto(created);
    }

    @Transactional
    public CreateFollowDto delete(Long followingId, Long followerId) {
        // 팔로우 정보
        Follow target = followRepository.findByBothId(followingId, followerId)
                .orElseThrow(() -> new FollowException(FollowErrorCode.NOT_FOUND_FOLLOW)
                );

        followRepository.delete(target);
        return CreateFollowDto.createFollowDto(target);
    }


    // 내가 팔로우 한 유저의 게시글 보기
    public Page<FollowingPostDto> showAll(Long followerId, Pageable pageable) {

        // 팔로우 목록
        List<Follow> follows = followRepository.findByFollowerId(followerId);
        if (follows.isEmpty()){
            throw new FollowException(FollowErrorCode.NOT_FOUND_FOLLOW);
        }

        // 팔로우 목록에서 유저 추출
        List<User> followings = follows.stream()
                .map(Follow::getFollowing)
                .collect(Collectors.toList());

        // 유저 정보로 post 조회 후 리스트 반환
        Page<Post> posts = postRepository.findByUserIn(followings, pageable);
        if(posts.isEmpty()){
            throw new PostException(PostErrorCode.POST_NOT_EXIST);
        }
        return new PageImpl<>(
                posts.getContent().stream()
                        .map(FollowingPostDto::createFollowingPostDto)
                        .collect(Collectors.toList()),
                pageable,
                posts.getTotalElements()
        );
    }

    // 유저 정보 확인
    private User checkUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() ->  new UserException(UserErrorCode.USER_NOT_EXIST));
    }


}

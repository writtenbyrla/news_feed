package com.example.news_feed.follow.service;

import com.example.news_feed.follow.domain.Follow;
import com.example.news_feed.follow.dto.request.CreateFollowDto;
import com.example.news_feed.follow.dto.response.FollowResponseDto;
import com.example.news_feed.follow.dto.response.FollowingPostDto;
import com.example.news_feed.follow.repository.FollowRepository;
import com.example.news_feed.post.domain.Post;
import com.example.news_feed.post.repository.PostRepository;
import com.example.news_feed.user.domain.User;
import com.example.news_feed.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FollowService {

    @Autowired
    private FollowRepository followRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public CreateFollowDto create(Long followingId, Long followerId) {
        // 사용자 정보
        User following = userRepository.findById(followingId)
                .orElseThrow( () -> new IllegalArgumentException("등록된 사용자가 없습니다."));

        // 사용자 정보
        User follower = userRepository.findById(followerId)
                .orElseThrow( () -> new IllegalArgumentException("등록된 사용자가 없습니다."));

        // 팔로우 자신을 팔로우하려는지 확인
        if (followingId.equals(followerId)) {
            throw new IllegalArgumentException("자신을 팔로우할 수 없습니다.");
        }

        // 팔로우 정보
        Follow target = followRepository.findByBothId(followingId, followerId);

        if (target != null) {
            throw new IllegalArgumentException("이미 팔로우중입니다.");
        }

        Follow follow = Follow.createFollow(following, follower);
        Follow created = followRepository.save(follow);
        return CreateFollowDto.createFollowDto(created);
    }

    @Transactional
    public CreateFollowDto delete(Long followingId, Long followerId) {
        // 팔로우 정보
        Follow target = followRepository.findByBothId(followingId, followerId);

        if (target == null) {
            throw new IllegalArgumentException("팔로우 취소 불가능! 팔로우 대상이 아닙니다.");
        }

        followRepository.delete(target);
        return CreateFollowDto.createFollowDto(target);
    }

    // 내가 팔로우 한 유저의 게시글 보기
    // 1. 내가 팔로우한 유저 목록 받아오기
    // 2. 받아온 유저목록으로 게시글 조회
    public List<FollowingPostDto> showAll(Long followerId) {

        // 팔로우 목록
        List<Follow> follows = followRepository.findByFollowerId(followerId);
        log.info(follows.toString());

        // 팔로우 목록에서 유저 추출
        List<User> followings = follows.stream()
                .map(Follow::getFollowingId)
                .collect(Collectors.toList());

        // 유저 정보로 post 조회 후 리스트 반환
        List<Post> posts = postRepository.findByUserIn(followings);
        return posts.stream()
                .map(FollowingPostDto::createFollowingPostDto)
                .collect(Collectors.toList());
    }
}

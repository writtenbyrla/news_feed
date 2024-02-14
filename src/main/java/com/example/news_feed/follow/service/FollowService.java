package com.example.news_feed.follow.service;

import com.example.news_feed.common.exception.HttpException;
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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class FollowService {

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
            throw new HttpException(false, "자신을 팔로우할 수 없습니다.", HttpStatus.BAD_REQUEST);
        }

        // 팔로우 정보
        followRepository.findByBothId(followingId, followerId)
                .ifPresent(follow -> {
                    throw new HttpException(false, "이미 팔로우중입니다.", HttpStatus.BAD_REQUEST);
                });

        Follow follow = Follow.createFollow(following, follower);
        Follow created = followRepository.save(follow);
        return CreateFollowDto.createFollowDto(created);
    }

    @Transactional
    public CreateFollowDto delete(Long followingId, Long followerId) {
        // 팔로우 정보
        Follow target = followRepository.findByBothId(followingId, followerId)
                .orElseThrow(() -> new HttpException(false,"팔로우 취소 불가능! 팔로우 대상이 아닙니다.", HttpStatus.BAD_REQUEST )
                );

        followRepository.delete(target);
        return CreateFollowDto.createFollowDto(target);
    }

    // 내가 팔로우 한 유저의 게시글 보기
    public List<FollowingPostDto> showAll(Long followerId) {

        // 팔로우 목록
        List<Follow> follows = followRepository.findByFollowerId(followerId);

        // 팔로우 목록에서 유저 추출
        List<User> followings = follows.stream()
                .map(Follow::getFollowing)
                .collect(Collectors.toList());

        // 유저 정보로 post 조회 후 리스트 반환
        List<Post> posts = postRepository.findByUserIn(followings);
        return posts.stream()
                .map(FollowingPostDto::createFollowingPostDto)
                .collect(Collectors.toList());
    }

    // 유저 정보 확인
    private User checkUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new HttpException(false, "유저 정보가 없습니다.", HttpStatus.BAD_REQUEST));
    }


}

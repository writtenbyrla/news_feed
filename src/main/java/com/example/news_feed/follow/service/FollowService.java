package com.example.news_feed.follow.service;

import com.example.news_feed.follow.dto.request.CreateFollowDto;
import com.example.news_feed.follow.dto.response.FollowingPostDto;
import com.example.news_feed.user.domain.User;

import java.util.List;

public interface FollowService {
    /*
     * 팔로우 추가
     * @param followingId 팔로우 당하는 사람
     * @param followerId 팔로우 하는 사람
     * @return 팔로우 추가 결과
     */
    CreateFollowDto create(Long followingId, Long followerId);

    /*
     * 팔로우 삭제
     * @param followingId 팔로우 당하는 사람
     * @param followerId 팔로우 하는 사람
     * @return 팔로우 삭제 결과
     */
    CreateFollowDto delete(Long followingId, Long followerId);

    /*
     * 나의 팔로우 목록
     * @param followerId 팔로우 하는 사람
     * @return 팔로우 유저 목록
     */

    List<FollowingPostDto> showAll(Long followerId);


}

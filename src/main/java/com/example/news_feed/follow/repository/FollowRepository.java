package com.example.news_feed.follow.repository;

import com.example.news_feed.follow.domain.Follow;
import com.example.news_feed.follow.dto.response.FollowingPostDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {

    @Query(value = "SELECT * FROM follow WHERE following_id = :followingId and follower_id = :followerId", nativeQuery = true)
    Follow findByBothId(@Param("followingId") Long followingId, @Param("followerId") Long followerId);

    @Query(value = "SELECT * FROM follow WHERE follower_id = :followerId", nativeQuery = true)
    List<Follow> findByFollowerId(@Param("followerId") Long followerId);

    @Query(value = "SELECT * FROM post WHERE user_id IN (:followingIds)", nativeQuery = true)
    List<FollowingPostDto> findByFollowingId(@Param("followingIds")List<Long> followingIds);

}

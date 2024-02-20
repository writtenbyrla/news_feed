package com.example.news_feed.follow.repository;

import com.example.news_feed.follow.domain.Follow;
import com.example.news_feed.follow.dto.response.FollowingPostDto;
import com.example.news_feed.post.domain.Post;
import com.example.news_feed.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Repository
@RepositoryDefinition(domainClass = Follow.class, idClass = Long.class)
public interface FollowRepository extends JpaRepository<Follow, Long> {

    @Query(value = "SELECT * FROM follow WHERE following_id = :followingId and follower_id = :followerId", nativeQuery = true)
    Optional<Follow> findByBothId(@Param("followingId") Long followingId, @Param("followerId") Long followerId);

    @Query(value = "SELECT * FROM follow WHERE follower_id = :followerId", nativeQuery = true)
    List<Follow> findByFollowerId(@Param("followerId") Long followerId);
    @Query(value = "SELECT * FROM follow WHERE following_id = :followingId", nativeQuery = true)
    List<Follow> findByFollowingId(@Param("followingId") Long followingId);
}

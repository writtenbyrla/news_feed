package com.example.news_feed.post.repository;

import com.example.news_feed.post.domain.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PostLikeRepository extends JpaRepository<PostLike, Long> {

    @Query(value = "SELECT * FROM postlike WHERE user_id =:userId AND post_id =:postId", nativeQuery = true)
    PostLike findByUserIdPostId(@Param("userId") Long userId, @Param("postId") Long postId);
}

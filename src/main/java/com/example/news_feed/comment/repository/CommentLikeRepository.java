package com.example.news_feed.comment.repository;

import com.example.news_feed.comment.domain.CommentLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {

    @Query(value = "SELECT * FROM commentlike WHERE comment_id =:commentId", nativeQuery = true)
    Optional<CommentLike> findByCommentId(Long commentId);
}

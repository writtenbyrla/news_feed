package com.example.news_feed.comment.repository;

import com.example.news_feed.comment.domain.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
@RepositoryDefinition(domainClass = Comment.class, idClass = Long.class)
public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query(value = "SELECT * FROM comment WHERE post_id = :postId", nativeQuery = true)
    Page<Comment> findByPostId(@Param("postId") Long postId, Pageable pageable);

    @Query(value = "SELECT * FROM comment WHERE user_id = :userId", nativeQuery = true)
    Page<Comment> findByUserId(@Param("userId") Long userId, Pageable pageable);
}

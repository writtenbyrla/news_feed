package com.example.news_feed.multimedia.repository;

import com.example.news_feed.multimedia.domain.MultiMedia;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MultiMediaRepository extends JpaRepository<MultiMedia, Long> {
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM multimedia WHERE post_id = :postId", nativeQuery = true)
    void deleteByPostId(@Param("postId") Long postId);
    @Query(value = "SELECT * FROM multimedia WHERE post_id = :postId", nativeQuery = true)
    List<MultiMedia> findByPostId(@Param("postId") Long postId);
}

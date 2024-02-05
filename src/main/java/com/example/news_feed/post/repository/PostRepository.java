package com.example.news_feed.post.repository;

import com.example.news_feed.post.domain.Post;
import com.example.news_feed.post.dto.response.PostDetailDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {


}


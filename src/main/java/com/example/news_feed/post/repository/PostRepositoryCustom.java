package com.example.news_feed.post.repository;

import com.example.news_feed.post.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PostRepositoryCustom {

        Page<Post> findByOption(String keyword, Pageable pageable);

        Page<Post> findByUser(String username, Pageable pageable);

}

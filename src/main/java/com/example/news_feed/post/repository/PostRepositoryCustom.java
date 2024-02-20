package com.example.news_feed.post.repository;

import com.example.news_feed.post.domain.Post;

import java.util.List;

public interface PostRepositoryCustom {

        List<Post> findBySearchOption(String title, String content, String username);
}

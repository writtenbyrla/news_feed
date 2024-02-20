package com.example.news_feed.post.repository;


import com.example.news_feed.post.domain.Post;
import com.example.news_feed.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom{

    List<Post> findByUserIn(List<User> followings);
}


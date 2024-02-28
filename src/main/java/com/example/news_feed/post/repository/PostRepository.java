package com.example.news_feed.post.repository;


import com.example.news_feed.post.domain.Post;
import com.example.news_feed.user.domain.User;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@RepositoryDefinition(domainClass = Post.class, idClass = Long.class)
public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom{

    Page<Post> findByUserIn(List<User> followings, Pageable pageable);

    @Query(value = "SELECT * FROM post WHERE user_id = :userId", nativeQuery = true)
    Page<Post> findByUserId(@Param("userId") Long userId, Pageable pageable);

    @Query("SELECT p FROM Post p JOIN FETCH p.user")
    Page<Post> findAll(Pageable pageable);
}


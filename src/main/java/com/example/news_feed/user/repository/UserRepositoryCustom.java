package com.example.news_feed.user.repository;

import com.example.news_feed.user.domain.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepositoryCustom {
    @Query(value = "SELECT * FROM user WHERE username LIKE %:username% AND status = 'Y'", nativeQuery = true)
    List<User> findByUsername(@Param("username") String username);
}

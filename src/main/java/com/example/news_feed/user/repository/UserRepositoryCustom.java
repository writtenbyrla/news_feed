package com.example.news_feed.user.repository;

import com.example.news_feed.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepositoryCustom {

    Page<User> findByUsername(@Param("username") String username, Pageable pageable);
}

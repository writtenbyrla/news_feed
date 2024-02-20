package com.example.news_feed.admin.repository;

import com.example.news_feed.comment.domain.Comment;
import com.example.news_feed.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminUserRepository extends JpaRepository<User, Long> {
}

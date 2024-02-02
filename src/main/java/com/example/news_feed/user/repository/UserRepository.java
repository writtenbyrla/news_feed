package com.example.news_feed.user.repository;

import com.example.news_feed.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query(value="select * from user where username = :username", nativeQuery = true)
    Optional<User> findByName(@Param("username") String username);

    @Query(value="select * from user where email = :email", nativeQuery = true)
    Optional<User> findByEmail(@Param("email") String email);

    @Query(value = "SELECT * FROM user WHERE username = :username AND user_id != :userId", nativeQuery = true)
    Optional<User> findByNameAndUserIdNot(@Param("username") String username, @Param("userId") Long userId);
}

package com.example.news_feed.user.repository;

import com.example.news_feed.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RepositoryDefinition(domainClass =  User.class, idClass = Long.class)
public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom {

    @Query(value="select * from member where username = :username", nativeQuery = true)
    Optional<User> findByName(@Param("username") String username);

    @Query(value="select * from member where email = :email", nativeQuery = true)
    Optional<User> findByEmail(@Param("email") String email);

    @Query(value = "SELECT * FROM member WHERE username = :username AND user_id != :userId", nativeQuery = true)
    Optional<User> findByNameAndUserIdNot(@Param("username") String username, @Param("userId") Long userId);

    @Query(value = "SELECT * FROM member WHERE status = 'Y'", nativeQuery = true)
    Page<User> showAllUser(Pageable pageable);

    Page<User> findByUserIdIn(List<Long> followingIds, Pageable pageable);
}

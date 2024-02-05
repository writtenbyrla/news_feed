package com.example.news_feed.user.repository;

import com.example.news_feed.user.domain.AuthHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistoryRepository extends JpaRepository<AuthHistory, Long> {
    @Query(value="select * from history where user_id = :userId order by updated_at desc limit 0, 3", nativeQuery = true)
    List<AuthHistory> findByUserId(@Param("userId") Long userId);

}

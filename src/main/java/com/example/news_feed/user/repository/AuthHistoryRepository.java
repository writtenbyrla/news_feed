package com.example.news_feed.user.repository;

import com.example.news_feed.user.domain.PwdHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthHistoryRepository extends JpaRepository<PwdHistory, Long> {
    @Query(value="select * from pwd_history where user_id = :userId order by modified_at desc limit 0, 3", nativeQuery = true)
    List<PwdHistory> findByUserId(@Param("userId") Long userId);

}

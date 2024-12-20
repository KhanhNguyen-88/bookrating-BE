package com.buihuuduy.book_rating.repository;

import com.buihuuduy.book_rating.DTO.response.DailyAccessChart;
import com.buihuuduy.book_rating.entity.LoginHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface LoginHistoryRepository extends JpaRepository<LoginHistoryEntity, Long>
{
    @Query("SELECT new com.buihuuduy.book_rating.DTO.response.DailyAccessChart(l.loginTime, COUNT(l.username)) " +
            "FROM LoginHistoryEntity l " +
            "WHERE l.loginTime >= :startOfWeek AND l.loginTime < :endOfWeek " +
            "GROUP BY l.loginTime " +
            "ORDER BY l.loginTime ASC")
    List<DailyAccessChart> countDailyAccessesInCurrentWeek(
            @Param("startOfWeek") LocalDate startOfWeek,
            @Param("endOfWeek") LocalDate endOfWeek
    );
}

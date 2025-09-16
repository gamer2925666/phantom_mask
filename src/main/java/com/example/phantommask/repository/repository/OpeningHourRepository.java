package com.example.phantommask.repository.repository;

import com.example.phantommask.repository.entity.OpeningHour;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.List;

@Repository
public interface OpeningHourRepository extends JpaRepository<OpeningHour, Long> {
    /**
     * 查詢指定時間與星期幾的營業時間，包含跨日營業的情況
     * @param dayOfWeek 星期幾 (1-7)
     * @param prevDayOfWeek 前一天的星期幾 (1-7)
     * @param time 指定時間
     * @return 營業時間列表
     */
    @Query("SELECT o FROM OpeningHour o " +
            "WHERE " +
            "(o.daysOfWeek = :dayOfWeek AND o.overnight = false AND :time BETWEEN o.openTime AND o.closeTime) OR " +
            "(o.daysOfWeek = :prevDayOfWeek AND o.overnight = true AND :time <= o.closeTime)")
    List<OpeningHour> findAllByDayAndTimeWithOvernight(
            @Param("dayOfWeek") Integer dayOfWeek,
            @Param("prevDayOfWeek") Integer prevDayOfWeek,
            @Param("time") LocalTime time
    );
}

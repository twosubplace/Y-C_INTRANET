package com.ync.schedule.mapper;

import com.ync.schedule.domain.Event;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface EventMapper {

    List<Event> findAll();

    Event findById(@Param("id") Long id);

    List<Event> findByMemberId(@Param("memberId") Long memberId);

    List<Event> findByDateRange(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    List<Event> findByMemberIdAndDateRange(
        @Param("memberId") Long memberId,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate
    );

    int insert(Event event);

    int update(Event event);

    int deleteById(@Param("id") Long id);
}

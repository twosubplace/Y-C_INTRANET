package com.ync.schedule.mapper;

import com.ync.schedule.domain.Approval;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ApprovalMapper {

    List<Approval> findAll();

    Approval findById(@Param("id") Long id);

    List<Approval> findByEventId(@Param("eventId") Long eventId);

    List<Approval> findByApproverId(@Param("approverId") Long approverId);

    List<Approval> findPendingByApproverId(@Param("approverId") Long approverId);

    Approval findByEventIdAndStepOrder(@Param("eventId") Long eventId, @Param("stepOrder") Integer stepOrder);

    int insert(Approval approval);

    int update(Approval approval);

    int deleteById(@Param("id") Long id);
}

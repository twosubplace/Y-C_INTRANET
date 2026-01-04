package com.ync.intranet.mapper;

import com.ync.intranet.domain.LeaveRequestIntranet;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

/**
 * 휴가 신청 Mapper (인트라넷)
 */
@Mapper
public interface LeaveRequestIntranetMapper {

    /**
     * ID로 휴가 신청 조회
     */
    LeaveRequestIntranet findById(@Param("id") Long id);

    /**
     * 문서 ID로 휴가 신청 조회
     */
    LeaveRequestIntranet findByDocumentId(@Param("documentId") Long documentId);

    /**
     * 휴가 유형별 조회
     */
    List<LeaveRequestIntranet> findByLeaveType(@Param("leaveType") String leaveType);

    /**
     * 기간별 휴가 조회
     */
    List<LeaveRequestIntranet> findByDateRange(@Param("startDate") LocalDate startDate,
                                                @Param("endDate") LocalDate endDate);

    /**
     * 전체 휴가 신청 조회
     */
    List<LeaveRequestIntranet> findAll();

    /**
     * 휴가 신청 등록
     */
    void insert(LeaveRequestIntranet leaveRequest);

    /**
     * 휴가 신청 수정
     */
    void update(LeaveRequestIntranet leaveRequest);

    /**
     * 휴가 신청 삭제
     */
    void deleteById(@Param("id") Long id);

    /**
     * 문서 ID로 휴가 신청 삭제
     */
    void deleteByDocumentId(@Param("documentId") Long documentId);
}

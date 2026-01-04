package com.ync.intranet.domain;

import lombok.Data;
import java.sql.Timestamp;
import java.util.Date;

/**
 * 일정/휴가 도메인
 */
@Data
public class ScheduleIntranet {
    private Long id;
    private Long memberId;
    private String scheduleType;        // VACATION, HALF_DAY, BUSINESS_TRIP, MEETING
    private String title;
    private String description;
    private Date startDate;
    private Date endDate;
    private String startTime;           // HH:MI
    private String endTime;             // HH:MI
    private Double daysUsed;
    private Long approverId;            // 결재자 ID
    private Long documentId;
    private String status;              // DRAFT, SUBMITTED, APPROVED, REJECTED, CANCELLED
    private Timestamp createdAt;
    private Timestamp updatedAt;

    // 조인용 필드
    private String memberName;
    private String memberEmail;
    private String departmentName;
}

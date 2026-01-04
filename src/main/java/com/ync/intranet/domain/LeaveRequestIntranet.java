package com.ync.intranet.domain;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 휴가 신청 (인트라넷)
 */
public class LeaveRequestIntranet {

    private Long id;
    private Long documentId;
    private String leaveType;
    private LocalDate startDate;
    private LocalDate endDate;
    private String startTime;
    private String endTime;
    private BigDecimal leaveDays;
    private String reason;
    private LocalDateTime createdAt;

    // 연관 객체
    private DocumentIntranet document;

    public LeaveRequestIntranet() {
    }

    public LeaveRequestIntranet(Long id, Long documentId, String leaveType,
                                LocalDate startDate, LocalDate endDate,
                                String startTime, String endTime,
                                BigDecimal leaveDays, String reason,
                                LocalDateTime createdAt) {
        this.id = id;
        this.documentId = documentId;
        this.leaveType = leaveType;
        this.startDate = startDate;
        this.endDate = endDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.leaveDays = leaveDays;
        this.reason = reason;
        this.createdAt = createdAt;
    }

    public static LeaveRequestIntranetBuilder builder() {
        return new LeaveRequestIntranetBuilder();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDocumentId() {
        return documentId;
    }

    public void setDocumentId(Long documentId) {
        this.documentId = documentId;
    }

    public String getLeaveType() {
        return leaveType;
    }

    public void setLeaveType(String leaveType) {
        this.leaveType = leaveType;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public BigDecimal getLeaveDays() {
        return leaveDays;
    }

    public void setLeaveDays(BigDecimal leaveDays) {
        this.leaveDays = leaveDays;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public DocumentIntranet getDocument() {
        return document;
    }

    public void setDocument(DocumentIntranet document) {
        this.document = document;
    }

    // Builder
    public static class LeaveRequestIntranetBuilder {
        private Long id;
        private Long documentId;
        private String leaveType;
        private LocalDate startDate;
        private LocalDate endDate;
        private String startTime;
        private String endTime;
        private BigDecimal leaveDays;
        private String reason;
        private LocalDateTime createdAt;

        LeaveRequestIntranetBuilder() {
        }

        public LeaveRequestIntranetBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public LeaveRequestIntranetBuilder documentId(Long documentId) {
            this.documentId = documentId;
            return this;
        }

        public LeaveRequestIntranetBuilder leaveType(String leaveType) {
            this.leaveType = leaveType;
            return this;
        }

        public LeaveRequestIntranetBuilder startDate(LocalDate startDate) {
            this.startDate = startDate;
            return this;
        }

        public LeaveRequestIntranetBuilder endDate(LocalDate endDate) {
            this.endDate = endDate;
            return this;
        }

        public LeaveRequestIntranetBuilder startTime(String startTime) {
            this.startTime = startTime;
            return this;
        }

        public LeaveRequestIntranetBuilder endTime(String endTime) {
            this.endTime = endTime;
            return this;
        }

        public LeaveRequestIntranetBuilder leaveDays(BigDecimal leaveDays) {
            this.leaveDays = leaveDays;
            return this;
        }

        public LeaveRequestIntranetBuilder reason(String reason) {
            this.reason = reason;
            return this;
        }

        public LeaveRequestIntranetBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public LeaveRequestIntranet build() {
            return new LeaveRequestIntranet(id, documentId, leaveType, startDate, endDate,
                    startTime, endTime, leaveDays, reason, createdAt);
        }
    }
}

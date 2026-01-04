package com.ync.schedule.dto;

import com.ync.schedule.domain.Approval;

import java.time.LocalDateTime;

public class ApprovalDto {
    private Long id;
    private Long eventId;
    private Integer stepOrder;
    private Long approverId;
    private String approverName;
    private Approval.ApprovalDecision decision;
    private String comment;
    private LocalDateTime submittedAt;
    private LocalDateTime decidedAt;

    public ApprovalDto() {
    }

    public ApprovalDto(Long id, Long eventId, Integer stepOrder, Long approverId, String approverName, Approval.ApprovalDecision decision, String comment, LocalDateTime submittedAt, LocalDateTime decidedAt) {
        this.id = id;
        this.eventId = eventId;
        this.stepOrder = stepOrder;
        this.approverId = approverId;
        this.approverName = approverName;
        this.decision = decision;
        this.comment = comment;
        this.submittedAt = submittedAt;
        this.decidedAt = decidedAt;
    }

    public static ApprovalDtoBuilder builder() {
        return new ApprovalDtoBuilder();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public Integer getStepOrder() {
        return stepOrder;
    }

    public void setStepOrder(Integer stepOrder) {
        this.stepOrder = stepOrder;
    }

    public Long getApproverId() {
        return approverId;
    }

    public void setApproverId(Long approverId) {
        this.approverId = approverId;
    }

    public String getApproverName() {
        return approverName;
    }

    public void setApproverName(String approverName) {
        this.approverName = approverName;
    }

    public Approval.ApprovalDecision getDecision() {
        return decision;
    }

    public void setDecision(Approval.ApprovalDecision decision) {
        this.decision = decision;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public LocalDateTime getSubmittedAt() {
        return submittedAt;
    }

    public void setSubmittedAt(LocalDateTime submittedAt) {
        this.submittedAt = submittedAt;
    }

    public LocalDateTime getDecidedAt() {
        return decidedAt;
    }

    public void setDecidedAt(LocalDateTime decidedAt) {
        this.decidedAt = decidedAt;
    }

    public static class ApprovalDtoBuilder {
        private Long id;
        private Long eventId;
        private Integer stepOrder;
        private Long approverId;
        private String approverName;
        private Approval.ApprovalDecision decision;
        private String comment;
        private LocalDateTime submittedAt;
        private LocalDateTime decidedAt;

        ApprovalDtoBuilder() {
        }

        public ApprovalDtoBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public ApprovalDtoBuilder eventId(Long eventId) {
            this.eventId = eventId;
            return this;
        }

        public ApprovalDtoBuilder stepOrder(Integer stepOrder) {
            this.stepOrder = stepOrder;
            return this;
        }

        public ApprovalDtoBuilder approverId(Long approverId) {
            this.approverId = approverId;
            return this;
        }

        public ApprovalDtoBuilder approverName(String approverName) {
            this.approverName = approverName;
            return this;
        }

        public ApprovalDtoBuilder decision(Approval.ApprovalDecision decision) {
            this.decision = decision;
            return this;
        }

        public ApprovalDtoBuilder comment(String comment) {
            this.comment = comment;
            return this;
        }

        public ApprovalDtoBuilder submittedAt(LocalDateTime submittedAt) {
            this.submittedAt = submittedAt;
            return this;
        }

        public ApprovalDtoBuilder decidedAt(LocalDateTime decidedAt) {
            this.decidedAt = decidedAt;
            return this;
        }

        public ApprovalDto build() {
            return new ApprovalDto(id, eventId, stepOrder, approverId, approverName, decision, comment, submittedAt, decidedAt);
        }
    }
}

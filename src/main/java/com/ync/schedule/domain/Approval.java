package com.ync.schedule.domain;

import java.time.LocalDateTime;

public class Approval {

    private Long id;
    private Long eventId;
    private Integer stepOrder;
    private Long approverId;
    private ApprovalDecision decision;
    private String comment;
    private LocalDateTime submittedAt;
    private LocalDateTime decidedAt;

    private Event event;
    private Member approver;

    public Approval() {
    }

    public Approval(Long id, Long eventId, Integer stepOrder, Long approverId, ApprovalDecision decision, String comment, LocalDateTime submittedAt, LocalDateTime decidedAt, Event event, Member approver) {
        this.id = id;
        this.eventId = eventId;
        this.stepOrder = stepOrder;
        this.approverId = approverId;
        this.decision = decision;
        this.comment = comment;
        this.submittedAt = submittedAt;
        this.decidedAt = decidedAt;
        this.event = event;
        this.approver = approver;
    }

    public static ApprovalBuilder builder() {
        return new ApprovalBuilder();
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

    public ApprovalDecision getDecision() {
        return decision;
    }

    public void setDecision(ApprovalDecision decision) {
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

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public Member getApprover() {
        return approver;
    }

    public void setApprover(Member approver) {
        this.approver = approver;
    }

    public enum ApprovalDecision {
        APPROVED, REJECTED
    }

    public static class ApprovalBuilder {
        private Long id;
        private Long eventId;
        private Integer stepOrder;
        private Long approverId;
        private ApprovalDecision decision;
        private String comment;
        private LocalDateTime submittedAt;
        private LocalDateTime decidedAt;
        private Event event;
        private Member approver;

        ApprovalBuilder() {
        }

        public ApprovalBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public ApprovalBuilder eventId(Long eventId) {
            this.eventId = eventId;
            return this;
        }

        public ApprovalBuilder stepOrder(Integer stepOrder) {
            this.stepOrder = stepOrder;
            return this;
        }

        public ApprovalBuilder approverId(Long approverId) {
            this.approverId = approverId;
            return this;
        }

        public ApprovalBuilder decision(ApprovalDecision decision) {
            this.decision = decision;
            return this;
        }

        public ApprovalBuilder comment(String comment) {
            this.comment = comment;
            return this;
        }

        public ApprovalBuilder submittedAt(LocalDateTime submittedAt) {
            this.submittedAt = submittedAt;
            return this;
        }

        public ApprovalBuilder decidedAt(LocalDateTime decidedAt) {
            this.decidedAt = decidedAt;
            return this;
        }

        public ApprovalBuilder event(Event event) {
            this.event = event;
            return this;
        }

        public ApprovalBuilder approver(Member approver) {
            this.approver = approver;
            return this;
        }

        public Approval build() {
            return new Approval(id, eventId, stepOrder, approverId, decision, comment, submittedAt, decidedAt, event, approver);
        }
    }
}

package com.ync.intranet.domain;

import java.time.LocalDateTime;

/**
 * 결재선 (인트라넷)
 */
public class ApprovalLineIntranet {

    private Long id;
    private Long documentId;
    private Integer stepOrder;
    private Long approverId;
    private String approverName;      // 스냅샷 (결재 당시 이름)
    private String approverPosition;  // 스냅샷 (결재 당시 직급)
    private ApprovalDecision decision;
    private String approvalComment;
    private LocalDateTime submittedAt;
    private LocalDateTime decidedAt;

    // 연관 객체
    private MemberIntranet approver;
    private DocumentIntranet document;

    public ApprovalLineIntranet() {
    }

    public ApprovalLineIntranet(Long id, Long documentId, Integer stepOrder, Long approverId,
                                String approverName, String approverPosition,
                                ApprovalDecision decision, String approvalComment,
                                LocalDateTime submittedAt, LocalDateTime decidedAt) {
        this.id = id;
        this.documentId = documentId;
        this.stepOrder = stepOrder;
        this.approverId = approverId;
        this.approverName = approverName;
        this.approverPosition = approverPosition;
        this.decision = decision;
        this.approvalComment = approvalComment;
        this.submittedAt = submittedAt;
        this.decidedAt = decidedAt;
    }

    public static ApprovalLineIntranetBuilder builder() {
        return new ApprovalLineIntranetBuilder();
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

    public String getApproverPosition() {
        return approverPosition;
    }

    public void setApproverPosition(String approverPosition) {
        this.approverPosition = approverPosition;
    }

    public ApprovalDecision getDecision() {
        return decision;
    }

    public void setDecision(ApprovalDecision decision) {
        this.decision = decision;
    }

    public String getApprovalComment() {
        return approvalComment;
    }

    public void setApprovalComment(String approvalComment) {
        this.approvalComment = approvalComment;
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

    public MemberIntranet getApprover() {
        return approver;
    }

    public void setApprover(MemberIntranet approver) {
        this.approver = approver;
    }

    public DocumentIntranet getDocument() {
        return document;
    }

    public void setDocument(DocumentIntranet document) {
        this.document = document;
    }

    // Enum
    public enum ApprovalDecision {
        PENDING, APPROVED, REJECTED
    }

    // Builder
    public static class ApprovalLineIntranetBuilder {
        private Long id;
        private Long documentId;
        private Integer stepOrder;
        private Long approverId;
        private String approverName;
        private String approverPosition;
        private ApprovalDecision decision;
        private String approvalComment;
        private LocalDateTime submittedAt;
        private LocalDateTime decidedAt;

        ApprovalLineIntranetBuilder() {
        }

        public ApprovalLineIntranetBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public ApprovalLineIntranetBuilder documentId(Long documentId) {
            this.documentId = documentId;
            return this;
        }

        public ApprovalLineIntranetBuilder stepOrder(Integer stepOrder) {
            this.stepOrder = stepOrder;
            return this;
        }

        public ApprovalLineIntranetBuilder approverId(Long approverId) {
            this.approverId = approverId;
            return this;
        }

        public ApprovalLineIntranetBuilder approverName(String approverName) {
            this.approverName = approverName;
            return this;
        }

        public ApprovalLineIntranetBuilder approverPosition(String approverPosition) {
            this.approverPosition = approverPosition;
            return this;
        }

        public ApprovalLineIntranetBuilder decision(ApprovalDecision decision) {
            this.decision = decision;
            return this;
        }

        public ApprovalLineIntranetBuilder approvalComment(String approvalComment) {
            this.approvalComment = approvalComment;
            return this;
        }

        public ApprovalLineIntranetBuilder submittedAt(LocalDateTime submittedAt) {
            this.submittedAt = submittedAt;
            return this;
        }

        public ApprovalLineIntranetBuilder decidedAt(LocalDateTime decidedAt) {
            this.decidedAt = decidedAt;
            return this;
        }

        public ApprovalLineIntranet build() {
            return new ApprovalLineIntranet(id, documentId, stepOrder, approverId,
                    approverName, approverPosition, decision, approvalComment,
                    submittedAt, decidedAt);
        }
    }
}

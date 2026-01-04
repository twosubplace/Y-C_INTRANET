package com.ync.schedule.domain;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class ExpenseReport {

    private Long id;
    private Long memberId;
    private String title;
    private LocalDate reportMonth;
    private BigDecimal totalAmount;
    private String description;
    private ExpenseStatus status;
    private String filePath;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Member member;

    public ExpenseReport() {
    }

    public ExpenseReport(Long id, Long memberId, String title, LocalDate reportMonth, BigDecimal totalAmount,
                        String description, ExpenseStatus status, String filePath,
                        LocalDateTime createdAt, LocalDateTime updatedAt, Member member) {
        this.id = id;
        this.memberId = memberId;
        this.title = title;
        this.reportMonth = reportMonth;
        this.totalAmount = totalAmount;
        this.description = description;
        this.status = status;
        this.filePath = filePath;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.member = member;
    }

    public static ExpenseReportBuilder builder() {
        return new ExpenseReportBuilder();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDate getReportMonth() {
        return reportMonth;
    }

    public void setReportMonth(LocalDate reportMonth) {
        this.reportMonth = reportMonth;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ExpenseStatus getStatus() {
        return status;
    }

    public void setStatus(ExpenseStatus status) {
        this.status = status;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public enum ExpenseStatus {
        DRAFT, SUBMITTED, APPROVED, REJECTED
    }

    public static class ExpenseReportBuilder {
        private Long id;
        private Long memberId;
        private String title;
        private LocalDate reportMonth;
        private BigDecimal totalAmount;
        private String description;
        private ExpenseStatus status;
        private String filePath;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private Member member;

        ExpenseReportBuilder() {
        }

        public ExpenseReportBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public ExpenseReportBuilder memberId(Long memberId) {
            this.memberId = memberId;
            return this;
        }

        public ExpenseReportBuilder title(String title) {
            this.title = title;
            return this;
        }

        public ExpenseReportBuilder reportMonth(LocalDate reportMonth) {
            this.reportMonth = reportMonth;
            return this;
        }

        public ExpenseReportBuilder totalAmount(BigDecimal totalAmount) {
            this.totalAmount = totalAmount;
            return this;
        }

        public ExpenseReportBuilder description(String description) {
            this.description = description;
            return this;
        }

        public ExpenseReportBuilder status(ExpenseStatus status) {
            this.status = status;
            return this;
        }

        public ExpenseReportBuilder filePath(String filePath) {
            this.filePath = filePath;
            return this;
        }

        public ExpenseReportBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public ExpenseReportBuilder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public ExpenseReportBuilder member(Member member) {
            this.member = member;
            return this;
        }

        public ExpenseReport build() {
            return new ExpenseReport(id, memberId, title, reportMonth, totalAmount, description,
                                    status, filePath, createdAt, updatedAt, member);
        }
    }
}

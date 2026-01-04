package com.ync.schedule.dto;

import com.ync.schedule.domain.ExpenseReport.ExpenseStatus;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class ExpenseReportDto {

    private Long id;
    private Long memberId;
    private String memberName;
    private String departmentName;
    private String title;
    private LocalDate reportMonth;
    private BigDecimal totalAmount;
    private String description;
    private ExpenseStatus status;
    private String filePath;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ExpenseReportDto() {
    }

    public ExpenseReportDto(Long id, Long memberId, String memberName, String departmentName,
                           String title, LocalDate reportMonth, BigDecimal totalAmount,
                           String description, ExpenseStatus status, String filePath,
                           LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.memberId = memberId;
        this.memberName = memberName;
        this.departmentName = departmentName;
        this.title = title;
        this.reportMonth = reportMonth;
        this.totalAmount = totalAmount;
        this.description = description;
        this.status = status;
        this.filePath = filePath;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static ExpenseReportDtoBuilder builder() {
        return new ExpenseReportDtoBuilder();
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

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
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

    public static class ExpenseReportDtoBuilder {
        private Long id;
        private Long memberId;
        private String memberName;
        private String departmentName;
        private String title;
        private LocalDate reportMonth;
        private BigDecimal totalAmount;
        private String description;
        private ExpenseStatus status;
        private String filePath;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        ExpenseReportDtoBuilder() {
        }

        public ExpenseReportDtoBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public ExpenseReportDtoBuilder memberId(Long memberId) {
            this.memberId = memberId;
            return this;
        }

        public ExpenseReportDtoBuilder memberName(String memberName) {
            this.memberName = memberName;
            return this;
        }

        public ExpenseReportDtoBuilder departmentName(String departmentName) {
            this.departmentName = departmentName;
            return this;
        }

        public ExpenseReportDtoBuilder title(String title) {
            this.title = title;
            return this;
        }

        public ExpenseReportDtoBuilder reportMonth(LocalDate reportMonth) {
            this.reportMonth = reportMonth;
            return this;
        }

        public ExpenseReportDtoBuilder totalAmount(BigDecimal totalAmount) {
            this.totalAmount = totalAmount;
            return this;
        }

        public ExpenseReportDtoBuilder description(String description) {
            this.description = description;
            return this;
        }

        public ExpenseReportDtoBuilder status(ExpenseStatus status) {
            this.status = status;
            return this;
        }

        public ExpenseReportDtoBuilder filePath(String filePath) {
            this.filePath = filePath;
            return this;
        }

        public ExpenseReportDtoBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public ExpenseReportDtoBuilder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public ExpenseReportDto build() {
            return new ExpenseReportDto(id, memberId, memberName, departmentName, title,
                                       reportMonth, totalAmount, description, status, filePath,
                                       createdAt, updatedAt);
        }
    }
}

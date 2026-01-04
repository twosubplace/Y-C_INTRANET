package com.ync.intranet.domain;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 경비보고서 (인트라넷)
 */
public class ExpenseReportIntranet {

    private Long id;
    private Long documentId;
    private LocalDate reportMonth;
    private BigDecimal totalAmount;
    private String description;
    private String filePath;
    private LocalDateTime createdAt;

    // 연관 객체
    private DocumentIntranet document;
    private List<ExpenseItemIntranet> items;

    public ExpenseReportIntranet() {
    }

    public ExpenseReportIntranet(Long id, Long documentId, LocalDate reportMonth,
                                 BigDecimal totalAmount, String description,
                                 String filePath, LocalDateTime createdAt) {
        this.id = id;
        this.documentId = documentId;
        this.reportMonth = reportMonth;
        this.totalAmount = totalAmount;
        this.description = description;
        this.filePath = filePath;
        this.createdAt = createdAt;
    }

    public static ExpenseReportIntranetBuilder builder() {
        return new ExpenseReportIntranetBuilder();
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

    public DocumentIntranet getDocument() {
        return document;
    }

    public void setDocument(DocumentIntranet document) {
        this.document = document;
    }

    public List<ExpenseItemIntranet> getItems() {
        return items;
    }

    public void setItems(List<ExpenseItemIntranet> items) {
        this.items = items;
    }

    // Builder
    public static class ExpenseReportIntranetBuilder {
        private Long id;
        private Long documentId;
        private LocalDate reportMonth;
        private BigDecimal totalAmount;
        private String description;
        private String filePath;
        private LocalDateTime createdAt;

        ExpenseReportIntranetBuilder() {
        }

        public ExpenseReportIntranetBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public ExpenseReportIntranetBuilder documentId(Long documentId) {
            this.documentId = documentId;
            return this;
        }

        public ExpenseReportIntranetBuilder reportMonth(LocalDate reportMonth) {
            this.reportMonth = reportMonth;
            return this;
        }

        public ExpenseReportIntranetBuilder totalAmount(BigDecimal totalAmount) {
            this.totalAmount = totalAmount;
            return this;
        }

        public ExpenseReportIntranetBuilder description(String description) {
            this.description = description;
            return this;
        }

        public ExpenseReportIntranetBuilder filePath(String filePath) {
            this.filePath = filePath;
            return this;
        }

        public ExpenseReportIntranetBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public ExpenseReportIntranet build() {
            return new ExpenseReportIntranet(id, documentId, reportMonth, totalAmount,
                    description, filePath, createdAt);
        }
    }
}

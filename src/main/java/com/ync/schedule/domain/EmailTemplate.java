package com.ync.schedule.domain;

import java.time.LocalDateTime;

public class EmailTemplate {

    private Long id;
    private String templateName;
    private String subject;
    private String content;
    private String description;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public EmailTemplate() {
    }

    public EmailTemplate(Long id, String templateName, String subject, String content,
                        String description, Boolean isActive, LocalDateTime createdAt,
                        LocalDateTime updatedAt) {
        this.id = id;
        this.templateName = templateName;
        this.subject = subject;
        this.content = content;
        this.description = description;
        this.isActive = isActive;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static EmailTemplateBuilder builder() {
        return new EmailTemplateBuilder();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
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

    public static class EmailTemplateBuilder {
        private Long id;
        private String templateName;
        private String subject;
        private String content;
        private String description;
        private Boolean isActive;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        EmailTemplateBuilder() {
        }

        public EmailTemplateBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public EmailTemplateBuilder templateName(String templateName) {
            this.templateName = templateName;
            return this;
        }

        public EmailTemplateBuilder subject(String subject) {
            this.subject = subject;
            return this;
        }

        public EmailTemplateBuilder content(String content) {
            this.content = content;
            return this;
        }

        public EmailTemplateBuilder description(String description) {
            this.description = description;
            return this;
        }

        public EmailTemplateBuilder isActive(Boolean isActive) {
            this.isActive = isActive;
            return this;
        }

        public EmailTemplateBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public EmailTemplateBuilder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public EmailTemplate build() {
            return new EmailTemplate(id, templateName, subject, content, description,
                                   isActive, createdAt, updatedAt);
        }
    }
}

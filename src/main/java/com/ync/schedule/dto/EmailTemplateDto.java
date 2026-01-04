package com.ync.schedule.dto;

public class EmailTemplateDto {
    private Long id;
    private String templateName;
    private String subject;
    private String content;
    private String description;
    private Boolean isActive;

    public EmailTemplateDto() {
    }

    public EmailTemplateDto(Long id, String templateName, String subject, String content,
                           String description, Boolean isActive) {
        this.id = id;
        this.templateName = templateName;
        this.subject = subject;
        this.content = content;
        this.description = description;
        this.isActive = isActive;
    }

    public static EmailTemplateDtoBuilder builder() {
        return new EmailTemplateDtoBuilder();
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

    public static class EmailTemplateDtoBuilder {
        private Long id;
        private String templateName;
        private String subject;
        private String content;
        private String description;
        private Boolean isActive;

        EmailTemplateDtoBuilder() {
        }

        public EmailTemplateDtoBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public EmailTemplateDtoBuilder templateName(String templateName) {
            this.templateName = templateName;
            return this;
        }

        public EmailTemplateDtoBuilder subject(String subject) {
            this.subject = subject;
            return this;
        }

        public EmailTemplateDtoBuilder content(String content) {
            this.content = content;
            return this;
        }

        public EmailTemplateDtoBuilder description(String description) {
            this.description = description;
            return this;
        }

        public EmailTemplateDtoBuilder isActive(Boolean isActive) {
            this.isActive = isActive;
            return this;
        }

        public EmailTemplateDto build() {
            return new EmailTemplateDto(id, templateName, subject, content, description, isActive);
        }
    }
}

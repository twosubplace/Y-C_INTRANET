package com.ync.intranet.domain;

import java.time.LocalDateTime;

/**
 * 공지사항 (인트라넷)
 */
public class NoticeIntranet {

    private Long id;
    private String title;
    private String content;
    private Long authorId;
    private String authorName;  // JOIN용
    private Boolean isPinned;
    private Integer viewCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // 연관 객체
    private MemberIntranet author;

    public NoticeIntranet() {
    }

    public NoticeIntranet(Long id, String title, String content, Long authorId,
                          String authorName, Boolean isPinned, Integer viewCount,
                          LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.authorId = authorId;
        this.authorName = authorName;
        this.isPinned = isPinned;
        this.viewCount = viewCount;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static NoticeIntranetBuilder builder() {
        return new NoticeIntranetBuilder();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public Boolean getIsPinned() {
        return isPinned;
    }

    public void setIsPinned(Boolean isPinned) {
        this.isPinned = isPinned;
    }

    public Integer getViewCount() {
        return viewCount;
    }

    public void setViewCount(Integer viewCount) {
        this.viewCount = viewCount;
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

    public MemberIntranet getAuthor() {
        return author;
    }

    public void setAuthor(MemberIntranet author) {
        this.author = author;
    }

    // Builder
    public static class NoticeIntranetBuilder {
        private Long id;
        private String title;
        private String content;
        private Long authorId;
        private String authorName;
        private Boolean isPinned;
        private Integer viewCount;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        NoticeIntranetBuilder() {
        }

        public NoticeIntranetBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public NoticeIntranetBuilder title(String title) {
            this.title = title;
            return this;
        }

        public NoticeIntranetBuilder content(String content) {
            this.content = content;
            return this;
        }

        public NoticeIntranetBuilder authorId(Long authorId) {
            this.authorId = authorId;
            return this;
        }

        public NoticeIntranetBuilder authorName(String authorName) {
            this.authorName = authorName;
            return this;
        }

        public NoticeIntranetBuilder isPinned(Boolean isPinned) {
            this.isPinned = isPinned;
            return this;
        }

        public NoticeIntranetBuilder viewCount(Integer viewCount) {
            this.viewCount = viewCount;
            return this;
        }

        public NoticeIntranetBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public NoticeIntranetBuilder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public NoticeIntranet build() {
            return new NoticeIntranet(id, title, content, authorId, authorName,
                    isPinned, viewCount, createdAt, updatedAt);
        }
    }
}

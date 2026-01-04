package com.ync.intranet.domain;

import java.time.LocalDateTime;

/**
 * 부서 정보 (인트라넷)
 */
public class DepartmentIntranet {

    private Long id;
    private String name;
    private Long parentId;
    private Long managerId;
    private String managerName;  // JOIN용
    private Integer displayOrder;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public DepartmentIntranet() {
    }

    public DepartmentIntranet(Long id, String name, Long parentId, Long managerId,
                              String managerName, Integer displayOrder, Boolean isActive,
                              LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.parentId = parentId;
        this.managerId = managerId;
        this.managerName = managerName;
        this.displayOrder = displayOrder;
        this.isActive = isActive;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static DepartmentIntranetBuilder builder() {
        return new DepartmentIntranetBuilder();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Long getManagerId() {
        return managerId;
    }

    public void setManagerId(Long managerId) {
        this.managerId = managerId;
    }

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
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

    // Builder
    public static class DepartmentIntranetBuilder {
        private Long id;
        private String name;
        private Long parentId;
        private Long managerId;
        private String managerName;
        private Integer displayOrder;
        private Boolean isActive;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        DepartmentIntranetBuilder() {
        }

        public DepartmentIntranetBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public DepartmentIntranetBuilder name(String name) {
            this.name = name;
            return this;
        }

        public DepartmentIntranetBuilder parentId(Long parentId) {
            this.parentId = parentId;
            return this;
        }

        public DepartmentIntranetBuilder managerId(Long managerId) {
            this.managerId = managerId;
            return this;
        }

        public DepartmentIntranetBuilder managerName(String managerName) {
            this.managerName = managerName;
            return this;
        }

        public DepartmentIntranetBuilder displayOrder(Integer displayOrder) {
            this.displayOrder = displayOrder;
            return this;
        }

        public DepartmentIntranetBuilder isActive(Boolean isActive) {
            this.isActive = isActive;
            return this;
        }

        public DepartmentIntranetBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public DepartmentIntranetBuilder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public DepartmentIntranet build() {
            return new DepartmentIntranet(id, name, parentId, managerId, managerName,
                    displayOrder, isActive, createdAt, updatedAt);
        }
    }
}

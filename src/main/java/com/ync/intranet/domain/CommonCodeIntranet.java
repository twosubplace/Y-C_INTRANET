package com.ync.intranet.domain;

/**
 * 공통 코드 (인트라넷)
 */
public class CommonCodeIntranet {

    private String codeType;
    private String code;
    private String name;
    private Integer displayOrder;
    private Boolean isActive;
    private String description;

    public CommonCodeIntranet() {
    }

    public CommonCodeIntranet(String codeType, String code, String name,
                              Integer displayOrder, Boolean isActive, String description) {
        this.codeType = codeType;
        this.code = code;
        this.name = name;
        this.displayOrder = displayOrder;
        this.isActive = isActive;
        this.description = description;
    }

    public static CommonCodeIntranetBuilder builder() {
        return new CommonCodeIntranetBuilder();
    }

    // Getters and Setters
    public String getCodeType() {
        return codeType;
    }

    public void setCodeType(String codeType) {
        this.codeType = codeType;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // Builder
    public static class CommonCodeIntranetBuilder {
        private String codeType;
        private String code;
        private String name;
        private Integer displayOrder;
        private Boolean isActive;
        private String description;

        CommonCodeIntranetBuilder() {
        }

        public CommonCodeIntranetBuilder codeType(String codeType) {
            this.codeType = codeType;
            return this;
        }

        public CommonCodeIntranetBuilder code(String code) {
            this.code = code;
            return this;
        }

        public CommonCodeIntranetBuilder name(String name) {
            this.name = name;
            return this;
        }

        public CommonCodeIntranetBuilder displayOrder(Integer displayOrder) {
            this.displayOrder = displayOrder;
            return this;
        }

        public CommonCodeIntranetBuilder isActive(Boolean isActive) {
            this.isActive = isActive;
            return this;
        }

        public CommonCodeIntranetBuilder description(String description) {
            this.description = description;
            return this;
        }

        public CommonCodeIntranet build() {
            return new CommonCodeIntranet(codeType, code, name, displayOrder, isActive, description);
        }
    }
}

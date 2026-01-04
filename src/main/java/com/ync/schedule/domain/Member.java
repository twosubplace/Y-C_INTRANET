package com.ync.schedule.domain;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Member {

    private Long id;
    private String name;
    private String email;
    private String phone;
    private Long departmentId;
    private String department; // JOIN 결과용 (부서명 표시)
    private String position;
    private LocalDate hireDate; // 입사일
    private BigDecimal annualLeaveGranted;
    private Boolean isActive;
    private String smtpPassword; // SMTP 비밀번호
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Member() {
    }

    public Member(Long id, String name, String email, String phone, Long departmentId, String department, String position, LocalDate hireDate, BigDecimal annualLeaveGranted, Boolean isActive, String smtpPassword, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.departmentId = departmentId;
        this.department = department;
        this.position = position;
        this.hireDate = hireDate;
        this.annualLeaveGranted = annualLeaveGranted;
        this.isActive = isActive;
        this.smtpPassword = smtpPassword;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static MemberBuilder builder() {
        return new MemberBuilder();
    }

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public LocalDate getHireDate() {
        return hireDate;
    }

    public void setHireDate(LocalDate hireDate) {
        this.hireDate = hireDate;
    }

    public BigDecimal getAnnualLeaveGranted() {
        return annualLeaveGranted;
    }

    public void setAnnualLeaveGranted(BigDecimal annualLeaveGranted) {
        this.annualLeaveGranted = annualLeaveGranted;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public String getSmtpPassword() {
        return smtpPassword;
    }

    public void setSmtpPassword(String smtpPassword) {
        this.smtpPassword = smtpPassword;
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

    public static class MemberBuilder {
        private Long id;
        private String name;
        private String email;
        private String phone;
        private Long departmentId;
        private String department;
        private String position;
        private LocalDate hireDate;
        private BigDecimal annualLeaveGranted;
        private Boolean isActive;
        private String smtpPassword;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        MemberBuilder() {
        }

        public MemberBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public MemberBuilder name(String name) {
            this.name = name;
            return this;
        }

        public MemberBuilder email(String email) {
            this.email = email;
            return this;
        }

        public MemberBuilder phone(String phone) {
            this.phone = phone;
            return this;
        }

        public MemberBuilder departmentId(Long departmentId) {
            this.departmentId = departmentId;
            return this;
        }

        public MemberBuilder department(String department) {
            this.department = department;
            return this;
        }

        public MemberBuilder position(String position) {
            this.position = position;
            return this;
        }

        public MemberBuilder hireDate(LocalDate hireDate) {
            this.hireDate = hireDate;
            return this;
        }

        public MemberBuilder annualLeaveGranted(BigDecimal annualLeaveGranted) {
            this.annualLeaveGranted = annualLeaveGranted;
            return this;
        }

        public MemberBuilder isActive(Boolean isActive) {
            this.isActive = isActive;
            return this;
        }

        public MemberBuilder smtpPassword(String smtpPassword) {
            this.smtpPassword = smtpPassword;
            return this;
        }

        public MemberBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public MemberBuilder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public Member build() {
            return new Member(id, name, email, phone, departmentId, department, position, hireDate, annualLeaveGranted, isActive, smtpPassword, createdAt, updatedAt);
        }
    }
}

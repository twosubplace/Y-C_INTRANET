package com.ync.schedule.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class MemberDto {
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

    public MemberDto() {
    }

    public MemberDto(Long id, String name, String email, String phone, Long departmentId, String department, String position, LocalDate hireDate, BigDecimal annualLeaveGranted, Boolean isActive, String smtpPassword) {
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
    }

    public static MemberDtoBuilder builder() {
        return new MemberDtoBuilder();
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

    public static class MemberDtoBuilder {
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

        MemberDtoBuilder() {
        }

        public MemberDtoBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public MemberDtoBuilder name(String name) {
            this.name = name;
            return this;
        }

        public MemberDtoBuilder email(String email) {
            this.email = email;
            return this;
        }

        public MemberDtoBuilder phone(String phone) {
            this.phone = phone;
            return this;
        }

        public MemberDtoBuilder departmentId(Long departmentId) {
            this.departmentId = departmentId;
            return this;
        }

        public MemberDtoBuilder department(String department) {
            this.department = department;
            return this;
        }

        public MemberDtoBuilder position(String position) {
            this.position = position;
            return this;
        }

        public MemberDtoBuilder hireDate(LocalDate hireDate) {
            this.hireDate = hireDate;
            return this;
        }

        public MemberDtoBuilder annualLeaveGranted(BigDecimal annualLeaveGranted) {
            this.annualLeaveGranted = annualLeaveGranted;
            return this;
        }

        public MemberDtoBuilder isActive(Boolean isActive) {
            this.isActive = isActive;
            return this;
        }

        public MemberDtoBuilder smtpPassword(String smtpPassword) {
            this.smtpPassword = smtpPassword;
            return this;
        }

        public MemberDto build() {
            return new MemberDto(id, name, email, phone, departmentId, department, position, hireDate, annualLeaveGranted, isActive, smtpPassword);
        }
    }
}

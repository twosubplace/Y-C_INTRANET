package com.ync.schedule.dto;

import java.math.BigDecimal;

public class LeaveBalanceDto {
    private Long memberId;
    private String memberName;
    private String department;
    private String position;
    private BigDecimal annualLeaveGranted;
    private BigDecimal usedLeave;
    private BigDecimal remainingLeave;

    public LeaveBalanceDto() {
    }

    public LeaveBalanceDto(Long memberId, String memberName, String department, String position, BigDecimal annualLeaveGranted, BigDecimal usedLeave, BigDecimal remainingLeave) {
        this.memberId = memberId;
        this.memberName = memberName;
        this.department = department;
        this.position = position;
        this.annualLeaveGranted = annualLeaveGranted;
        this.usedLeave = usedLeave;
        this.remainingLeave = remainingLeave;
    }

    public static LeaveBalanceDtoBuilder builder() {
        return new LeaveBalanceDtoBuilder();
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

    public BigDecimal getAnnualLeaveGranted() {
        return annualLeaveGranted;
    }

    public void setAnnualLeaveGranted(BigDecimal annualLeaveGranted) {
        this.annualLeaveGranted = annualLeaveGranted;
    }

    public BigDecimal getUsedLeave() {
        return usedLeave;
    }

    public void setUsedLeave(BigDecimal usedLeave) {
        this.usedLeave = usedLeave;
    }

    public BigDecimal getRemainingLeave() {
        return remainingLeave;
    }

    public void setRemainingLeave(BigDecimal remainingLeave) {
        this.remainingLeave = remainingLeave;
    }

    public static class LeaveBalanceDtoBuilder {
        private Long memberId;
        private String memberName;
        private String department;
        private String position;
        private BigDecimal annualLeaveGranted;
        private BigDecimal usedLeave;
        private BigDecimal remainingLeave;

        LeaveBalanceDtoBuilder() {
        }

        public LeaveBalanceDtoBuilder memberId(Long memberId) {
            this.memberId = memberId;
            return this;
        }

        public LeaveBalanceDtoBuilder memberName(String memberName) {
            this.memberName = memberName;
            return this;
        }

        public LeaveBalanceDtoBuilder department(String department) {
            this.department = department;
            return this;
        }

        public LeaveBalanceDtoBuilder position(String position) {
            this.position = position;
            return this;
        }

        public LeaveBalanceDtoBuilder annualLeaveGranted(BigDecimal annualLeaveGranted) {
            this.annualLeaveGranted = annualLeaveGranted;
            return this;
        }

        public LeaveBalanceDtoBuilder usedLeave(BigDecimal usedLeave) {
            this.usedLeave = usedLeave;
            return this;
        }

        public LeaveBalanceDtoBuilder remainingLeave(BigDecimal remainingLeave) {
            this.remainingLeave = remainingLeave;
            return this;
        }

        public LeaveBalanceDto build() {
            return new LeaveBalanceDto(memberId, memberName, department, position, annualLeaveGranted, usedLeave, remainingLeave);
        }
    }
}

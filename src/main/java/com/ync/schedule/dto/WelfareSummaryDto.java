package com.ync.schedule.dto;

import java.math.BigDecimal;
import java.util.List;

public class WelfareSummaryDto {

    private Long memberId;
    private String memberName;
    private Integer year;
    private List<WelfareUsageDto> quarters;
    private BigDecimal annualBudget;
    private BigDecimal annualUsed;
    private BigDecimal annualRemaining;

    public WelfareSummaryDto() {
    }

    public WelfareSummaryDto(Long memberId, String memberName, Integer year, List<WelfareUsageDto> quarters,
                            BigDecimal annualBudget, BigDecimal annualUsed, BigDecimal annualRemaining) {
        this.memberId = memberId;
        this.memberName = memberName;
        this.year = year;
        this.quarters = quarters;
        this.annualBudget = annualBudget;
        this.annualUsed = annualUsed;
        this.annualRemaining = annualRemaining;
    }

    public static WelfareSummaryDtoBuilder builder() {
        return new WelfareSummaryDtoBuilder();
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

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public List<WelfareUsageDto> getQuarters() {
        return quarters;
    }

    public void setQuarters(List<WelfareUsageDto> quarters) {
        this.quarters = quarters;
    }

    public BigDecimal getAnnualBudget() {
        return annualBudget;
    }

    public void setAnnualBudget(BigDecimal annualBudget) {
        this.annualBudget = annualBudget;
    }

    public BigDecimal getAnnualUsed() {
        return annualUsed;
    }

    public void setAnnualUsed(BigDecimal annualUsed) {
        this.annualUsed = annualUsed;
    }

    public BigDecimal getAnnualRemaining() {
        return annualRemaining;
    }

    public void setAnnualRemaining(BigDecimal annualRemaining) {
        this.annualRemaining = annualRemaining;
    }

    public static class WelfareSummaryDtoBuilder {
        private Long memberId;
        private String memberName;
        private Integer year;
        private List<WelfareUsageDto> quarters;
        private BigDecimal annualBudget;
        private BigDecimal annualUsed;
        private BigDecimal annualRemaining;

        WelfareSummaryDtoBuilder() {
        }

        public WelfareSummaryDtoBuilder memberId(Long memberId) {
            this.memberId = memberId;
            return this;
        }

        public WelfareSummaryDtoBuilder memberName(String memberName) {
            this.memberName = memberName;
            return this;
        }

        public WelfareSummaryDtoBuilder year(Integer year) {
            this.year = year;
            return this;
        }

        public WelfareSummaryDtoBuilder quarters(List<WelfareUsageDto> quarters) {
            this.quarters = quarters;
            return this;
        }

        public WelfareSummaryDtoBuilder annualBudget(BigDecimal annualBudget) {
            this.annualBudget = annualBudget;
            return this;
        }

        public WelfareSummaryDtoBuilder annualUsed(BigDecimal annualUsed) {
            this.annualUsed = annualUsed;
            return this;
        }

        public WelfareSummaryDtoBuilder annualRemaining(BigDecimal annualRemaining) {
            this.annualRemaining = annualRemaining;
            return this;
        }

        public WelfareSummaryDto build() {
            return new WelfareSummaryDto(memberId, memberName, year, quarters, annualBudget, annualUsed, annualRemaining);
        }
    }
}

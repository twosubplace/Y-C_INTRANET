package com.ync.intranet.dto;

import java.math.BigDecimal;

public class WelfareUsageDto {

    private Integer year;
    private Integer quarter;
    private BigDecimal budget;
    private BigDecimal used;
    private BigDecimal remaining;

    public WelfareUsageDto() {
    }

    public WelfareUsageDto(Integer year, Integer quarter, BigDecimal budget, BigDecimal used, BigDecimal remaining) {
        this.year = year;
        this.quarter = quarter;
        this.budget = budget;
        this.used = used;
        this.remaining = remaining;
    }

    public static WelfareUsageDtoBuilder builder() {
        return new WelfareUsageDtoBuilder();
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getQuarter() {
        return quarter;
    }

    public void setQuarter(Integer quarter) {
        this.quarter = quarter;
    }

    public BigDecimal getBudget() {
        return budget;
    }

    public void setBudget(BigDecimal budget) {
        this.budget = budget;
    }

    public BigDecimal getUsed() {
        return used;
    }

    public void setUsed(BigDecimal used) {
        this.used = used;
    }

    public BigDecimal getRemaining() {
        return remaining;
    }

    public void setRemaining(BigDecimal remaining) {
        this.remaining = remaining;
    }

    public static class WelfareUsageDtoBuilder {
        private Integer year;
        private Integer quarter;
        private BigDecimal budget;
        private BigDecimal used;
        private BigDecimal remaining;

        WelfareUsageDtoBuilder() {
        }

        public WelfareUsageDtoBuilder year(Integer year) {
            this.year = year;
            return this;
        }

        public WelfareUsageDtoBuilder quarter(Integer quarter) {
            this.quarter = quarter;
            return this;
        }

        public WelfareUsageDtoBuilder budget(BigDecimal budget) {
            this.budget = budget;
            return this;
        }

        public WelfareUsageDtoBuilder used(BigDecimal used) {
            this.used = used;
            return this;
        }

        public WelfareUsageDtoBuilder remaining(BigDecimal remaining) {
            this.remaining = remaining;
            return this;
        }

        public WelfareUsageDto build() {
            return new WelfareUsageDto(year, quarter, budget, used, remaining);
        }
    }
}

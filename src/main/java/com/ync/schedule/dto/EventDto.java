package com.ync.schedule.dto;

import com.ync.schedule.domain.Event;

import java.math.BigDecimal;
import java.time.LocalDate;

public class EventDto {
    private Long id;
    private Long memberId;
    private String memberName;
    private Event.EventType eventType;
    private String eventSubtype;
    private LocalDate startDate;
    private LocalDate endDate;
    private String startTime;
    private String endTime;
    private BigDecimal leaveAmount;
    private String title;
    private String description;
    private Event.EventStatus status;
    private String department;
    private String position;
    private String division;

    public EventDto() {
    }

    public EventDto(Long id, Long memberId, String memberName, Event.EventType eventType, String eventSubtype, LocalDate startDate, LocalDate endDate, String startTime, String endTime, BigDecimal leaveAmount, String title, String description, Event.EventStatus status, String department, String position, String division) {
        this.id = id;
        this.memberId = memberId;
        this.memberName = memberName;
        this.eventType = eventType;
        this.eventSubtype = eventSubtype;
        this.startDate = startDate;
        this.endDate = endDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.leaveAmount = leaveAmount;
        this.title = title;
        this.description = description;
        this.status = status;
        this.department = department;
        this.position = position;
        this.division = division;
    }

    public static EventDtoBuilder builder() {
        return new EventDtoBuilder();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Event.EventType getEventType() {
        return eventType;
    }

    public void setEventType(Event.EventType eventType) {
        this.eventType = eventType;
    }

    public String getEventSubtype() {
        return eventSubtype;
    }

    public void setEventSubtype(String eventSubtype) {
        this.eventSubtype = eventSubtype;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public BigDecimal getLeaveAmount() {
        return leaveAmount;
    }

    public void setLeaveAmount(BigDecimal leaveAmount) {
        this.leaveAmount = leaveAmount;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Event.EventStatus getStatus() {
        return status;
    }

    public void setStatus(Event.EventStatus status) {
        this.status = status;
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

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public static class EventDtoBuilder {
        private Long id;
        private Long memberId;
        private String memberName;
        private Event.EventType eventType;
        private String eventSubtype;
        private LocalDate startDate;
        private LocalDate endDate;
        private String startTime;
        private String endTime;
        private BigDecimal leaveAmount;
        private String title;
        private String description;
        private Event.EventStatus status;
        private String department;
        private String position;
        private String division;

        EventDtoBuilder() {
        }

        public EventDtoBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public EventDtoBuilder memberId(Long memberId) {
            this.memberId = memberId;
            return this;
        }

        public EventDtoBuilder memberName(String memberName) {
            this.memberName = memberName;
            return this;
        }

        public EventDtoBuilder eventType(Event.EventType eventType) {
            this.eventType = eventType;
            return this;
        }

        public EventDtoBuilder eventSubtype(String eventSubtype) {
            this.eventSubtype = eventSubtype;
            return this;
        }

        public EventDtoBuilder startDate(LocalDate startDate) {
            this.startDate = startDate;
            return this;
        }

        public EventDtoBuilder endDate(LocalDate endDate) {
            this.endDate = endDate;
            return this;
        }

        public EventDtoBuilder startTime(String startTime) {
            this.startTime = startTime;
            return this;
        }

        public EventDtoBuilder endTime(String endTime) {
            this.endTime = endTime;
            return this;
        }

        public EventDtoBuilder leaveAmount(BigDecimal leaveAmount) {
            this.leaveAmount = leaveAmount;
            return this;
        }

        public EventDtoBuilder title(String title) {
            this.title = title;
            return this;
        }

        public EventDtoBuilder description(String description) {
            this.description = description;
            return this;
        }

        public EventDtoBuilder status(Event.EventStatus status) {
            this.status = status;
            return this;
        }

        public EventDtoBuilder department(String department) {
            this.department = department;
            return this;
        }

        public EventDtoBuilder position(String position) {
            this.position = position;
            return this;
        }

        public EventDtoBuilder division(String division) {
            this.division = division;
            return this;
        }

        public EventDto build() {
            return new EventDto(id, memberId, memberName, eventType, eventSubtype, startDate, endDate, startTime, endTime, leaveAmount, title, description, status, department, position, division);
        }
    }
}

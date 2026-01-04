package com.ync.schedule.domain;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Event {

    private Long id;
    private Long memberId;
    private EventType eventType;
    private String eventSubtype;
    private LocalDate startDate;
    private LocalDate endDate;
    private String startTime;
    private String endTime;
    private BigDecimal leaveAmount;
    private String title;
    private String description;
    private EventStatus status;
    private String department;
    private String position;
    private String division;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Member member;

    public Event() {
    }

    public Event(Long id, Long memberId, EventType eventType, String eventSubtype, LocalDate startDate, LocalDate endDate, String startTime, String endTime, BigDecimal leaveAmount, String title, String description, EventStatus status, String department, String position, String division, LocalDateTime createdAt, LocalDateTime updatedAt, Member member) {
        this.id = id;
        this.memberId = memberId;
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
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.member = member;
    }

    public static EventBuilder builder() {
        return new EventBuilder();
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

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
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

    public EventStatus getStatus() {
        return status;
    }

    public void setStatus(EventStatus status) {
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

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public enum EventType {
        LEAVE, SCHEDULE
    }

    public enum EventStatus {
        DRAFT, SUBMITTED, APPROVED, REJECTED, CANCELED
    }

    public static class EventBuilder {
        private Long id;
        private Long memberId;
        private EventType eventType;
        private String eventSubtype;
        private LocalDate startDate;
        private LocalDate endDate;
        private String startTime;
        private String endTime;
        private BigDecimal leaveAmount;
        private String title;
        private String description;
        private EventStatus status;
        private String department;
        private String position;
        private String division;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private Member member;

        EventBuilder() {
        }

        public EventBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public EventBuilder memberId(Long memberId) {
            this.memberId = memberId;
            return this;
        }

        public EventBuilder eventType(EventType eventType) {
            this.eventType = eventType;
            return this;
        }

        public EventBuilder eventSubtype(String eventSubtype) {
            this.eventSubtype = eventSubtype;
            return this;
        }

        public EventBuilder startDate(LocalDate startDate) {
            this.startDate = startDate;
            return this;
        }

        public EventBuilder endDate(LocalDate endDate) {
            this.endDate = endDate;
            return this;
        }

        public EventBuilder startTime(String startTime) {
            this.startTime = startTime;
            return this;
        }

        public EventBuilder endTime(String endTime) {
            this.endTime = endTime;
            return this;
        }

        public EventBuilder leaveAmount(BigDecimal leaveAmount) {
            this.leaveAmount = leaveAmount;
            return this;
        }

        public EventBuilder title(String title) {
            this.title = title;
            return this;
        }

        public EventBuilder description(String description) {
            this.description = description;
            return this;
        }

        public EventBuilder status(EventStatus status) {
            this.status = status;
            return this;
        }

        public EventBuilder department(String department) {
            this.department = department;
            return this;
        }

        public EventBuilder position(String position) {
            this.position = position;
            return this;
        }

        public EventBuilder division(String division) {
            this.division = division;
            return this;
        }

        public EventBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public EventBuilder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public EventBuilder member(Member member) {
            this.member = member;
            return this;
        }

        public Event build() {
            return new Event(id, memberId, eventType, eventSubtype, startDate, endDate, startTime, endTime, leaveAmount, title, description, status, department, position, division, createdAt, updatedAt, member);
        }
    }
}

-- Team Leave & Schedule Management System
-- Database Schema for Oracle

-- Drop tables if exists (for clean reinstall)
DROP TABLE approvals CASCADE CONSTRAINTS;
DROP TABLE events CASCADE CONSTRAINTS;
DROP TABLE members CASCADE CONSTRAINTS;

-- Drop sequences if exists
DROP SEQUENCE members_seq;
DROP SEQUENCE events_seq;
DROP SEQUENCE approvals_seq;

-- Create sequences for auto-increment
CREATE SEQUENCE members_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE events_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE approvals_seq START WITH 1 INCREMENT BY 1;

-- Table: members
-- Stores team member information
CREATE TABLE members (
    id NUMBER(19) PRIMARY KEY,
    name VARCHAR2(100) NOT NULL,
    department VARCHAR2(100) NOT NULL,
    position VARCHAR2(100) NOT NULL,
    annual_leave_granted NUMBER(4, 1) DEFAULT 15.0 NOT NULL,
    is_active NUMBER(1) DEFAULT 1 NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT check_annual_leave_positive CHECK (annual_leave_granted >= 0),
    CONSTRAINT check_is_active CHECK (is_active IN (0, 1))
);

-- Table: events
-- Unified table for both leave and schedule events
CREATE TABLE events (
    id NUMBER(19) PRIMARY KEY,
    member_id NUMBER(19) NOT NULL,
    event_type VARCHAR2(20) NOT NULL,
    event_subtype VARCHAR2(50) NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    leave_amount NUMBER(3, 1) DEFAULT 0.0 NOT NULL,
    title VARCHAR2(200) NOT NULL,
    description CLOB,
    status VARCHAR2(20) DEFAULT 'DRAFT' NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT fk_events_member FOREIGN KEY (member_id) REFERENCES members(id) ON DELETE CASCADE,
    CONSTRAINT check_event_type CHECK (event_type IN ('LEAVE', 'SCHEDULE')),
    CONSTRAINT check_event_subtype CHECK (event_subtype IN ('연차', '반차-오전', '반차-오후', '병가', '회의', '외근', '개인일정')),
    CONSTRAINT check_status CHECK (status IN ('DRAFT', 'SUBMITTED', 'APPROVED', 'REJECTED', 'CANCELED')),
    CONSTRAINT check_leave_amount CHECK (leave_amount >= 0 AND leave_amount <= 365),
    CONSTRAINT check_date_order CHECK (end_date >= start_date)
);

-- Table: approvals
-- Stores approval workflow history
CREATE TABLE approvals (
    id NUMBER(19) PRIMARY KEY,
    event_id NUMBER(19) NOT NULL,
    step_order NUMBER(10) NOT NULL,
    approver_id NUMBER(19) NOT NULL,
    decision VARCHAR2(20),
    comment CLOB,
    submitted_at TIMESTAMP,
    decided_at TIMESTAMP,
    CONSTRAINT fk_approvals_event FOREIGN KEY (event_id) REFERENCES events(id) ON DELETE CASCADE,
    CONSTRAINT fk_approvals_approver FOREIGN KEY (approver_id) REFERENCES members(id),
    CONSTRAINT check_decision CHECK (decision IS NULL OR decision IN ('APPROVED', 'REJECTED')),
    CONSTRAINT unique_event_step UNIQUE (event_id, step_order)
);

-- Indexes for performance optimization
CREATE INDEX idx_members_active ON members(is_active);
CREATE INDEX idx_members_department ON members(department);

CREATE INDEX idx_events_member ON events(member_id);
CREATE INDEX idx_events_type ON events(event_type);
CREATE INDEX idx_events_status ON events(status);
CREATE INDEX idx_events_dates ON events(start_date, end_date);
CREATE INDEX idx_events_member_dates ON events(member_id, start_date, end_date);

CREATE INDEX idx_approvals_event ON approvals(event_id);
CREATE INDEX idx_approvals_approver ON approvals(approver_id);
CREATE INDEX idx_approvals_decision ON approvals(decision);

-- Triggers for auto-increment using sequences
CREATE OR REPLACE TRIGGER members_bir
BEFORE INSERT ON members
FOR EACH ROW
BEGIN
    IF :new.id IS NULL THEN
        SELECT members_seq.NEXTVAL INTO :new.id FROM dual;
    END IF;
END;
/

CREATE OR REPLACE TRIGGER events_bir
BEFORE INSERT ON events
FOR EACH ROW
BEGIN
    IF :new.id IS NULL THEN
        SELECT events_seq.NEXTVAL INTO :new.id FROM dual;
    END IF;
END;
/

CREATE OR REPLACE TRIGGER approvals_bir
BEFORE INSERT ON approvals
FOR EACH ROW
BEGIN
    IF :new.id IS NULL THEN
        SELECT approvals_seq.NEXTVAL INTO :new.id FROM dual;
    END IF;
END;
/

-- Sample data for testing
INSERT INTO members (id, name, department, position, annual_leave_granted) VALUES
(members_seq.NEXTVAL, '김철수', '개발팀', '팀장', 20.0);
INSERT INTO members (id, name, department, position, annual_leave_granted) VALUES
(members_seq.NEXTVAL, '이영희', '개발팀', '선임', 18.0);
INSERT INTO members (id, name, department, position, annual_leave_granted) VALUES
(members_seq.NEXTVAL, '박민수', '기획팀', '팀장', 20.0);
INSERT INTO members (id, name, department, position, annual_leave_granted) VALUES
(members_seq.NEXTVAL, '정수진', '개발팀', '주임', 15.0);
INSERT INTO members (id, name, department, position, annual_leave_granted) VALUES
(members_seq.NEXTVAL, '최동욱', '기획팀', '대리', 16.0);

-- Sample events (assuming member IDs 1-5)
INSERT INTO events (id, member_id, event_type, event_subtype, start_date, end_date, leave_amount, title, status) VALUES
(events_seq.NEXTVAL, 1, 'LEAVE', '연차', TO_DATE('2025-12-24', 'YYYY-MM-DD'), TO_DATE('2025-12-24', 'YYYY-MM-DD'), 1.0, '연차', 'APPROVED');
INSERT INTO events (id, member_id, event_type, event_subtype, start_date, end_date, leave_amount, title, status) VALUES
(events_seq.NEXTVAL, 2, 'LEAVE', '반차-오전', TO_DATE('2025-12-23', 'YYYY-MM-DD'), TO_DATE('2025-12-23', 'YYYY-MM-DD'), 0.5, '반차(오전)', 'SUBMITTED');
INSERT INTO events (id, member_id, event_type, event_subtype, start_date, end_date, leave_amount, title, status) VALUES
(events_seq.NEXTVAL, 3, 'SCHEDULE', '회의', TO_DATE('2025-12-23', 'YYYY-MM-DD'), TO_DATE('2025-12-23', 'YYYY-MM-DD'), 0.0, '경영진 회의', 'APPROVED');
INSERT INTO events (id, member_id, event_type, event_subtype, start_date, end_date, leave_amount, title, status) VALUES
(events_seq.NEXTVAL, 4, 'LEAVE', '병가', TO_DATE('2025-12-20', 'YYYY-MM-DD'), TO_DATE('2025-12-20', 'YYYY-MM-DD'), 1.0, '병가', 'APPROVED');

-- Sample approvals (assuming event IDs 1-4)
INSERT INTO approvals (id, event_id, step_order, approver_id, decision, submitted_at, decided_at) VALUES
(approvals_seq.NEXTVAL, 1, 1, 3, 'APPROVED', TO_TIMESTAMP('2025-12-20 09:00:00', 'YYYY-MM-DD HH24:MI:SS'), TO_TIMESTAMP('2025-12-20 10:00:00', 'YYYY-MM-DD HH24:MI:SS'));
INSERT INTO approvals (id, event_id, step_order, approver_id, decision, submitted_at, decided_at) VALUES
(approvals_seq.NEXTVAL, 2, 1, 1, NULL, TO_TIMESTAMP('2025-12-23 08:00:00', 'YYYY-MM-DD HH24:MI:SS'), NULL);
INSERT INTO approvals (id, event_id, step_order, approver_id, decision, submitted_at, decided_at) VALUES
(approvals_seq.NEXTVAL, 3, 1, 1, 'APPROVED', TO_TIMESTAMP('2025-12-22 14:00:00', 'YYYY-MM-DD HH24:MI:SS'), TO_TIMESTAMP('2025-12-22 15:00:00', 'YYYY-MM-DD HH24:MI:SS'));
INSERT INTO approvals (id, event_id, step_order, approver_id, decision, submitted_at, decided_at) VALUES
(approvals_seq.NEXTVAL, 4, 1, 1, 'APPROVED', TO_TIMESTAMP('2025-12-19 09:00:00', 'YYYY-MM-DD HH24:MI:SS'), TO_TIMESTAMP('2025-12-19 11:00:00', 'YYYY-MM-DD HH24:MI:SS'));

COMMIT;

-- Example query: Calculate leave balance per member for a given year (2025)
-- This query calculates: granted leaves - used leaves = remaining balance
/*
SELECT
    m.id,
    m.name,
    m.department,
    m.position,
    m.annual_leave_granted,
    NVL(SUM(CASE
        WHEN e.event_type = 'LEAVE'
        AND e.status = 'APPROVED'
        AND EXTRACT(YEAR FROM e.start_date) = 2025
        THEN e.leave_amount
        ELSE 0
    END), 0) AS used_leave,
    m.annual_leave_granted - NVL(SUM(CASE
        WHEN e.event_type = 'LEAVE'
        AND e.status = 'APPROVED'
        AND EXTRACT(YEAR FROM e.start_date) = 2025
        THEN e.leave_amount
        ELSE 0
    END), 0) AS remaining_leave
FROM
    members m
LEFT JOIN
    events e ON m.id = e.member_id
WHERE
    m.is_active = 1
GROUP BY
    m.id, m.name, m.department, m.position, m.annual_leave_granted
ORDER BY
    m.department, m.name;
*/

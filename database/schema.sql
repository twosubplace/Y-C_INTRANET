-- Team Leave & Schedule Management System
-- Database Schema for PostgreSQL (portable to other RDBMS)

-- Drop tables if exists (for clean reinstall)
DROP TABLE IF EXISTS approvals CASCADE;
DROP TABLE IF EXISTS events CASCADE;
DROP TABLE IF EXISTS members CASCADE;

-- Table: members
-- Stores team member information
CREATE TABLE members (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    department VARCHAR(100) NOT NULL,
    position VARCHAR(100) NOT NULL,
    annual_leave_granted DECIMAL(4, 1) NOT NULL DEFAULT 15.0,
    is_active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT check_annual_leave_positive CHECK (annual_leave_granted >= 0)
);

-- Table: events
-- Unified table for both leave and schedule events
CREATE TABLE events (
    id SERIAL PRIMARY KEY,
    member_id INTEGER NOT NULL,
    event_type VARCHAR(20) NOT NULL,
    event_subtype VARCHAR(50) NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    leave_amount DECIMAL(3, 1) NOT NULL DEFAULT 0.0,
    title VARCHAR(200) NOT NULL,
    description TEXT,
    status VARCHAR(20) NOT NULL DEFAULT 'DRAFT',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
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
    id SERIAL PRIMARY KEY,
    event_id INTEGER NOT NULL,
    step_order INTEGER NOT NULL,
    approver_id INTEGER NOT NULL,
    decision VARCHAR(20),
    comment TEXT,
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

-- Sample data for testing
INSERT INTO members (name, department, position, annual_leave_granted) VALUES
('김철수', '개발팀', '팀장', 20.0),
('이영희', '개발팀', '선임', 18.0),
('박민수', '기획팀', '팀장', 20.0),
('정수진', '개발팀', '주임', 15.0),
('최동욱', '기획팀', '대리', 16.0);

-- Sample events
INSERT INTO events (member_id, event_type, event_subtype, start_date, end_date, leave_amount, title, status) VALUES
(1, 'LEAVE', '연차', '2025-12-24', '2025-12-24', 1.0, '연차', 'APPROVED'),
(2, 'LEAVE', '반차-오전', '2025-12-23', '2025-12-23', 0.5, '반차(오전)', 'SUBMITTED'),
(3, 'SCHEDULE', '회의', '2025-12-23', '2025-12-23', 0.0, '경영진 회의', 'APPROVED'),
(4, 'LEAVE', '병가', '2025-12-20', '2025-12-20', 1.0, '병가', 'APPROVED');

-- Sample approvals
INSERT INTO approvals (event_id, step_order, approver_id, decision, submitted_at, decided_at) VALUES
(1, 1, 3, 'APPROVED', '2025-12-20 09:00:00', '2025-12-20 10:00:00'),
(2, 1, 1, NULL, '2025-12-23 08:00:00', NULL),
(3, 1, 1, 'APPROVED', '2025-12-22 14:00:00', '2025-12-22 15:00:00'),
(4, 1, 1, 'APPROVED', '2025-12-19 09:00:00', '2025-12-19 11:00:00');

-- Example query: Calculate leave balance per member for a given year (2025)
-- This query calculates: granted leaves - used leaves = remaining balance
/*
SELECT
    m.id,
    m.name,
    m.department,
    m.position,
    m.annual_leave_granted,
    COALESCE(SUM(CASE
        WHEN e.event_type = 'LEAVE'
        AND e.status = 'APPROVED'
        AND EXTRACT(YEAR FROM e.start_date) = 2025
        THEN e.leave_amount
        ELSE 0
    END), 0) AS used_leave,
    m.annual_leave_granted - COALESCE(SUM(CASE
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
    m.is_active = true
GROUP BY
    m.id, m.name, m.department, m.position, m.annual_leave_granted
ORDER BY
    m.department, m.name;
*/

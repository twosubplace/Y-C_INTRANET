-- Team Leave & Schedule Management System
-- Database Schema for Oracle Database
-- NOTE: Only creates tables if they don't exist

-- Create sequences for auto-increment (only if not exists)
DECLARE
  v_count NUMBER;
BEGIN
  SELECT COUNT(*) INTO v_count FROM user_sequences WHERE sequence_name = 'MEMBERS_SEQ';
  IF v_count = 0 THEN
    EXECUTE IMMEDIATE 'CREATE SEQUENCE members_seq START WITH 1 INCREMENT BY 1';
  END IF;
END;
/

DECLARE
  v_count NUMBER;
BEGIN
  SELECT COUNT(*) INTO v_count FROM user_sequences WHERE sequence_name = 'EVENTS_SEQ';
  IF v_count = 0 THEN
    EXECUTE IMMEDIATE 'CREATE SEQUENCE events_seq START WITH 1 INCREMENT BY 1';
  END IF;
END;
/

DECLARE
  v_count NUMBER;
BEGIN
  SELECT COUNT(*) INTO v_count FROM user_sequences WHERE sequence_name = 'APPROVALS_SEQ';
  IF v_count = 0 THEN
    EXECUTE IMMEDIATE 'CREATE SEQUENCE approvals_seq START WITH 1 INCREMENT BY 1';
  END IF;
END;
/

-- Table: members (only if not exists)
DECLARE
  v_count NUMBER;
BEGIN
  SELECT COUNT(*) INTO v_count FROM user_tables WHERE table_name = 'MEMBERS';
  IF v_count = 0 THEN
    EXECUTE IMMEDIATE '
    CREATE TABLE members (
        id NUMBER PRIMARY KEY,
        name VARCHAR2(100) NOT NULL,
        department VARCHAR2(100) NOT NULL,
        position VARCHAR2(100) NOT NULL,
        annual_leave_granted NUMBER(4, 1) DEFAULT 15.0 NOT NULL,
        is_active NUMBER(1) DEFAULT 1 NOT NULL,
        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
        updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
        CONSTRAINT check_annual_leave_positive CHECK (annual_leave_granted >= 0),
        CONSTRAINT check_is_active CHECK (is_active IN (0, 1))
    )';
  END IF;
END;
/

-- Table: events (only if not exists)
DECLARE
  v_count NUMBER;
BEGIN
  SELECT COUNT(*) INTO v_count FROM user_tables WHERE table_name = 'EVENTS';
  IF v_count = 0 THEN
    EXECUTE IMMEDIATE '
    CREATE TABLE events (
        id NUMBER PRIMARY KEY,
        member_id NUMBER NOT NULL,
        event_type VARCHAR2(20) NOT NULL,
        event_subtype VARCHAR2(50) NOT NULL,
        start_date DATE NOT NULL,
        end_date DATE NOT NULL,
        leave_amount NUMBER(3, 1) DEFAULT 0.0 NOT NULL,
        title VARCHAR2(200) NOT NULL,
        description CLOB,
        status VARCHAR2(20) DEFAULT ''DRAFT'' NOT NULL,
        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
        updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
        CONSTRAINT fk_events_member FOREIGN KEY (member_id) REFERENCES members(id) ON DELETE CASCADE,
        CONSTRAINT check_event_type CHECK (event_type IN (''LEAVE'', ''SCHEDULE'')),
        CONSTRAINT check_event_subtype CHECK (event_subtype IN (''연차'', ''반차-오전'', ''반차-오후'', ''병가'', ''회의'', ''외근'', ''개인일정'')),
        CONSTRAINT check_status CHECK (status IN (''DRAFT'', ''SUBMITTED'', ''APPROVED'', ''REJECTED'', ''CANCELED'')),
        CONSTRAINT check_leave_amount CHECK (leave_amount >= 0 AND leave_amount <= 365),
        CONSTRAINT check_date_order CHECK (end_date >= start_date)
    )';
  END IF;
END;
/

-- Table: approvals (only if not exists)
DECLARE
  v_count NUMBER;
BEGIN
  SELECT COUNT(*) INTO v_count FROM user_tables WHERE table_name = 'APPROVALS';
  IF v_count = 0 THEN
    EXECUTE IMMEDIATE '
    CREATE TABLE approvals (
        id NUMBER PRIMARY KEY,
        event_id NUMBER NOT NULL,
        step_order NUMBER NOT NULL,
        approver_id NUMBER NOT NULL,
        decision VARCHAR2(20),
        comment CLOB,
        submitted_at TIMESTAMP,
        decided_at TIMESTAMP,
        CONSTRAINT fk_approvals_event FOREIGN KEY (event_id) REFERENCES events(id) ON DELETE CASCADE,
        CONSTRAINT fk_approvals_approver FOREIGN KEY (approver_id) REFERENCES members(id),
        CONSTRAINT check_decision CHECK (decision IS NULL OR decision IN (''APPROVED'', ''REJECTED'')),
        CONSTRAINT unique_event_step UNIQUE (event_id, step_order)
    )';
  END IF;
END;
/

-- Create triggers for auto-increment (only if not exists)
DECLARE
  v_count NUMBER;
BEGIN
  SELECT COUNT(*) INTO v_count FROM user_triggers WHERE trigger_name = 'MEMBERS_BIR';
  IF v_count = 0 THEN
    EXECUTE IMMEDIATE '
    CREATE OR REPLACE TRIGGER members_bir
    BEFORE INSERT ON members
    FOR EACH ROW
    BEGIN
      IF :NEW.id IS NULL THEN
        SELECT members_seq.NEXTVAL INTO :NEW.id FROM dual;
      END IF;
    END;';
  END IF;
END;
/

DECLARE
  v_count NUMBER;
BEGIN
  SELECT COUNT(*) INTO v_count FROM user_triggers WHERE trigger_name = 'EVENTS_BIR';
  IF v_count = 0 THEN
    EXECUTE IMMEDIATE '
    CREATE OR REPLACE TRIGGER events_bir
    BEFORE INSERT ON events
    FOR EACH ROW
    BEGIN
      IF :NEW.id IS NULL THEN
        SELECT events_seq.NEXTVAL INTO :NEW.id FROM dual;
      END IF;
    END;';
  END IF;
END;
/

DECLARE
  v_count NUMBER;
BEGIN
  SELECT COUNT(*) INTO v_count FROM user_triggers WHERE trigger_name = 'APPROVALS_BIR';
  IF v_count = 0 THEN
    EXECUTE IMMEDIATE '
    CREATE OR REPLACE TRIGGER approvals_bir
    BEFORE INSERT ON approvals
    FOR EACH ROW
    BEGIN
      IF :NEW.id IS NULL THEN
        SELECT approvals_seq.NEXTVAL INTO :NEW.id FROM dual;
      END IF;
    END;';
  END IF;
END;
/

-- Indexes for performance optimization (only if not exists)
DECLARE
  v_count NUMBER;
BEGIN
  SELECT COUNT(*) INTO v_count FROM user_indexes WHERE index_name = 'IDX_MEMBERS_ACTIVE';
  IF v_count = 0 THEN
    EXECUTE IMMEDIATE 'CREATE INDEX idx_members_active ON members(is_active)';
  END IF;
END;
/

DECLARE
  v_count NUMBER;
BEGIN
  SELECT COUNT(*) INTO v_count FROM user_indexes WHERE index_name = 'IDX_MEMBERS_DEPARTMENT';
  IF v_count = 0 THEN
    EXECUTE IMMEDIATE 'CREATE INDEX idx_members_department ON members(department)';
  END IF;
END;
/

DECLARE
  v_count NUMBER;
BEGIN
  SELECT COUNT(*) INTO v_count FROM user_indexes WHERE index_name = 'IDX_EVENTS_MEMBER';
  IF v_count = 0 THEN
    EXECUTE IMMEDIATE 'CREATE INDEX idx_events_member ON events(member_id)';
  END IF;
END;
/

DECLARE
  v_count NUMBER;
BEGIN
  SELECT COUNT(*) INTO v_count FROM user_indexes WHERE index_name = 'IDX_EVENTS_TYPE';
  IF v_count = 0 THEN
    EXECUTE IMMEDIATE 'CREATE INDEX idx_events_type ON events(event_type)';
  END IF;
END;
/

DECLARE
  v_count NUMBER;
BEGIN
  SELECT COUNT(*) INTO v_count FROM user_indexes WHERE index_name = 'IDX_EVENTS_STATUS';
  IF v_count = 0 THEN
    EXECUTE IMMEDIATE 'CREATE INDEX idx_events_status ON events(status)';
  END IF;
END;
/

DECLARE
  v_count NUMBER;
BEGIN
  SELECT COUNT(*) INTO v_count FROM user_indexes WHERE index_name = 'IDX_EVENTS_DATES';
  IF v_count = 0 THEN
    EXECUTE IMMEDIATE 'CREATE INDEX idx_events_dates ON events(start_date, end_date)';
  END IF;
END;
/

DECLARE
  v_count NUMBER;
BEGIN
  SELECT COUNT(*) INTO v_count FROM user_indexes WHERE index_name = 'IDX_EVENTS_MEMBER_DATES';
  IF v_count = 0 THEN
    EXECUTE IMMEDIATE 'CREATE INDEX idx_events_member_dates ON events(member_id, start_date, end_date)';
  END IF;
END;
/

DECLARE
  v_count NUMBER;
BEGIN
  SELECT COUNT(*) INTO v_count FROM user_indexes WHERE index_name = 'IDX_APPROVALS_EVENT';
  IF v_count = 0 THEN
    EXECUTE IMMEDIATE 'CREATE INDEX idx_approvals_event ON approvals(event_id)';
  END IF;
END;
/

DECLARE
  v_count NUMBER;
BEGIN
  SELECT COUNT(*) INTO v_count FROM user_indexes WHERE index_name = 'IDX_APPROVALS_APPROVER';
  IF v_count = 0 THEN
    EXECUTE IMMEDIATE 'CREATE INDEX idx_approvals_approver ON approvals(approver_id)';
  END IF;
END;
/

DECLARE
  v_count NUMBER;
BEGIN
  SELECT COUNT(*) INTO v_count FROM user_indexes WHERE index_name = 'IDX_APPROVALS_DECISION';
  IF v_count = 0 THEN
    EXECUTE IMMEDIATE 'CREATE INDEX idx_approvals_decision ON approvals(decision)';
  END IF;
END;
/

-- Sample data for testing (only if members table is empty)
DECLARE
  v_count NUMBER;
BEGIN
  SELECT COUNT(*) INTO v_count FROM members;
  IF v_count = 0 THEN
    INSERT INTO members (name, department, position, annual_leave_granted) VALUES
    ('김철수', '개발팀', '팀장', 20.0);
    INSERT INTO members (name, department, position, annual_leave_granted) VALUES
    ('이영희', '개발팀', '선임', 18.0);
    INSERT INTO members (name, department, position, annual_leave_granted) VALUES
    ('박민수', '기획팀', '팀장', 20.0);
    INSERT INTO members (name, department, position, annual_leave_granted) VALUES
    ('정수진', '개발팀', '주임', 15.0);
    INSERT INTO members (name, department, position, annual_leave_granted) VALUES
    ('최동욱', '기획팀', '대리', 16.0);

    INSERT INTO events (member_id, event_type, event_subtype, start_date, end_date, leave_amount, title, status) VALUES
    (1, 'LEAVE', '연차', TO_DATE('2025-12-24', 'YYYY-MM-DD'), TO_DATE('2025-12-24', 'YYYY-MM-DD'), 1.0, '연차', 'APPROVED');
    INSERT INTO events (member_id, event_type, event_subtype, start_date, end_date, leave_amount, title, status) VALUES
    (2, 'LEAVE', '반차-오전', TO_DATE('2025-12-23', 'YYYY-MM-DD'), TO_DATE('2025-12-23', 'YYYY-MM-DD'), 0.5, '반차(오전)', 'SUBMITTED');
    INSERT INTO events (member_id, event_type, event_subtype, start_date, end_date, leave_amount, title, status) VALUES
    (3, 'SCHEDULE', '회의', TO_DATE('2025-12-23', 'YYYY-MM-DD'), TO_DATE('2025-12-23', 'YYYY-MM-DD'), 0.0, '경영진 회의', 'APPROVED');
    INSERT INTO events (member_id, event_type, event_subtype, start_date, end_date, leave_amount, title, status) VALUES
    (4, 'LEAVE', '병가', TO_DATE('2025-12-20', 'YYYY-MM-DD'), TO_DATE('2025-12-20', 'YYYY-MM-DD'), 1.0, '병가', 'APPROVED');

    INSERT INTO approvals (event_id, step_order, approver_id, decision, submitted_at, decided_at) VALUES
    (1, 1, 3, 'APPROVED', TO_TIMESTAMP('2025-12-20 09:00:00', 'YYYY-MM-DD HH24:MI:SS'), TO_TIMESTAMP('2025-12-20 10:00:00', 'YYYY-MM-DD HH24:MI:SS'));
    INSERT INTO approvals (event_id, step_order, approver_id, decision, submitted_at, decided_at) VALUES
    (2, 1, 1, NULL, TO_TIMESTAMP('2025-12-23 08:00:00', 'YYYY-MM-DD HH24:MI:SS'), NULL);
    INSERT INTO approvals (event_id, step_order, approver_id, decision, submitted_at, decided_at) VALUES
    (3, 1, 1, 'APPROVED', TO_TIMESTAMP('2025-12-22 14:00:00', 'YYYY-MM-DD HH24:MI:SS'), TO_TIMESTAMP('2025-12-22 15:00:00', 'YYYY-MM-DD HH24:MI:SS'));
    INSERT INTO approvals (event_id, step_order, approver_id, decision, submitted_at, decided_at) VALUES
    (4, 1, 1, 'APPROVED', TO_TIMESTAMP('2025-12-19 09:00:00', 'YYYY-MM-DD HH24:MI:SS'), TO_TIMESTAMP('2025-12-19 11:00:00', 'YYYY-MM-DD HH24:MI:SS'));

    COMMIT;
  END IF;
END;
/

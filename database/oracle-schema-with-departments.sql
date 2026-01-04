-- Team Leave & Schedule Management System
-- Database Schema for Oracle Database with Department Hierarchy
-- NOTE: Only creates tables if they don't exist

-- Create sequences for auto-increment (only if not exists)
DECLARE
  v_count NUMBER;
BEGIN
  SELECT COUNT(*) INTO v_count FROM user_sequences WHERE sequence_name = 'DEPARTMENTS_SEQ';
  IF v_count = 0 THEN
    EXECUTE IMMEDIATE 'CREATE SEQUENCE departments_seq START WITH 1 INCREMENT BY 1';
  END IF;
END;
/

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

-- Table: departments (부서 테이블 - 상하위 부서 구조)
DECLARE
  v_count NUMBER;
BEGIN
  SELECT COUNT(*) INTO v_count FROM user_tables WHERE table_name = 'DEPARTMENTS';
  IF v_count = 0 THEN
    EXECUTE IMMEDIATE '
    CREATE TABLE departments (
        id NUMBER PRIMARY KEY,
        name VARCHAR2(100) NOT NULL,
        parent_id NUMBER,
        depth NUMBER DEFAULT 0 NOT NULL,
        is_active NUMBER(1) DEFAULT 1 NOT NULL,
        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
        updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
    )';
  END IF;
END;
/

-- Table: members (department_id로 변경)
DECLARE
  v_count NUMBER;
BEGIN
  SELECT COUNT(*) INTO v_count FROM user_tables WHERE table_name = 'MEMBERS';
  IF v_count = 0 THEN
    EXECUTE IMMEDIATE '
    CREATE TABLE members (
        id NUMBER PRIMARY KEY,
        name VARCHAR2(100) NOT NULL,
        department_id NUMBER NOT NULL,
        position VARCHAR2(100) NOT NULL,
        annual_leave_granted NUMBER(4, 1) DEFAULT 15.0 NOT NULL,
        is_active NUMBER(1) DEFAULT 1 NOT NULL,
        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
        updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
    )';
  END IF;
END;
/

-- Table: events (기존과 동일)
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
        updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
    )';
  END IF;
END;
/

-- Table: approvals (기존과 동일)
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
        approval_comment CLOB,
        submitted_at TIMESTAMP,
        decided_at TIMESTAMP
    )';
  END IF;
END;
/

-- Create triggers for auto-increment
DECLARE
  v_count NUMBER;
BEGIN
  SELECT COUNT(*) INTO v_count FROM user_triggers WHERE trigger_name = 'DEPARTMENTS_BIR';
  IF v_count = 0 THEN
    EXECUTE IMMEDIATE '
    CREATE OR REPLACE TRIGGER departments_bir
    BEFORE INSERT ON departments
    FOR EACH ROW
    BEGIN
      IF :NEW.id IS NULL THEN
        SELECT departments_seq.NEXTVAL INTO :NEW.id FROM dual;
      END IF;
    END;';
  END IF;
END;
/

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

-- Indexes for performance optimization
DECLARE
  v_count NUMBER;
BEGIN
  SELECT COUNT(*) INTO v_count FROM user_indexes WHERE index_name = 'IDX_DEPARTMENTS_PARENT';
  IF v_count = 0 THEN
    EXECUTE IMMEDIATE 'CREATE INDEX idx_departments_parent ON departments(parent_id)';
  END IF;
END;
/

DECLARE
  v_count NUMBER;
BEGIN
  SELECT COUNT(*) INTO v_count FROM user_indexes WHERE index_name = 'IDX_DEPARTMENTS_ACTIVE';
  IF v_count = 0 THEN
    EXECUTE IMMEDIATE 'CREATE INDEX idx_departments_active ON departments(is_active)';
  END IF;
END;
/

DECLARE
  v_count NUMBER;
BEGIN
  SELECT COUNT(*) INTO v_count FROM user_indexes WHERE index_name = 'IDX_MEMBERS_DEPARTMENT';
  IF v_count = 0 THEN
    EXECUTE IMMEDIATE 'CREATE INDEX idx_members_department ON members(department_id)';
  END IF;
END;
/

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

DECLARE
  v_count NUMBER;
BEGIN
  SELECT COUNT(*) INTO v_count FROM user_indexes WHERE index_name = 'IDX_APPROVALS_EVENT_STEP';
  IF v_count = 0 THEN
    EXECUTE IMMEDIATE 'CREATE UNIQUE INDEX idx_approvals_event_step ON approvals(event_id, step_order)';
  END IF;
END;
/

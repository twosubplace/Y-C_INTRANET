-- Add expense_reports table to existing schema
-- This script adds the expense reports feature to the system

-- Create sequence for expense_reports
DECLARE
  v_count NUMBER;
BEGIN
  SELECT COUNT(*) INTO v_count FROM user_sequences WHERE sequence_name = 'EXPENSE_REPORTS_SEQ';
  IF v_count = 0 THEN
    EXECUTE IMMEDIATE 'CREATE SEQUENCE expense_reports_seq START WITH 1 INCREMENT BY 1';
  END IF;
END;
/

-- Create expense_reports table
DECLARE
  v_count NUMBER;
BEGIN
  SELECT COUNT(*) INTO v_count FROM user_tables WHERE table_name = 'EXPENSE_REPORTS';
  IF v_count = 0 THEN
    EXECUTE IMMEDIATE '
    CREATE TABLE expense_reports (
        id NUMBER PRIMARY KEY,
        member_id NUMBER NOT NULL,
        title VARCHAR2(200) NOT NULL,
        report_month DATE NOT NULL,
        total_amount NUMBER(12, 2) NOT NULL,
        description CLOB,
        status VARCHAR2(20) DEFAULT ''DRAFT'' NOT NULL,
        file_path VARCHAR2(500),
        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
        updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
        CONSTRAINT fk_expense_member FOREIGN KEY (member_id) REFERENCES members(id)
    )';
  END IF;
END;
/

-- Create trigger for auto-increment
DECLARE
  v_count NUMBER;
BEGIN
  SELECT COUNT(*) INTO v_count FROM user_triggers WHERE trigger_name = 'EXPENSE_REPORTS_BIR';
  IF v_count = 0 THEN
    EXECUTE IMMEDIATE '
    CREATE OR REPLACE TRIGGER expense_reports_bir
    BEFORE INSERT ON expense_reports
    FOR EACH ROW
    BEGIN
      IF :NEW.id IS NULL THEN
        SELECT expense_reports_seq.NEXTVAL INTO :NEW.id FROM dual;
      END IF;
    END;';
  END IF;
END;
/

-- Create indexes for performance
DECLARE
  v_count NUMBER;
BEGIN
  SELECT COUNT(*) INTO v_count FROM user_indexes WHERE index_name = 'IDX_EXPENSE_REPORTS_MEMBER';
  IF v_count = 0 THEN
    EXECUTE IMMEDIATE 'CREATE INDEX idx_expense_reports_member ON expense_reports(member_id)';
  END IF;
END;
/

DECLARE
  v_count NUMBER;
BEGIN
  SELECT COUNT(*) INTO v_count FROM user_indexes WHERE index_name = 'IDX_EXPENSE_REPORTS_MONTH';
  IF v_count = 0 THEN
    EXECUTE IMMEDIATE 'CREATE INDEX idx_expense_reports_month ON expense_reports(report_month)';
  END IF;
END;
/

DECLARE
  v_count NUMBER;
BEGIN
  SELECT COUNT(*) INTO v_count FROM user_indexes WHERE index_name = 'IDX_EXPENSE_REPORTS_STATUS';
  IF v_count = 0 THEN
    EXECUTE IMMEDIATE 'CREATE INDEX idx_expense_reports_status ON expense_reports(status)';
  END IF;
END;
/

-- Add comment to table
COMMENT ON TABLE expense_reports IS '지출보고서 테이블';
COMMENT ON COLUMN expense_reports.id IS '고유 ID';
COMMENT ON COLUMN expense_reports.member_id IS '멤버 ID (FK)';
COMMENT ON COLUMN expense_reports.title IS '보고서 제목';
COMMENT ON COLUMN expense_reports.report_month IS '보고 월';
COMMENT ON COLUMN expense_reports.total_amount IS '총 금액';
COMMENT ON COLUMN expense_reports.description IS '설명';
COMMENT ON COLUMN expense_reports.status IS '상태 (DRAFT, SUBMITTED, APPROVED, REJECTED)';
COMMENT ON COLUMN expense_reports.file_path IS '파일 경로';
COMMENT ON COLUMN expense_reports.created_at IS '생성 일시';
COMMENT ON COLUMN expense_reports.updated_at IS '수정 일시';

-- Add expense_items table for detailed expense tracking
-- This script adds the expense items feature to the system

-- Create sequence for expense_items
DECLARE
  v_count NUMBER;
BEGIN
  SELECT COUNT(*) INTO v_count FROM user_sequences WHERE sequence_name = 'EXPENSE_ITEMS_SEQ';
  IF v_count = 0 THEN
    EXECUTE IMMEDIATE 'CREATE SEQUENCE expense_items_seq START WITH 1 INCREMENT BY 1';
  END IF;
END;
/

-- Create expense_items table
DECLARE
  v_count NUMBER;
BEGIN
  SELECT COUNT(*) INTO v_count FROM user_tables WHERE table_name = 'EXPENSE_ITEMS';
  IF v_count = 0 THEN
    EXECUTE IMMEDIATE '
    CREATE TABLE expense_items (
        id NUMBER PRIMARY KEY,
        member_id NUMBER NOT NULL,
        usage_date DATE NOT NULL,
        description VARCHAR2(200) NOT NULL,
        account VARCHAR2(100) NOT NULL,
        amount NUMBER(12, 2) NOT NULL,
        vendor VARCHAR2(200),
        cost_code VARCHAR2(100),
        project_code VARCHAR2(100),
        note CLOB,
        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
        updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
        CONSTRAINT fk_expense_item_member FOREIGN KEY (member_id) REFERENCES members(id)
    )';
  END IF;
END;
/

-- Create trigger for auto-increment
DECLARE
  v_count NUMBER;
BEGIN
  SELECT COUNT(*) INTO v_count FROM user_triggers WHERE trigger_name = 'EXPENSE_ITEMS_BIR';
  IF v_count = 0 THEN
    EXECUTE IMMEDIATE '
    CREATE OR REPLACE TRIGGER expense_items_bir
    BEFORE INSERT ON expense_items
    FOR EACH ROW
    BEGIN
      IF :NEW.id IS NULL THEN
        SELECT expense_items_seq.NEXTVAL INTO :NEW.id FROM dual;
      END IF;
    END;';
  END IF;
END;
/

-- Create indexes for performance
DECLARE
  v_count NUMBER;
BEGIN
  SELECT COUNT(*) INTO v_count FROM user_indexes WHERE index_name = 'IDX_EXPENSE_ITEMS_MEMBER';
  IF v_count = 0 THEN
    EXECUTE IMMEDIATE 'CREATE INDEX idx_expense_items_member ON expense_items(member_id)';
  END IF;
END;
/

DECLARE
  v_count NUMBER;
BEGIN
  SELECT COUNT(*) INTO v_count FROM user_indexes WHERE index_name = 'IDX_EXPENSE_ITEMS_DATE';
  IF v_count = 0 THEN
    EXECUTE IMMEDIATE 'CREATE INDEX idx_expense_items_date ON expense_items(usage_date)';
  END IF;
END;
/

DECLARE
  v_count NUMBER;
BEGIN
  SELECT COUNT(*) INTO v_count FROM user_indexes WHERE index_name = 'IDX_EXPENSE_ITEMS_MEMBER_DATE';
  IF v_count = 0 THEN
    EXECUTE IMMEDIATE 'CREATE INDEX idx_expense_items_member_date ON expense_items(member_id, usage_date)';
  END IF;
END;
/

-- Add comment to table
COMMENT ON TABLE expense_items IS '지출 내역 상세 테이블';
COMMENT ON COLUMN expense_items.id IS '고유 ID';
COMMENT ON COLUMN expense_items.member_id IS '멤버 ID (FK)';
COMMENT ON COLUMN expense_items.usage_date IS '사용일시';
COMMENT ON COLUMN expense_items.description IS '사용 내용';
COMMENT ON COLUMN expense_items.account IS '계정';
COMMENT ON COLUMN expense_items.amount IS '사용금액';
COMMENT ON COLUMN expense_items.vendor IS '업소명';
COMMENT ON COLUMN expense_items.cost_code IS '경비코드';
COMMENT ON COLUMN expense_items.project_code IS '프로젝트코드';
COMMENT ON COLUMN expense_items.note IS '사용 용도/참석자 메모';
COMMENT ON COLUMN expense_items.created_at IS '생성 일시';
COMMENT ON COLUMN expense_items.updated_at IS '수정 일시';

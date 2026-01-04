-- Alter expense_items_intranet table to match expense_items structure
-- This script adds missing columns to expense_items_intranet table

-- Add member_id column
DECLARE
  v_count NUMBER;
BEGIN
  SELECT COUNT(*) INTO v_count
  FROM user_tab_columns
  WHERE table_name = 'EXPENSE_ITEMS_INTRANET' AND column_name = 'MEMBER_ID';

  IF v_count = 0 THEN
    EXECUTE IMMEDIATE 'ALTER TABLE expense_items_intranet ADD (member_id NUMBER)';
    EXECUTE IMMEDIATE 'COMMENT ON COLUMN expense_items_intranet.member_id IS ''멤버 ID (FK)''';
  END IF;
END;
/

-- Add usage_date column (rename from expense_date)
DECLARE
  v_count NUMBER;
BEGIN
  SELECT COUNT(*) INTO v_count
  FROM user_tab_columns
  WHERE table_name = 'EXPENSE_ITEMS_INTRANET' AND column_name = 'USAGE_DATE';

  IF v_count = 0 THEN
    EXECUTE IMMEDIATE 'ALTER TABLE expense_items_intranet ADD (usage_date DATE)';
    -- Copy data from expense_date if it exists
    DECLARE
      v_old_col NUMBER;
    BEGIN
      SELECT COUNT(*) INTO v_old_col
      FROM user_tab_columns
      WHERE table_name = 'EXPENSE_ITEMS_INTRANET' AND column_name = 'EXPENSE_DATE';

      IF v_old_col > 0 THEN
        EXECUTE IMMEDIATE 'UPDATE expense_items_intranet SET usage_date = expense_date WHERE expense_date IS NOT NULL';
      END IF;
    END;
    EXECUTE IMMEDIATE 'COMMENT ON COLUMN expense_items_intranet.usage_date IS ''사용일시''';
  END IF;
END;
/

-- Add account column (rename from category)
DECLARE
  v_count NUMBER;
BEGIN
  SELECT COUNT(*) INTO v_count
  FROM user_tab_columns
  WHERE table_name = 'EXPENSE_ITEMS_INTRANET' AND column_name = 'ACCOUNT';

  IF v_count = 0 THEN
    EXECUTE IMMEDIATE 'ALTER TABLE expense_items_intranet ADD (account VARCHAR2(100))';
    -- Copy data from category if it exists
    DECLARE
      v_old_col NUMBER;
    BEGIN
      SELECT COUNT(*) INTO v_old_col
      FROM user_tab_columns
      WHERE table_name = 'EXPENSE_ITEMS_INTRANET' AND column_name = 'CATEGORY';

      IF v_old_col > 0 THEN
        EXECUTE IMMEDIATE 'UPDATE expense_items_intranet SET account = category WHERE category IS NOT NULL';
      END IF;
    END;
    EXECUTE IMMEDIATE 'COMMENT ON COLUMN expense_items_intranet.account IS ''계정''';
  END IF;
END;
/

-- Add vendor column
DECLARE
  v_count NUMBER;
BEGIN
  SELECT COUNT(*) INTO v_count
  FROM user_tab_columns
  WHERE table_name = 'EXPENSE_ITEMS_INTRANET' AND column_name = 'VENDOR';

  IF v_count = 0 THEN
    EXECUTE IMMEDIATE 'ALTER TABLE expense_items_intranet ADD (vendor VARCHAR2(200))';
    EXECUTE IMMEDIATE 'COMMENT ON COLUMN expense_items_intranet.vendor IS ''업소명''';
  END IF;
END;
/

-- Add cost_code column
DECLARE
  v_count NUMBER;
BEGIN
  SELECT COUNT(*) INTO v_count
  FROM user_tab_columns
  WHERE table_name = 'EXPENSE_ITEMS_INTRANET' AND column_name = 'COST_CODE';

  IF v_count = 0 THEN
    EXECUTE IMMEDIATE 'ALTER TABLE expense_items_intranet ADD (cost_code VARCHAR2(100))';
    EXECUTE IMMEDIATE 'COMMENT ON COLUMN expense_items_intranet.cost_code IS ''경비코드''';
  END IF;
END;
/

-- Add project_code column
DECLARE
  v_count NUMBER;
BEGIN
  SELECT COUNT(*) INTO v_count
  FROM user_tab_columns
  WHERE table_name = 'EXPENSE_ITEMS_INTRANET' AND column_name = 'PROJECT_CODE';

  IF v_count = 0 THEN
    EXECUTE IMMEDIATE 'ALTER TABLE expense_items_intranet ADD (project_code VARCHAR2(100))';
    EXECUTE IMMEDIATE 'COMMENT ON COLUMN expense_items_intranet.project_code IS ''프로젝트코드''';
  END IF;
END;
/

-- Add note column
DECLARE
  v_count NUMBER;
BEGIN
  SELECT COUNT(*) INTO v_count
  FROM user_tab_columns
  WHERE table_name = 'EXPENSE_ITEMS_INTRANET' AND column_name = 'NOTE';

  IF v_count = 0 THEN
    EXECUTE IMMEDIATE 'ALTER TABLE expense_items_intranet ADD (note CLOB)';
    EXECUTE IMMEDIATE 'COMMENT ON COLUMN expense_items_intranet.note IS ''사용 용도/참석자 메모''';
  END IF;
END;
/

-- Add welfare_flag column
DECLARE
  v_count NUMBER;
BEGIN
  SELECT COUNT(*) INTO v_count
  FROM user_tab_columns
  WHERE table_name = 'EXPENSE_ITEMS_INTRANET' AND column_name = 'WELFARE_FLAG';

  IF v_count = 0 THEN
    EXECUTE IMMEDIATE 'ALTER TABLE expense_items_intranet ADD (welfare_flag VARCHAR2(1) DEFAULT ''N'')';
    EXECUTE IMMEDIATE 'COMMENT ON COLUMN expense_items_intranet.welfare_flag IS ''복지비 여부 (Y/N)''';
  END IF;
END;
/

-- Add updated_at column
DECLARE
  v_count NUMBER;
BEGIN
  SELECT COUNT(*) INTO v_count
  FROM user_tab_columns
  WHERE table_name = 'EXPENSE_ITEMS_INTRANET' AND column_name = 'UPDATED_AT';

  IF v_count = 0 THEN
    EXECUTE IMMEDIATE 'ALTER TABLE expense_items_intranet ADD (updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)';
    EXECUTE IMMEDIATE 'UPDATE expense_items_intranet SET updated_at = created_at WHERE updated_at IS NULL';
    EXECUTE IMMEDIATE 'COMMENT ON COLUMN expense_items_intranet.updated_at IS ''수정 일시''';
  END IF;
END;
/

-- Add foreign key constraint for member_id
DECLARE
  v_count NUMBER;
BEGIN
  SELECT COUNT(*) INTO v_count
  FROM user_constraints
  WHERE table_name = 'EXPENSE_ITEMS_INTRANET' AND constraint_name = 'FK_EXPENSE_ITEM_INTRANET_MEMBER';

  IF v_count = 0 THEN
    -- Check if members_intranet table exists
    DECLARE
      v_table_exists NUMBER;
    BEGIN
      SELECT COUNT(*) INTO v_table_exists
      FROM user_tables
      WHERE table_name = 'MEMBERS_INTRANET';

      IF v_table_exists > 0 THEN
        EXECUTE IMMEDIATE 'ALTER TABLE expense_items_intranet ADD CONSTRAINT fk_expense_item_intranet_member FOREIGN KEY (member_id) REFERENCES members_intranet(id)';
      END IF;
    END;
  END IF;
END;
/

-- Create index on member_id
DECLARE
  v_count NUMBER;
BEGIN
  SELECT COUNT(*) INTO v_count
  FROM user_indexes
  WHERE index_name = 'IDX_EXPENSE_ITEMS_INTRANET_MEMBER';

  IF v_count = 0 THEN
    EXECUTE IMMEDIATE 'CREATE INDEX idx_expense_items_intranet_member ON expense_items_intranet(member_id)';
  END IF;
END;
/

-- Create index on usage_date
DECLARE
  v_count NUMBER;
BEGIN
  SELECT COUNT(*) INTO v_count
  FROM user_indexes
  WHERE index_name = 'IDX_EXPENSE_ITEMS_INTRANET_DATE';

  IF v_count = 0 THEN
    EXECUTE IMMEDIATE 'CREATE INDEX idx_expense_items_intranet_date ON expense_items_intranet(usage_date)';
  END IF;
END;
/

-- Create composite index on member_id and usage_date
DECLARE
  v_count NUMBER;
BEGIN
  SELECT COUNT(*) INTO v_count
  FROM user_indexes
  WHERE index_name = 'IDX_EXPENSE_ITEMS_INTRANET_MEM_DATE';

  IF v_count = 0 THEN
    EXECUTE IMMEDIATE 'CREATE INDEX idx_expense_items_intranet_mem_date ON expense_items_intranet(member_id, usage_date)';
  END IF;
END;
/

-- Create index on account (for filtering by expense type)
DECLARE
  v_count NUMBER;
BEGIN
  SELECT COUNT(*) INTO v_count
  FROM user_indexes
  WHERE index_name = 'IDX_EXPENSE_ITEMS_INTRANET_ACCOUNT';

  IF v_count = 0 THEN
    EXECUTE IMMEDIATE 'CREATE INDEX idx_expense_items_intranet_account ON expense_items_intranet(account)';
  END IF;
END;
/

-- Create index on welfare_flag (for welfare expense filtering)
DECLARE
  v_count NUMBER;
BEGIN
  SELECT COUNT(*) INTO v_count
  FROM user_indexes
  WHERE index_name = 'IDX_EXPENSE_ITEMS_INTRANET_WELFARE';

  IF v_count = 0 THEN
    EXECUTE IMMEDIATE 'CREATE INDEX idx_expense_items_intranet_welfare ON expense_items_intranet(welfare_flag)';
  END IF;
END;
/

-- Optional: Drop old columns if they are no longer needed
-- Uncomment the following blocks if you want to remove the old columns

/*
-- Drop expense_date column (replaced by usage_date)
DECLARE
  v_count NUMBER;
BEGIN
  SELECT COUNT(*) INTO v_count
  FROM user_tab_columns
  WHERE table_name = 'EXPENSE_ITEMS_INTRANET' AND column_name = 'EXPENSE_DATE';

  IF v_count > 0 THEN
    EXECUTE IMMEDIATE 'ALTER TABLE expense_items_intranet DROP COLUMN expense_date';
  END IF;
END;
/

-- Drop category column (replaced by account)
DECLARE
  v_count NUMBER;
BEGIN
  SELECT COUNT(*) INTO v_count
  FROM user_tab_columns
  WHERE table_name = 'EXPENSE_ITEMS_INTRANET' AND column_name = 'CATEGORY';

  IF v_count > 0 THEN
    EXECUTE IMMEDIATE 'ALTER TABLE expense_items_intranet DROP COLUMN category';
  END IF;
END;
/

-- Drop receipt_path column (if not needed)
DECLARE
  v_count NUMBER;
BEGIN
  SELECT COUNT(*) INTO v_count
  FROM user_tab_columns
  WHERE table_name = 'EXPENSE_ITEMS_INTRANET' AND column_name = 'RECEIPT_PATH';

  IF v_count > 0 THEN
    EXECUTE IMMEDIATE 'ALTER TABLE expense_items_intranet DROP COLUMN receipt_path';
  END IF;
END;
/
*/

COMMIT;

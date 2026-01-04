-- Add welfare_flag column to expense_items table
-- This column tracks whether an expense is a welfare benefit expense

DECLARE
  v_count NUMBER;
BEGIN
  -- Check if column already exists
  SELECT COUNT(*) INTO v_count
  FROM user_tab_columns
  WHERE table_name = 'EXPENSE_ITEMS' AND column_name = 'WELFARE_FLAG';

  IF v_count = 0 THEN
    EXECUTE IMMEDIATE 'ALTER TABLE expense_items ADD welfare_flag CHAR(1) DEFAULT ''N'' CHECK (welfare_flag IN (''Y'', ''N''))';
    DBMS_OUTPUT.PUT_LINE('Column welfare_flag added successfully');
  ELSE
    DBMS_OUTPUT.PUT_LINE('Column welfare_flag already exists');
  END IF;
END;
/

-- Add comment
COMMENT ON COLUMN expense_items.welfare_flag IS '복지비 여부 (Y/N)';

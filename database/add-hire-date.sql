-- Add hire_date column to members table

-- Check if hire_date column exists and add it if not
DECLARE
  v_count NUMBER;
BEGIN
  SELECT COUNT(*) INTO v_count
  FROM user_tab_columns
  WHERE table_name = 'MEMBERS' AND column_name = 'HIRE_DATE';

  IF v_count = 0 THEN
    EXECUTE IMMEDIATE 'ALTER TABLE members ADD hire_date DATE';
    DBMS_OUTPUT.PUT_LINE('hire_date column added to members table');
  ELSE
    DBMS_OUTPUT.PUT_LINE('hire_date column already exists');
  END IF;
END;
/

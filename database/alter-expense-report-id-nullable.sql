-- Fix expense_items_intranet table to allow NULL values for old columns
-- and drop old columns that have been replaced

SET SERVEROUTPUT ON;

-- 1. Make expense_report_id nullable
BEGIN
    EXECUTE IMMEDIATE 'ALTER TABLE expense_items_intranet MODIFY (expense_report_id NUMBER NULL)';
    DBMS_OUTPUT.PUT_LINE('✓ expense_report_id modified to allow NULL');
EXCEPTION
    WHEN OTHERS THEN
        IF SQLCODE = -1451 THEN
            DBMS_OUTPUT.PUT_LINE('✓ expense_report_id already allows NULL');
        ELSE
            DBMS_OUTPUT.PUT_LINE('✗ Error modifying expense_report_id: ' || SQLERRM);
        END IF;
END;
/

-- 2. Drop expense_date if usage_date exists, otherwise make it nullable
DECLARE
    v_expense_date_exists NUMBER;
    v_usage_date_exists NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_expense_date_exists FROM user_tab_columns WHERE table_name = 'EXPENSE_ITEMS_INTRANET' AND column_name = 'EXPENSE_DATE';
    SELECT COUNT(*) INTO v_usage_date_exists FROM user_tab_columns WHERE table_name = 'EXPENSE_ITEMS_INTRANET' AND column_name = 'USAGE_DATE';

    IF v_expense_date_exists > 0 THEN
        IF v_usage_date_exists > 0 THEN
            EXECUTE IMMEDIATE 'ALTER TABLE expense_items_intranet DROP COLUMN expense_date';
            DBMS_OUTPUT.PUT_LINE('✓ expense_date column dropped (replaced by usage_date)');
        ELSE
            EXECUTE IMMEDIATE 'ALTER TABLE expense_items_intranet MODIFY (expense_date DATE NULL)';
            DBMS_OUTPUT.PUT_LINE('✓ expense_date modified to allow NULL');
        END IF;
    ELSE
        DBMS_OUTPUT.PUT_LINE('○ expense_date does not exist');
    END IF;
EXCEPTION
    WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE('✗ Error handling expense_date: ' || SQLERRM);
END;
/

-- 3. Drop category if account exists, otherwise make it nullable
DECLARE
    v_category_exists NUMBER;
    v_account_exists NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_category_exists FROM user_tab_columns WHERE table_name = 'EXPENSE_ITEMS_INTRANET' AND column_name = 'CATEGORY';
    SELECT COUNT(*) INTO v_account_exists FROM user_tab_columns WHERE table_name = 'EXPENSE_ITEMS_INTRANET' AND column_name = 'ACCOUNT';

    IF v_category_exists > 0 THEN
        IF v_account_exists > 0 THEN
            EXECUTE IMMEDIATE 'ALTER TABLE expense_items_intranet DROP COLUMN category';
            DBMS_OUTPUT.PUT_LINE('✓ category column dropped (replaced by account)');
        ELSE
            EXECUTE IMMEDIATE 'ALTER TABLE expense_items_intranet MODIFY (category VARCHAR2(100) NULL)';
            DBMS_OUTPUT.PUT_LINE('✓ category modified to allow NULL');
        END IF;
    ELSE
        DBMS_OUTPUT.PUT_LINE('○ category does not exist');
    END IF;
EXCEPTION
    WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE('✗ Error handling category: ' || SQLERRM);
END;
/

-- 4. Make amount nullable if it has NOT NULL constraint
BEGIN
    EXECUTE IMMEDIATE 'ALTER TABLE expense_items_intranet MODIFY (amount NUMBER NULL)';
    DBMS_OUTPUT.PUT_LINE('✓ amount modified to allow NULL');
EXCEPTION
    WHEN OTHERS THEN
        IF SQLCODE = -1451 THEN
            DBMS_OUTPUT.PUT_LINE('✓ amount already allows NULL');
        ELSE
            DBMS_OUTPUT.PUT_LINE('✗ Error modifying amount: ' || SQLERRM);
        END IF;
END;
/

-- 5. Make description nullable if it has NOT NULL constraint
BEGIN
    EXECUTE IMMEDIATE 'ALTER TABLE expense_items_intranet MODIFY (description VARCHAR2(500) NULL)';
    DBMS_OUTPUT.PUT_LINE('✓ description modified to allow NULL');
EXCEPTION
    WHEN OTHERS THEN
        IF SQLCODE = -1451 THEN
            DBMS_OUTPUT.PUT_LINE('✓ description already allows NULL');
        ELSE
            DBMS_OUTPUT.PUT_LINE('✗ Error modifying description: ' || SQLERRM);
        END IF;
END;
/

-- 6. Drop receipt_path if it exists (not used anymore)
DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count FROM user_tab_columns WHERE table_name = 'EXPENSE_ITEMS_INTRANET' AND column_name = 'RECEIPT_PATH';

    IF v_count > 0 THEN
        EXECUTE IMMEDIATE 'ALTER TABLE expense_items_intranet DROP COLUMN receipt_path';
        DBMS_OUTPUT.PUT_LINE('✓ receipt_path column dropped (not used)');
    ELSE
        DBMS_OUTPUT.PUT_LINE('○ receipt_path does not exist');
    END IF;
EXCEPTION
    WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE('✗ Error dropping receipt_path: ' || SQLERRM);
END;
/

COMMIT;

DBMS_OUTPUT.PUT_LINE('');
DBMS_OUTPUT.PUT_LINE('=== Migration Complete ===');

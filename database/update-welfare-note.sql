-- 복지비 플래그가 'Y'인데 메모에 "[복지비]"가 없는 항목들에 "[복지비]" 추가

SET SERVEROUTPUT ON;

DECLARE
    v_count NUMBER := 0;
    v_updated NUMBER := 0;
BEGIN
    -- 복지비 플래그가 'Y'이고 메모가 NULL이거나 "[복지비]"로 시작하지 않는 항목 개수 확인
    SELECT COUNT(*) INTO v_count
    FROM expense_items_intranet
    WHERE welfare_flag = 'Y'
    AND (note IS NULL OR note NOT LIKE '[복지비]%');

    DBMS_OUTPUT.PUT_LINE('업데이트 대상 항목 수: ' || v_count);

    IF v_count > 0 THEN
        -- 메모가 NULL인 경우
        UPDATE expense_items_intranet
        SET note = '[복지비] ',
            updated_at = CURRENT_TIMESTAMP
        WHERE welfare_flag = 'Y'
        AND note IS NULL;

        v_updated := SQL%ROWCOUNT;
        DBMS_OUTPUT.PUT_LINE('✓ 메모가 NULL인 항목 업데이트: ' || v_updated || '건');

        -- 메모가 있지만 "[복지비]"로 시작하지 않는 경우
        UPDATE expense_items_intranet
        SET note = '[복지비] ' || note,
            updated_at = CURRENT_TIMESTAMP
        WHERE welfare_flag = 'Y'
        AND note IS NOT NULL
        AND note NOT LIKE '[복지비]%';

        v_updated := SQL%ROWCOUNT;
        DBMS_OUTPUT.PUT_LINE('✓ 메모에 [복지비] 추가: ' || v_updated || '건');

        COMMIT;
        DBMS_OUTPUT.PUT_LINE('');
        DBMS_OUTPUT.PUT_LINE('=== 업데이트 완료 ===');
    ELSE
        DBMS_OUTPUT.PUT_LINE('업데이트할 항목이 없습니다.');
    END IF;
EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        DBMS_OUTPUT.PUT_LINE('✗ 오류 발생: ' || SQLERRM);
END;
/

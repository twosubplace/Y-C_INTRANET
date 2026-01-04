-- ================================================================
-- moods2@yncsmart.com (윤대웅) 계정 상태 확인
-- ================================================================

SET PAGESIZE 50
SET LINESIZE 200
COL name FORMAT A20
COL email FORMAT A30
COL department_name FORMAT A40
COL position FORMAT A15
COL password_hash FORMAT A70

-- 윤대웅 계정 상세 정보
SELECT '========================================' AS separator FROM dual;
SELECT 'moods2@yncsmart.com 계정 정보' AS title FROM dual;
SELECT '========================================' AS separator FROM dual;

SELECT
    m.id,
    m.name,
    m.email,
    d.name AS department_name,
    m.position,
    m.phone,
    m.role,
    m.is_active,
    m.password AS password_hash,
    m.created_at,
    m.updated_at
FROM members_intranet m
LEFT JOIN departments_intranet d ON m.department_id = d.id
WHERE m.email = 'moods2@yncsmart.com';

-- 계정이 없으면
SELECT '========================================' AS separator FROM dual;
SELECT CASE
    WHEN COUNT(*) = 0 THEN '계정이 존재하지 않습니다!'
    WHEN COUNT(*) = 1 THEN '계정이 존재합니다.'
    ELSE '중복 계정이 존재합니다!'
END AS account_status
FROM members_intranet
WHERE email = 'moods2@yncsmart.com';

-- 모든 공통/FCM Unit 사원 확인
SELECT '========================================' AS separator FROM dual;
SELECT '공통/FCM Unit 전체 사원' AS title FROM dual;
SELECT '========================================' AS separator FROM dual;

SELECT
    m.id,
    m.name,
    m.email,
    m.position,
    m.role,
    m.is_active
FROM members_intranet m
WHERE m.department_id = 14
ORDER BY m.name;

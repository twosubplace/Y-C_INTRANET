-- ================================================================
-- 모든 신규 사원 비밀번호를 admin과 동일하게 동기화
-- ================================================================
-- admin@yncsmart.com 계정의 해시를 모든 신규 사원에게 복사
-- ================================================================

-- 현재 admin 비밀번호 해시 확인
SELECT '========================================' AS separator FROM dual;
SELECT 'admin 계정 비밀번호 해시' AS title FROM dual;
SELECT '========================================' AS separator FROM dual;

SELECT
    name,
    email,
    password
FROM members_intranet
WHERE email = 'admin@yncsmart.com';

-- 모든 신규 사원 비밀번호를 admin과 동일하게 업데이트
UPDATE members_intranet
SET password = (SELECT password FROM members_intranet WHERE email = 'admin@yncsmart.com'),
    updated_at = CURRENT_TIMESTAMP
WHERE email IN (
    'sjlee@yncsmart.com',
    'mspark1313@yncsmart.com',
    'moods2@yncsmart.com',
    'ss9827ss@yncsmart.com',
    'mtalk79@yncsmart.com',
    'soyun22k@yncsmart.com',
    'parkgaya@yncsmart.com',
    'kaidlee@yncsmart.com',
    'cheej@yncsmart.com'
);

COMMIT;

-- 변경 결과 확인
SELECT '========================================' AS separator FROM dual;
SELECT '모든 계정이 admin과 동일한 비밀번호로 변경되었습니다.' AS result FROM dual;
SELECT '========================================' AS separator FROM dual;

SELECT
    name,
    email,
    SUBSTR(password, 1, 25) || '...' AS password_hash,
    CASE
        WHEN password = (SELECT password FROM members_intranet WHERE email = 'admin@yncsmart.com')
        THEN 'admin과 일치'
        ELSE 'admin과 불일치'
    END AS match_status
FROM members_intranet
WHERE email LIKE '%@yncsmart.com'
ORDER BY name;

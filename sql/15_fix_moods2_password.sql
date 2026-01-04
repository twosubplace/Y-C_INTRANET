-- ================================================================
-- moods2@yncsmart.com (윤대웅) 비밀번호 수정
-- ================================================================
-- admin 계정과 동일한 해시로 변경 (admin1234)
-- ================================================================

-- 현재 비밀번호 확인
SELECT '========================================' AS separator FROM dual;
SELECT '변경 전 비밀번호 해시' AS title FROM dual;
SELECT '========================================' AS separator FROM dual;

SELECT
    name,
    email,
    password
FROM members_intranet
WHERE email = 'moods2@yncsmart.com';

-- 비밀번호 업데이트 (admin1234)
UPDATE members_intranet
SET password = '$2a$10$N9qo8uLOickgx2ZNqJLqrOeQXLgZgZ6wYPPWlKdFJLqVWrLVj1Vka', -- admin1234
    updated_at = CURRENT_TIMESTAMP
WHERE email = 'moods2@yncsmart.com';

COMMIT;

-- 변경 후 확인
SELECT '========================================' AS separator FROM dual;
SELECT '변경 후 비밀번호 해시' AS title FROM dual;
SELECT '비밀번호: admin1234' AS password FROM dual;
SELECT '========================================' AS separator FROM dual;

SELECT
    name,
    email,
    password
FROM members_intranet
WHERE email = 'moods2@yncsmart.com';

-- admin 계정 해시와 비교
SELECT '========================================' AS separator FROM dual;
SELECT 'admin 계정과 해시 비교' AS title FROM dual;
SELECT '========================================' AS separator FROM dual;

SELECT
    name,
    email,
    CASE
        WHEN password = '$2a$10$N9qo8uLOickgx2ZNqJLqrOeQXLgZgZ6wYPPWlKdFJLqVWrLVj1Vka' THEN '일치'
        ELSE '불일치'
    END AS hash_match
FROM members_intranet
WHERE email IN ('admin@yncsmart.com', 'moods2@yncsmart.com')
ORDER BY email;

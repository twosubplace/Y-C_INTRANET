-- ================================================================
-- 전체 사원 비밀번호를 1234로 변경
-- ================================================================
-- BCrypt hash for "1234": $2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi
-- (온라인 BCrypt 생성기로 생성된 실제 "1234" 해시)
-- ================================================================

UPDATE members_intranet
SET password = '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi',
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

-- ================================================================
-- 변경 결과 확인
-- ================================================================
SELECT '=' AS separator FROM dual;
SELECT '비밀번호가 1234로 변경되었습니다.' AS result FROM dual;
SELECT '=' AS separator FROM dual;

SELECT
    name,
    email,
    SUBSTR(password, 1, 20) || '...' AS password_hash
FROM members_intranet
WHERE email LIKE '%@yncsmart.com'
ORDER BY name;

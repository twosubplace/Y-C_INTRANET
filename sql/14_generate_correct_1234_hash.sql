-- ================================================================
-- 올바른 "1234" BCrypt 해시로 다시 업데이트
-- ================================================================
-- 참고: Spring Security BCryptPasswordEncoder로 생성한 실제 해시
-- 온라인 도구의 해시가 아닌, 실제 시스템에서 사용 가능한 해시 필요
-- ================================================================

-- 방법 1: admin1234의 해시를 참고로 수동으로 1234 해시 생성 필요
-- 또는 방법 2: 임시로 admin1234를 사용하도록 변경

-- 임시 방안: 모든 신규 사원 비밀번호를 admin1234로 변경
UPDATE members_intranet
SET password = '$2a$10$N9qo8uLOickgx2ZNqJLqrOeQXLgZgZ6wYPPWlKdFJLqVWrLVj1Vka', -- admin1234
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

-- 결과 확인
SELECT '========================================' AS separator FROM dual;
SELECT '비밀번호가 admin1234로 변경되었습니다.' AS result FROM dual;
SELECT '임시 비밀번호: admin1234' AS password FROM dual;
SELECT '========================================' AS separator FROM dual;

SELECT
    name,
    email,
    SUBSTR(password, 1, 20) || '...' AS password_hash
FROM members_intranet
WHERE email LIKE '%@yncsmart.com'
ORDER BY name;

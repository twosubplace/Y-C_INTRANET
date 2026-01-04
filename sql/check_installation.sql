-- ============================================
-- YNC INTRANET SYSTEM - 설치 확인 스크립트
-- ============================================

-- 1. 테이블 목록 확인
SELECT '=== 생성된 테이블 목록 ===' AS info FROM dual;
SELECT table_name
FROM user_tables
WHERE table_name LIKE '%_INTRANET'
ORDER BY table_name;

-- 2. 시퀀스 목록 확인
SELECT '=== 생성된 시퀀스 목록 ===' AS info FROM dual;
SELECT sequence_name
FROM user_sequences
WHERE sequence_name LIKE '%_INTRANET_%'
ORDER BY sequence_name;

-- 3. 삽입된 데이터 확인
SELECT '=== 삽입된 데이터 건수 ===' AS info FROM dual;
SELECT '공통 코드' AS 구분, COUNT(*) AS 건수 FROM common_codes_intranet
UNION ALL
SELECT '부서', COUNT(*) FROM departments_intranet
UNION ALL
SELECT '사용자', COUNT(*) FROM members_intranet
UNION ALL
SELECT '메일 템플릿', COUNT(*) FROM email_templates_intranet;

-- 4. 공통 코드 상세
SELECT '=== 공통 코드 상세 ===' AS info FROM dual;
SELECT code_type, COUNT(*) AS 코드수
FROM common_codes_intranet
GROUP BY code_type
ORDER BY code_type;

-- 5. 부서 목록
SELECT '=== 부서 목록 ===' AS info FROM dual;
SELECT id, name, display_order, is_active
FROM departments_intranet
ORDER BY display_order;

-- 6. 관리자 계정
SELECT '=== 관리자 계정 ===' AS info FROM dual;
SELECT id, email, name, role, position, is_active
FROM members_intranet;

-- 7. 메일 템플릿
SELECT '=== 메일 템플릿 ===' AS info FROM dual;
SELECT template_type, subject, is_active
FROM email_templates_intranet
ORDER BY id;

-- 8. 외래키 제약조건 확인
SELECT '=== 외래키 제약조건 ===' AS info FROM dual;
SELECT constraint_name, table_name, r_constraint_name
FROM user_constraints
WHERE constraint_type = 'R'
AND table_name LIKE '%_INTRANET'
ORDER BY table_name, constraint_name;

SELECT '=== 설치 확인 완료 ===' AS info FROM dual;

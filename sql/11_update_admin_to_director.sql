-- ================================================================
-- 이승주 본부장 신규 등록
-- ================================================================
-- 비밀번호: 1234 (BCrypt 암호화)
-- 시스템관리자는 그대로 유지하고, 이승주 본부장을 새로 등록
-- ================================================================

-- 이승주 (본부장) - 기업사업1본부 (id=4)
INSERT INTO members_intranet (
    id, email, password, name, department_id, position, phone, role,
    annual_leave_granted, is_active, created_at, updated_at
) VALUES (
    members_intranet_seq.NEXTVAL,
    'sjlee@yncsmart.com',
    '$2a$10$vI8aWBnW3fID.ZQ4/zo1G.q1lRps.9cGqaq6t8p0q1qFweiUjgVGe', -- 1234
    '이승주',
    4, -- 기업사업1본부
    '본부장',
    '010-5422-0285',
    'APPROVER', -- 결재권자 (본부장)
    15,
    1,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
);

-- 기업사업1본부의 manager_id를 이승주로 설정
UPDATE departments_intranet
SET manager_id = (SELECT id FROM members_intranet WHERE email = 'sjlee@yncsmart.com'),
    updated_at = CURRENT_TIMESTAMP
WHERE id = 4;

COMMIT;

-- ================================================================
-- 등록 결과 확인
-- ================================================================
SELECT '=' AS separator FROM dual;
SELECT '이승주 본부장이 등록되었습니다.' AS result FROM dual;
SELECT '=' AS separator FROM dual;

-- 등록된 정보 확인
SELECT
    m.id,
    m.name,
    m.email,
    d.name AS department_name,
    m.position,
    m.phone,
    m.role
FROM members_intranet m
LEFT JOIN departments_intranet d ON m.department_id = d.id
WHERE m.email = 'sjlee@yncsmart.com';

-- 기업사업1본부 정보 확인
SELECT
    d.id,
    d.name AS department_name,
    d.manager_id,
    m.name AS manager_name
FROM departments_intranet d
LEFT JOIN members_intranet m ON d.manager_id = m.id
WHERE d.id = 4;

-- ================================================================
-- 사원 데이터 삽입 스크립트
-- ================================================================
-- 비밀번호: admin1234 (BCrypt 암호화됨)
-- BCrypt hash: $2a$10$N9qo8uLOickgx2ZNqJLqrOeQXLgZgZ6wYPPWlKdFJLqVWrLVj1Vka
-- ================================================================

-- ================================================================
-- 1. 전문위원 (지능형 인테리어 소프트웨어 연구소 산하)
-- ================================================================
-- 참고: 기업사업1본부장 이승주는 11_update_admin_to_director.sql에서 처리
-- ================================================================

-- 박명순 (전문위원) - 전문위원 Unit (id=8)
INSERT INTO members_intranet (
    id, email, password, name, department_id, position, phone, role,
    annual_leave_granted, is_active, created_at, updated_at
) VALUES (
    members_intranet_seq.NEXTVAL,
    'mspark1313@yncsmart.com',
    '$2a$10$vI8aWBnW3fID.ZQ4/zo1G.q1lRps.9cGqaq6t8p0q1qFweiUjgVGe', -- 1234
    '박명순',
    8, -- 전문위원 Unit
    '자문/임원',
    '01052145744',
    'APPROVER',
    15,
    1,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
);

-- ================================================================
-- 2. 공통/FCM Unit (기업사업1본부 산하)
-- ================================================================

-- 윤대웅 (Unit장) - 공통/FCM Unit (id=14)
INSERT INTO members_intranet (
    id, email, password, name, department_id, position, phone, role,
    annual_leave_granted, is_active, created_at, updated_at
) VALUES (
    members_intranet_seq.NEXTVAL,
    'moods2@yncsmart.com',
    '$2a$10$vI8aWBnW3fID.ZQ4/zo1G.q1lRps.9cGqaq6t8p0q1qFweiUjgVGe', -- 1234
    '윤대웅',
    14, -- 공통/FCM Unit
    'Unit장',
    '01065493554',
    'APPROVER',
    15,
    1,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
);

-- 신의섭 (매니저) - 공통/FCM Unit (id=14)
INSERT INTO members_intranet (
    id, email, password, name, department_id, position, phone, role,
    annual_leave_granted, is_active, created_at, updated_at
) VALUES (
    members_intranet_seq.NEXTVAL,
    'ss9827ss@yncsmart.com',
    '$2a$10$vI8aWBnW3fID.ZQ4/zo1G.q1lRps.9cGqaq6t8p0q1qFweiUjgVGe', -- 1234
    '신의섭',
    14, -- 공통/FCM Unit
    '매니저',
    '83891970',
    'USER',
    15,
    1,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
);

-- 이준영 (매니저) - 공통/FCM Unit (id=14)
INSERT INTO members_intranet (
    id, email, password, name, department_id, position, phone, role,
    annual_leave_granted, is_active, created_at, updated_at
) VALUES (
    members_intranet_seq.NEXTVAL,
    'mtalk79@yncsmart.com',
    '$2a$10$vI8aWBnW3fID.ZQ4/zo1G.q1lRps.9cGqaq6t8p0q1qFweiUjgVGe', -- 1234
    '이준영',
    14, -- 공통/FCM Unit
    '매니저',
    '010-6296-5117',
    'USER',
    15,
    1,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
);

-- 변소윤 - 공통/FCM Unit (id=14)
INSERT INTO members_intranet (
    id, email, password, name, department_id, position, phone, role,
    annual_leave_granted, is_active, created_at, updated_at
) VALUES (
    members_intranet_seq.NEXTVAL,
    'soyun22k@yncsmart.com',
    '$2a$10$vI8aWBnW3fID.ZQ4/zo1G.q1lRps.9cGqaq6t8p0q1qFweiUjgVGe', -- 1234
    '변소윤',
    14, -- 공통/FCM Unit
    '사원',
    '010-8703-1259',
    'USER',
    15,
    1,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
);

-- ================================================================
-- 3. UI/UX Unit (기업사업1본부 산하)
-- ================================================================

-- 박혜경 (Unit장) - UI/UX Unit (id=15)
INSERT INTO members_intranet (
    id, email, password, name, department_id, position, phone, role,
    annual_leave_granted, is_active, created_at, updated_at
) VALUES (
    members_intranet_seq.NEXTVAL,
    'parkgaya@yncsmart.com',
    '$2a$10$vI8aWBnW3fID.ZQ4/zo1G.q1lRps.9cGqaq6t8p0q1qFweiUjgVGe', -- 1234
    '박혜경',
    15, -- UI/UX Unit
    'Unit장',
    '10-9119-0769',
    'APPROVER',
    15,
    1,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
);

-- 이충원 (매니저) - UI/UX Unit (id=15)
INSERT INTO members_intranet (
    id, email, password, name, department_id, position, phone, role,
    annual_leave_granted, is_active, created_at, updated_at
) VALUES (
    members_intranet_seq.NEXTVAL,
    'kaidlee@yncsmart.com',
    '$2a$10$vI8aWBnW3fID.ZQ4/zo1G.q1lRps.9cGqaq6t8p0q1qFweiUjgVGe', -- 1234
    '이충원',
    15, -- UI/UX Unit
    '매니저',
    '010-3113-6148',
    'USER',
    15,
    1,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
);

-- 최은지 (매니저) - UI/UX Unit (id=15)
INSERT INTO members_intranet (
    id, email, password, name, department_id, position, phone, role,
    annual_leave_granted, is_active, created_at, updated_at
) VALUES (
    members_intranet_seq.NEXTVAL,
    'cheej@yncsmart.com',
    '$2a$10$vI8aWBnW3fID.ZQ4/zo1G.q1lRps.9cGqaq6t8p0q1qFweiUjgVGe', -- 1234
    '최은지',
    15, -- UI/UX Unit
    '매니저',
    '01022477615',
    'USER',
    15,
    1,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
);

COMMIT;

-- ================================================================
-- 삽입 결과 확인
-- ================================================================
SELECT '=' AS separator FROM dual;
SELECT '사원 데이터가 성공적으로 삽입되었습니다.' AS result FROM dual;
SELECT '=' AS separator FROM dual;

-- 부서별 사원 수 확인
SELECT
    d.name AS department_name,
    COUNT(m.id) AS member_count
FROM departments_intranet d
LEFT JOIN members_intranet m ON d.id = m.department_id
WHERE d.id IN (4, 8, 14, 15)
GROUP BY d.name
ORDER BY d.display_order;

-- 삽입된 사원 목록 확인
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
WHERE m.email LIKE '%@yncsmart.com'
ORDER BY d.display_order, m.position DESC, m.name;

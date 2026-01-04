-- ================================================================
-- 최종 사원 및 조직도 확인
-- ================================================================

SET PAGESIZE 100
SET LINESIZE 200
COL name FORMAT A20
COL email FORMAT A30
COL department_name FORMAT A40
COL position FORMAT A15
COL phone FORMAT A20
COL role FORMAT A10

-- ================================================================
-- 1. 전체 사원 목록 (부서별, 직급별)
-- ================================================================
SELECT '========================================' AS separator FROM dual;
SELECT '전체 사원 목록' AS title FROM dual;
SELECT '========================================' AS separator FROM dual;

SELECT
    m.id,
    m.name,
    m.email,
    d.name AS department_name,
    m.position,
    m.phone,
    m.role,
    m.annual_leave_granted AS leave_days
FROM members_intranet m
LEFT JOIN departments_intranet d ON m.department_id = d.id
ORDER BY d.display_order, m.position DESC, m.name;

-- ================================================================
-- 2. 부서별 사원 수
-- ================================================================
SELECT '========================================' AS separator FROM dual;
SELECT '부서별 사원 수' AS title FROM dual;
SELECT '========================================' AS separator FROM dual;

SELECT
    d.name AS department_name,
    COUNT(m.id) AS member_count,
    m2.name AS manager_name
FROM departments_intranet d
LEFT JOIN members_intranet m ON d.id = m.department_id
LEFT JOIN members_intranet m2 ON d.manager_id = m2.id
WHERE d.parent_id IS NOT NULL
GROUP BY d.name, m2.name
ORDER BY COUNT(m.id) DESC;

-- ================================================================
-- 3. 역할(Role)별 사원 수
-- ================================================================
SELECT '========================================' AS separator FROM dual;
SELECT '역할별 사원 수' AS title FROM dual;
SELECT '========================================' AS separator FROM dual;

SELECT
    role,
    COUNT(*) AS count
FROM members_intranet
GROUP BY role
ORDER BY
    CASE role
        WHEN 'ADMIN' THEN 1
        WHEN 'APPROVER' THEN 2
        WHEN 'USER' THEN 3
    END;

-- ================================================================
-- 4. 본부별 조직 구조
-- ================================================================
SELECT '========================================' AS separator FROM dual;
SELECT '조직도 구조 (본부 → Unit → 사원)' AS title FROM dual;
SELECT '========================================' AS separator FROM dual;

SELECT
    LEVEL AS depth,
    LPAD(' ', (LEVEL-1)*2, ' ') || d.name AS organization_tree,
    d.id,
    d.parent_id,
    m.name AS manager_name,
    (SELECT COUNT(*) FROM members_intranet WHERE department_id = d.id) AS member_count
FROM departments_intranet d
LEFT JOIN members_intranet m ON d.manager_id = m.id
START WITH d.parent_id IS NULL
CONNECT BY PRIOR d.id = d.parent_id
ORDER SIBLINGS BY d.display_order;

-- ================================================================
-- 5. 결재권자(APPROVER) 목록
-- ================================================================
SELECT '========================================' AS separator FROM dual;
SELECT '결재권자 목록' AS title FROM dual;
SELECT '========================================' AS separator FROM dual;

SELECT
    m.name,
    m.position,
    d.name AS department_name,
    m.email,
    m.phone
FROM members_intranet m
LEFT JOIN departments_intranet d ON m.department_id = d.id
WHERE m.role = 'APPROVER'
ORDER BY d.display_order, m.name;

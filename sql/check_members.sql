-- ================================================================
-- 전체 사원 목록 조회
-- ================================================================

SET PAGESIZE 50
SET LINESIZE 200
COL name FORMAT A20
COL email FORMAT A30
COL department_name FORMAT A30
COL position FORMAT A15
COL phone FORMAT A20

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
ORDER BY m.id;

-- 부서별 사원 수
SELECT '=' AS separator FROM dual;
SELECT 'Unit별 사원 수' AS title FROM dual;
SELECT '=' AS separator FROM dual;

SELECT
    d.name AS department_name,
    COUNT(m.id) AS member_count
FROM departments_intranet d
LEFT JOIN members_intranet m ON d.id = m.department_id
WHERE d.parent_id IS NOT NULL
GROUP BY d.name
HAVING COUNT(m.id) > 0
ORDER BY COUNT(m.id) DESC;

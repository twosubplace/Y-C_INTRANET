-- ============================================
-- members 테이블의 department_id 값 업데이트
-- ============================================

-- 1. 현재 상태 확인
SELECT m.id, m.name, m.department_id, d.name as dept_name
FROM members m
LEFT JOIN departments d ON m.department_id = d.id;

-- 2. 모든 members의 department_id를 기본값으로 설정 (공통/FCM으로 설정)
UPDATE members
SET department_id = (SELECT id FROM departments WHERE name = '공통/FCM' AND rownum = 1)
WHERE department_id IS NULL;

-- 3. 결과 확인
SELECT m.id, m.name, m.department_id, d.name as dept_name,
       (SELECT p.name FROM departments p WHERE p.id = d.parent_id) as parent_dept
FROM members m
LEFT JOIN departments d ON m.department_id = d.id;

COMMIT;

-- ============================================
-- 기존 department 컬럼을 department_id로 마이그레이션
-- ============================================

-- 1. 기존 members 데이터 확인
SELECT id, name, department FROM members;

-- 2. 기존 department 문자열 값으로 department_id 업데이트
UPDATE members SET department_id = (SELECT id FROM departments WHERE name = '공통/FCM') WHERE department = '개발팀';
UPDATE members SET department_id = (SELECT id FROM departments WHERE name = 'UX/UI') WHERE department = '기획팀';
UPDATE members SET department_id = (SELECT id FROM departments WHERE name = '개발팀') WHERE department = '디자인팀';
UPDATE members SET department_id = (SELECT id FROM departments WHERE name = '기획팀') WHERE department = '영업팀';
UPDATE members SET department_id = (SELECT id FROM departments WHERE name = '디자인팀') WHERE department = '인사팀';
UPDATE members SET department_id = (SELECT id FROM departments WHERE name = '인사팀') WHERE department = '재무팀';

-- 3. 매칭되지 않은 데이터는 기본값으로 설정 (기업사업1본부 - 개발팀으로)
UPDATE members SET department_id = (SELECT id FROM departments WHERE name = '개발팀' AND parent_id = 1)
WHERE department_id IS NULL;

-- 4. 결과 확인
SELECT m.id, m.name, m.department as old_dept, d.name as new_dept, m.department_id
FROM members m
LEFT JOIN departments d ON m.department_id = d.id;

-- 5. department 컬럼 삭제
ALTER TABLE members DROP COLUMN department;

COMMIT;

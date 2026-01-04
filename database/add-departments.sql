-- ============================================
-- 부서 테이블 추가 및 members 테이블 수정
-- ============================================

-- 1. departments 테이블 생성
CREATE TABLE departments (
    id NUMBER PRIMARY KEY,
    name VARCHAR2(100) NOT NULL,
    parent_id NUMBER,
    depth NUMBER DEFAULT 0 NOT NULL,
    is_active NUMBER(1) DEFAULT 1 NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);

-- 2. departments 시퀀스 생성
CREATE SEQUENCE departments_seq START WITH 1 INCREMENT BY 1;

-- 3. departments 트리거 생성
CREATE OR REPLACE TRIGGER departments_bir
BEFORE INSERT ON departments
FOR EACH ROW
BEGIN
  IF :NEW.id IS NULL THEN
    SELECT departments_seq.NEXTVAL INTO :NEW.id FROM dual;
  END IF;
END;
/

-- 4. members 테이블에 department_id 컬럼 추가
ALTER TABLE members ADD department_id NUMBER;

-- 5. 기존 department 문자열 데이터를 department_id로 마이그레이션하기 위한 부서 데이터 삽입

-- 최상위 부서 (본부)
INSERT INTO departments (name, parent_id, depth) VALUES ('경영본부', NULL, 0);
INSERT INTO departments (name, parent_id, depth) VALUES ('기술본부', NULL, 0);
INSERT INTO departments (name, parent_id, depth) VALUES ('영업본부', NULL, 0);

-- 하위 부서 (팀) - 경영본부 산하
INSERT INTO departments (name, parent_id, depth) VALUES ('인사팀', 1, 1);
INSERT INTO departments (name, parent_id, depth) VALUES ('재무팀', 1, 1);

-- 하위 부서 (팀) - 기술본부 산하
INSERT INTO departments (name, parent_id, depth) VALUES ('개발팀', 2, 1);
INSERT INTO departments (name, parent_id, depth) VALUES ('디자인팀', 2, 1);
INSERT INTO departments (name, parent_id, depth) VALUES ('기획팀', 2, 1);

-- 하위 부서 (팀) - 영업본부 산하
INSERT INTO departments (name, parent_id, depth) VALUES ('국내영업팀', 3, 1);
INSERT INTO departments (name, parent_id, depth) VALUES ('해외영업팀', 3, 1);

-- 6. 기존 members 데이터의 department_id 업데이트
UPDATE members SET department_id = (SELECT id FROM departments WHERE name = '개발팀') WHERE department = '개발팀';
UPDATE members SET department_id = (SELECT id FROM departments WHERE name = '기획팀') WHERE department = '기획팀';
UPDATE members SET department_id = (SELECT id FROM departments WHERE name = '디자인팀') WHERE department = '디자인팀';
UPDATE members SET department_id = (SELECT id FROM departments WHERE name = '국내영업팀') WHERE department = '영업팀';
UPDATE members SET department_id = (SELECT id FROM departments WHERE name = '인사팀') WHERE department = '인사팀';
UPDATE members SET department_id = (SELECT id FROM departments WHERE name = '재무팀') WHERE department = '재무팀';

-- 7. department_id를 NOT NULL로 변경
ALTER TABLE members MODIFY department_id NUMBER NOT NULL;

-- 8. 기존 department 컬럼 삭제 (선택사항 - 필요시 실행)
-- ALTER TABLE members DROP COLUMN department;

-- 9. 인덱스 생성
CREATE INDEX idx_departments_parent ON departments(parent_id);
CREATE INDEX idx_departments_active ON departments(is_active);
CREATE INDEX idx_members_department_new ON members(department_id);

COMMIT;

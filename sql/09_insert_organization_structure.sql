-- ================================================================
-- 조직도 구조 데이터 삽입 스크립트
-- ================================================================
-- (주)와이앤씨스마트웰스 조직도
-- ================================================================

-- 기존 부서 데이터 삭제 (초기화)
DELETE FROM departments_intranet;

-- 시퀀스 초기화
ALTER SEQUENCE departments_intranet_seq RESTART;

-- ================================================================
-- 1. 회사 최상위 (Root)
-- ================================================================
INSERT INTO departments_intranet (id, name, parent_id, manager_id, display_order, is_active)
VALUES (departments_intranet_seq.NEXTVAL, '(주)와이앤씨스마트웰스', NULL, NULL, 0, 1);

-- ================================================================
-- 2. 본부 (상위 부서) - 4개
-- ================================================================

-- 2-1. 경영관리본부 (id=2)
INSERT INTO departments_intranet (id, name, parent_id, manager_id, display_order, is_active)
VALUES (2, '경영관리본부', 1, NULL, 1, 1);

-- 2-2. 지능형 인테리어 소프트웨어 연구소 (id=3)
INSERT INTO departments_intranet (id, name, parent_id, manager_id, display_order, is_active)
VALUES (3, '지능형 인테리어 소프트웨어 연구소', 1, NULL, 2, 1);

-- 2-3. 기업사업1본부 (id=4)
INSERT INTO departments_intranet (id, name, parent_id, manager_id, display_order, is_active)
VALUES (4, '기업사업1본부', 1, NULL, 3, 1);

-- 2-4. 기업사업2본부 (id=5)
INSERT INTO departments_intranet (id, name, parent_id, manager_id, display_order, is_active)
VALUES (5, '기업사업2본부', 1, NULL, 4, 1);

-- ================================================================
-- 3. Unit (하위 팀) - 18개
-- ================================================================

-- 3-1. 경영관리본부 산하 Unit (2개)
INSERT INTO departments_intranet (id, name, parent_id, manager_id, display_order, is_active)
VALUES (6, '경영관리 Unit', 2, NULL, 1, 1);

INSERT INTO departments_intranet (id, name, parent_id, manager_id, display_order, is_active)
VALUES (7, 'CDP운영 Unit', 2, NULL, 2, 1);

-- 3-2. 지능형 인테리어 소프트웨어 연구소 산하 Unit (4개)
INSERT INTO departments_intranet (id, name, parent_id, manager_id, display_order, is_active)
VALUES (8, '전문위원', 3, NULL, 1, 1);

INSERT INTO departments_intranet (id, name, parent_id, manager_id, display_order, is_active)
VALUES (9, 'AI에이전트기획', 3, NULL, 2, 1);

INSERT INTO departments_intranet (id, name, parent_id, manager_id, display_order, is_active)
VALUES (10, '핵심기술소싱Unit', 3, NULL, 3, 1);

INSERT INTO departments_intranet (id, name, parent_id, manager_id, display_order, is_active)
VALUES (11, 'AI기반SW개발Unit', 3, NULL, 4, 1);

-- 3-3. 기업사업1본부 산하 Unit (6개)
INSERT INTO departments_intranet (id, name, parent_id, manager_id, display_order, is_active)
VALUES (12, '영업/구매 Unit', 4, NULL, 1, 1);

INSERT INTO departments_intranet (id, name, parent_id, manager_id, display_order, is_active)
VALUES (13, '생산/물류 Unit', 4, NULL, 2, 1);

INSERT INTO departments_intranet (id, name, parent_id, manager_id, display_order, is_active)
VALUES (14, '공통/FCM Unit', 4, NULL, 3, 1);

INSERT INTO departments_intranet (id, name, parent_id, manager_id, display_order, is_active)
VALUES (15, 'UI/UX Unit', 4, NULL, 4, 1);

INSERT INTO departments_intranet (id, name, parent_id, manager_id, display_order, is_active)
VALUES (16, 'SI사업 Unit', 4, NULL, 5, 1);

INSERT INTO departments_intranet (id, name, parent_id, manager_id, display_order, is_active)
VALUES (17, 'ICT 사업부 Unit', 4, NULL, 6, 1);

-- 3-4. 기업사업2본부 산하 Unit (2개)
INSERT INTO departments_intranet (id, name, parent_id, manager_id, display_order, is_active)
VALUES (18, 'TI사업 Unit', 5, NULL, 1, 1);

INSERT INTO departments_intranet (id, name, parent_id, manager_id, display_order, is_active)
VALUES (19, 'TS사업 Unit', 5, NULL, 2, 1);

COMMIT;

-- ================================================================
-- 조직도 구조 확인 쿼리
-- ================================================================
SELECT '조직도 데이터가 성공적으로 삽입되었습니다.' AS result FROM dual;

-- 전체 조직도 조회 (계층 구조)
SELECT
    LEVEL AS depth,
    LPAD(' ', (LEVEL-1)*2, ' ') || name AS organization_tree,
    id,
    parent_id,
    display_order
FROM departments_intranet
START WITH parent_id IS NULL
CONNECT BY PRIOR id = parent_id
ORDER SIBLINGS BY display_order;

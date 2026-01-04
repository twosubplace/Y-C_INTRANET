-- events 테이블에 부서/직급/본부 컬럼 추가
ALTER TABLE events ADD (
    department VARCHAR2(100),
    position VARCHAR2(50),
    division VARCHAR2(100)
);

COMMENT ON COLUMN events.department IS '부서명 (화면 입력값)';
COMMENT ON COLUMN events.position IS '직급 (화면 입력값)';
COMMENT ON COLUMN events.division IS '본부명 (화면 입력값)';

COMMIT;

-- 회원 테이블에 핸드폰번호 컬럼 추가
ALTER TABLE MEMBERS ADD phone VARCHAR2(20);

COMMENT ON COLUMN MEMBERS.phone IS '핸드폰번호';

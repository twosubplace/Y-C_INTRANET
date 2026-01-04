-- Members 테이블에 SMTP 비밀번호 컬럼 추가
ALTER TABLE members ADD (
    smtp_password VARCHAR2(255)
);

COMMENT ON COLUMN members.smtp_password IS 'SMTP 인증용 비밀번호 (이메일 발송시 사용)';

COMMIT;

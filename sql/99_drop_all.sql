-- ============================================
-- YNC INTRANET SYSTEM - DROP ALL OBJECTS SCRIPT
-- ============================================
-- Version: 1.0
-- Created: 2025-12-31
-- Description: 모든 인트라넷 테이블 및 시퀀스 삭제 스크립트
-- ⚠️ WARNING: 이 스크립트는 모든 데이터를 삭제합니다!
-- ============================================

-- ============================================
-- 테이블 삭제 (외래키 순서 고려하여 역순)
-- ============================================

DROP TABLE system_logs_intranet CASCADE CONSTRAINTS;
DROP TABLE notices_intranet CASCADE CONSTRAINTS;
DROP TABLE attachments_intranet CASCADE CONSTRAINTS;
DROP TABLE approval_lines_intranet CASCADE CONSTRAINTS;
DROP TABLE expense_items_intranet CASCADE CONSTRAINTS;
DROP TABLE expense_reports_intranet CASCADE CONSTRAINTS;
DROP TABLE leave_requests_intranet CASCADE CONSTRAINTS;
DROP TABLE documents_intranet CASCADE CONSTRAINTS;
DROP TABLE members_intranet CASCADE CONSTRAINTS;
DROP TABLE departments_intranet CASCADE CONSTRAINTS;
DROP TABLE email_templates_intranet CASCADE CONSTRAINTS;
DROP TABLE common_codes_intranet CASCADE CONSTRAINTS;

-- ============================================
-- 시퀀스 삭제
-- ============================================

DROP SEQUENCE departments_intranet_seq;
DROP SEQUENCE members_intranet_seq;
DROP SEQUENCE documents_intranet_seq;
DROP SEQUENCE leave_requests_intranet_seq;
DROP SEQUENCE expense_reports_intranet_seq;
DROP SEQUENCE expense_items_intranet_seq;
DROP SEQUENCE approval_lines_intranet_seq;
DROP SEQUENCE attachments_intranet_seq;
DROP SEQUENCE notices_intranet_seq;
DROP SEQUENCE system_logs_intranet_seq;
DROP SEQUENCE email_templates_intranet_seq;

-- ============================================
-- 완료 메시지
-- ============================================

SELECT '모든 인트라넷 객체가 삭제되었습니다.' AS result FROM dual;

-- ============================================
-- END OF DROP SCRIPT
-- ============================================

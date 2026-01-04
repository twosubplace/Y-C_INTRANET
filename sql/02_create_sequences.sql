-- ============================================
-- YNC INTRANET SYSTEM - SEQUENCE CREATION SCRIPT
-- ============================================
-- Version: 1.0
-- Created: 2025-12-31
-- Description: 인트라넷 시스템 시퀀스 생성 스크립트
-- ============================================

-- ============================================
-- 1. DEPARTMENTS_INTRANET SEQUENCE
-- ============================================
CREATE SEQUENCE departments_intranet_seq
    START WITH 1
    INCREMENT BY 1
    NOCACHE
    NOCYCLE;

-- ============================================
-- 2. MEMBERS_INTRANET SEQUENCE
-- ============================================
CREATE SEQUENCE members_intranet_seq
    START WITH 1
    INCREMENT BY 1
    NOCACHE
    NOCYCLE;

-- ============================================
-- 3. DOCUMENTS_INTRANET SEQUENCE
-- ============================================
CREATE SEQUENCE documents_intranet_seq
    START WITH 1
    INCREMENT BY 1
    NOCACHE
    NOCYCLE;

-- ============================================
-- 4. LEAVE_REQUESTS_INTRANET SEQUENCE
-- ============================================
CREATE SEQUENCE leave_requests_intranet_seq
    START WITH 1
    INCREMENT BY 1
    NOCACHE
    NOCYCLE;

-- ============================================
-- 5. EXPENSE_REPORTS_INTRANET SEQUENCE
-- ============================================
CREATE SEQUENCE expense_reports_intranet_seq
    START WITH 1
    INCREMENT BY 1
    NOCACHE
    NOCYCLE;

-- ============================================
-- 6. EXPENSE_ITEMS_INTRANET SEQUENCE
-- ============================================
CREATE SEQUENCE expense_items_intranet_seq
    START WITH 1
    INCREMENT BY 1
    NOCACHE
    NOCYCLE;

-- ============================================
-- 7. APPROVAL_LINES_INTRANET SEQUENCE
-- ============================================
CREATE SEQUENCE approval_lines_intranet_seq
    START WITH 1
    INCREMENT BY 1
    NOCACHE
    NOCYCLE;

-- ============================================
-- 8. ATTACHMENTS_INTRANET SEQUENCE
-- ============================================
CREATE SEQUENCE attachments_intranet_seq
    START WITH 1
    INCREMENT BY 1
    NOCACHE
    NOCYCLE;

-- ============================================
-- 9. NOTICES_INTRANET SEQUENCE
-- ============================================
CREATE SEQUENCE notices_intranet_seq
    START WITH 1
    INCREMENT BY 1
    NOCACHE
    NOCYCLE;

-- ============================================
-- 10. SYSTEM_LOGS_INTRANET SEQUENCE
-- ============================================
CREATE SEQUENCE system_logs_intranet_seq
    START WITH 1
    INCREMENT BY 1
    NOCACHE
    NOCYCLE;

-- ============================================
-- 11. EMAIL_TEMPLATES_INTRANET SEQUENCE
-- ============================================
CREATE SEQUENCE email_templates_intranet_seq
    START WITH 1
    INCREMENT BY 1
    NOCACHE
    NOCYCLE;

-- ============================================
-- END OF SEQUENCE CREATION SCRIPT
-- ============================================

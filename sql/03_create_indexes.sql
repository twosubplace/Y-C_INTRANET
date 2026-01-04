-- ============================================
-- YNC INTRANET SYSTEM - INDEX CREATION SCRIPT
-- ============================================
-- Version: 1.0
-- Created: 2025-12-31
-- Description: 인트라넷 시스템 인덱스 생성 스크립트
-- ============================================

-- ============================================
-- 1. DEPARTMENTS_INTRANET INDEXES
-- ============================================
CREATE INDEX idx_dept_intra_parent ON departments_intranet(parent_id);
CREATE INDEX idx_dept_intra_manager ON departments_intranet(manager_id);
CREATE INDEX idx_dept_intra_active ON departments_intranet(is_active);

-- ============================================
-- 2. MEMBERS_INTRANET INDEXES
-- ============================================
CREATE INDEX idx_member_intra_email ON members_intranet(email);
CREATE INDEX idx_member_intra_dept ON members_intranet(department_id);
CREATE INDEX idx_member_intra_role ON members_intranet(role);
CREATE INDEX idx_member_intra_active ON members_intranet(is_active);
CREATE INDEX idx_member_intra_hire ON members_intranet(hire_date);

-- ============================================
-- 3. DOCUMENTS_INTRANET INDEXES
-- ============================================
CREATE INDEX idx_doc_intra_author ON documents_intranet(author_id);
CREATE INDEX idx_doc_intra_type ON documents_intranet(document_type);
CREATE INDEX idx_doc_intra_status ON documents_intranet(status);
CREATE INDEX idx_doc_intra_created ON documents_intranet(created_at DESC);
CREATE INDEX idx_doc_intra_submitted ON documents_intranet(submitted_at DESC);
CREATE INDEX idx_doc_intra_approved ON documents_intranet(approved_at DESC);
CREATE INDEX idx_doc_intra_type_status ON documents_intranet(document_type, status);
CREATE INDEX idx_doc_intra_author_status ON documents_intranet(author_id, status);

-- ============================================
-- 4. LEAVE_REQUESTS_INTRANET INDEXES
-- ============================================
CREATE INDEX idx_leave_intra_doc ON leave_requests_intranet(document_id);
CREATE INDEX idx_leave_intra_type ON leave_requests_intranet(leave_type);
CREATE INDEX idx_leave_intra_start ON leave_requests_intranet(start_date);
CREATE INDEX idx_leave_intra_end ON leave_requests_intranet(end_date);
CREATE INDEX idx_leave_intra_dates ON leave_requests_intranet(start_date, end_date);

-- ============================================
-- 5. EXPENSE_REPORTS_INTRANET INDEXES
-- ============================================
CREATE INDEX idx_expense_intra_doc ON expense_reports_intranet(document_id);
CREATE INDEX idx_expense_intra_month ON expense_reports_intranet(report_month);

-- ============================================
-- 6. EXPENSE_ITEMS_INTRANET INDEXES
-- ============================================
CREATE INDEX idx_expitem_intra_report ON expense_items_intranet(expense_report_id);
CREATE INDEX idx_expitem_intra_category ON expense_items_intranet(category);
CREATE INDEX idx_expitem_intra_date ON expense_items_intranet(expense_date);

-- ============================================
-- 7. APPROVAL_LINES_INTRANET INDEXES
-- ============================================
CREATE INDEX idx_approval_intra_doc ON approval_lines_intranet(document_id);
CREATE INDEX idx_approval_intra_approver ON approval_lines_intranet(approver_id);
CREATE INDEX idx_approval_intra_decision ON approval_lines_intranet(decision);
CREATE INDEX idx_approval_intra_step ON approval_lines_intranet(step_order);
CREATE INDEX idx_approval_intra_submitted ON approval_lines_intranet(submitted_at DESC);
CREATE INDEX idx_approval_intra_decided ON approval_lines_intranet(decided_at DESC);
CREATE INDEX idx_approval_intra_pending ON approval_lines_intranet(approver_id, decision);

-- ============================================
-- 8. ATTACHMENTS_INTRANET INDEXES
-- ============================================
CREATE INDEX idx_attach_intra_doc ON attachments_intranet(document_id);
CREATE INDEX idx_attach_intra_uploader ON attachments_intranet(uploaded_by);
CREATE INDEX idx_attach_intra_uploaded ON attachments_intranet(uploaded_at DESC);

-- ============================================
-- 9. NOTICES_INTRANET INDEXES
-- ============================================
CREATE INDEX idx_notice_intra_author ON notices_intranet(author_id);
CREATE INDEX idx_notice_intra_created ON notices_intranet(created_at DESC);
CREATE INDEX idx_notice_intra_pinned ON notices_intranet(is_pinned, created_at DESC);

-- ============================================
-- 10. SYSTEM_LOGS_INTRANET INDEXES
-- ============================================
CREATE INDEX idx_log_intra_user ON system_logs_intranet(user_id);
CREATE INDEX idx_log_intra_action ON system_logs_intranet(action);
CREATE INDEX idx_log_intra_created ON system_logs_intranet(created_at DESC);
CREATE INDEX idx_log_intra_target ON system_logs_intranet(target_type, target_id);
CREATE INDEX idx_log_intra_user_action ON system_logs_intranet(user_id, action);

-- ============================================
-- 11. COMMON_CODES_INTRANET INDEXES
-- ============================================
CREATE INDEX idx_code_intra_type ON common_codes_intranet(code_type);
CREATE INDEX idx_code_intra_active ON common_codes_intranet(is_active);

-- ============================================
-- 12. EMAIL_TEMPLATES_INTRANET INDEXES
-- ============================================
CREATE INDEX idx_emailtpl_intra_type ON email_templates_intranet(template_type);
CREATE INDEX idx_emailtpl_intra_active ON email_templates_intranet(is_active);

-- ============================================
-- END OF INDEX CREATION SCRIPT
-- ============================================

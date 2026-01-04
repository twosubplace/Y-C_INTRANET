-- ============================================
-- YNC INTRANET SYSTEM - TABLE CREATION SCRIPT
-- ============================================
-- Version: 1.0
-- Created: 2025-12-31
-- Description: 인트라넷 시스템 테이블 생성 스크립트
-- ============================================

-- ============================================
-- 1. DEPARTMENTS_INTRANET (부서)
-- ============================================
CREATE TABLE departments_intranet (
    id NUMBER PRIMARY KEY,
    name VARCHAR2(100) NOT NULL UNIQUE,
    parent_id NUMBER,
    manager_id NUMBER,
    display_order NUMBER DEFAULT 0,
    is_active NUMBER(1) DEFAULT 1,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_dept_intra_parent FOREIGN KEY (parent_id)
        REFERENCES departments_intranet(id)
);

COMMENT ON TABLE departments_intranet IS '부서 정보';
COMMENT ON COLUMN departments_intranet.id IS '부서 ID';
COMMENT ON COLUMN departments_intranet.name IS '부서명';
COMMENT ON COLUMN departments_intranet.parent_id IS '상위 부서 ID';
COMMENT ON COLUMN departments_intranet.manager_id IS '부서장 ID';
COMMENT ON COLUMN departments_intranet.display_order IS '표시 순서';
COMMENT ON COLUMN departments_intranet.is_active IS '활성화 여부 (1:활성, 0:비활성)';

-- ============================================
-- 2. MEMBERS_INTRANET (사원)
-- ============================================
CREATE TABLE members_intranet (
    id NUMBER PRIMARY KEY,
    email VARCHAR2(100) UNIQUE NOT NULL,
    password VARCHAR2(100) NOT NULL,
    name VARCHAR2(50) NOT NULL,
    phone VARCHAR2(20),
    department_id NUMBER,
    position VARCHAR2(50),
    role VARCHAR2(20) DEFAULT 'USER',
    hire_date DATE,
    annual_leave_granted NUMBER(5,2) DEFAULT 15,
    is_active NUMBER(1) DEFAULT 1,
    smtp_password VARCHAR2(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_member_intra_dept FOREIGN KEY (department_id)
        REFERENCES departments_intranet(id),
    CONSTRAINT chk_member_intra_role CHECK (role IN ('USER', 'APPROVER', 'ADMIN'))
);

COMMENT ON TABLE members_intranet IS '사원 정보';
COMMENT ON COLUMN members_intranet.id IS '사원 ID';
COMMENT ON COLUMN members_intranet.email IS '이메일 (로그인 ID)';
COMMENT ON COLUMN members_intranet.password IS '비밀번호 (BCrypt 암호화)';
COMMENT ON COLUMN members_intranet.name IS '이름';
COMMENT ON COLUMN members_intranet.phone IS '전화번호';
COMMENT ON COLUMN members_intranet.department_id IS '부서 ID';
COMMENT ON COLUMN members_intranet.position IS '직급';
COMMENT ON COLUMN members_intranet.role IS '권한 (USER, APPROVER, ADMIN)';
COMMENT ON COLUMN members_intranet.hire_date IS '입사일';
COMMENT ON COLUMN members_intranet.annual_leave_granted IS '연차 부여 일수';
COMMENT ON COLUMN members_intranet.is_active IS '활성화 여부 (1:재직, 0:퇴사)';
COMMENT ON COLUMN members_intranet.smtp_password IS 'Naver Works SMTP 비밀번호';

-- ============================================
-- 3. DOCUMENTS_INTRANET (문서 통합)
-- ============================================
CREATE TABLE documents_intranet (
    id NUMBER PRIMARY KEY,
    document_type VARCHAR2(20) NOT NULL,
    author_id NUMBER NOT NULL,
    title VARCHAR2(200) NOT NULL,
    content CLOB,
    status VARCHAR2(20) DEFAULT 'DRAFT',
    metadata CLOB,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    submitted_at TIMESTAMP,
    approved_at TIMESTAMP,
    CONSTRAINT fk_doc_intra_author FOREIGN KEY (author_id)
        REFERENCES members_intranet(id),
    CONSTRAINT chk_doc_intra_type CHECK (document_type IN ('LEAVE', 'EXPENSE', 'GENERAL')),
    CONSTRAINT chk_doc_intra_status CHECK (status IN ('DRAFT', 'PENDING', 'APPROVED', 'REJECTED', 'CANCELED'))
);

COMMENT ON TABLE documents_intranet IS '문서 통합 테이블';
COMMENT ON COLUMN documents_intranet.id IS '문서 ID';
COMMENT ON COLUMN documents_intranet.document_type IS '문서 유형 (LEAVE:휴가, EXPENSE:경비, GENERAL:일반)';
COMMENT ON COLUMN documents_intranet.author_id IS '작성자 ID';
COMMENT ON COLUMN documents_intranet.title IS '제목';
COMMENT ON COLUMN documents_intranet.content IS '내용';
COMMENT ON COLUMN documents_intranet.status IS '상태 (DRAFT:임시저장, PENDING:결재중, APPROVED:승인, REJECTED:반려, CANCELED:취소)';
COMMENT ON COLUMN documents_intranet.metadata IS '추가 메타데이터 (JSON 형식)';
COMMENT ON COLUMN documents_intranet.submitted_at IS '결재 상신일시';
COMMENT ON COLUMN documents_intranet.approved_at IS '최종 승인일시';

-- ============================================
-- 4. LEAVE_REQUESTS_INTRANET (휴가 신청)
-- ============================================
CREATE TABLE leave_requests_intranet (
    id NUMBER PRIMARY KEY,
    document_id NUMBER NOT NULL UNIQUE,
    leave_type VARCHAR2(50) NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    start_time VARCHAR2(10),
    end_time VARCHAR2(10),
    leave_days NUMBER(5,2) NOT NULL,
    reason VARCHAR2(500),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_leave_intra_doc FOREIGN KEY (document_id)
        REFERENCES documents_intranet(id) ON DELETE CASCADE
);

COMMENT ON TABLE leave_requests_intranet IS '휴가 신청 상세';
COMMENT ON COLUMN leave_requests_intranet.id IS '휴가 신청 ID';
COMMENT ON COLUMN leave_requests_intranet.document_id IS '문서 ID';
COMMENT ON COLUMN leave_requests_intranet.leave_type IS '휴가 유형 (연차, 반차, 병가, 경조사 등)';
COMMENT ON COLUMN leave_requests_intranet.start_date IS '시작일';
COMMENT ON COLUMN leave_requests_intranet.end_date IS '종료일';
COMMENT ON COLUMN leave_requests_intranet.start_time IS '시작 시간 (반차용)';
COMMENT ON COLUMN leave_requests_intranet.end_time IS '종료 시간 (반차용)';
COMMENT ON COLUMN leave_requests_intranet.leave_days IS '차감 일수';
COMMENT ON COLUMN leave_requests_intranet.reason IS '휴가 사유';

-- ============================================
-- 5. EXPENSE_REPORTS_INTRANET (경비보고서)
-- ============================================
CREATE TABLE expense_reports_intranet (
    id NUMBER PRIMARY KEY,
    document_id NUMBER NOT NULL UNIQUE,
    report_month DATE NOT NULL,
    total_amount NUMBER(12,2) DEFAULT 0,
    description VARCHAR2(500),
    file_path VARCHAR2(500),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_expense_intra_doc FOREIGN KEY (document_id)
        REFERENCES documents_intranet(id) ON DELETE CASCADE
);

COMMENT ON TABLE expense_reports_intranet IS '경비보고서';
COMMENT ON COLUMN expense_reports_intranet.id IS '경비보고서 ID';
COMMENT ON COLUMN expense_reports_intranet.document_id IS '문서 ID';
COMMENT ON COLUMN expense_reports_intranet.report_month IS '보고 월';
COMMENT ON COLUMN expense_reports_intranet.total_amount IS '총 금액';
COMMENT ON COLUMN expense_reports_intranet.description IS '설명';
COMMENT ON COLUMN expense_reports_intranet.file_path IS '첨부파일 경로';

-- ============================================
-- 6. EXPENSE_ITEMS_INTRANET (경비 항목)
-- ============================================
CREATE TABLE expense_items_intranet (
    id NUMBER PRIMARY KEY,
    expense_report_id NUMBER NOT NULL,
    expense_date DATE NOT NULL,
    category VARCHAR2(50) NOT NULL,
    amount NUMBER(12,2) NOT NULL,
    description VARCHAR2(200),
    receipt_path VARCHAR2(500),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_item_intra_expense FOREIGN KEY (expense_report_id)
        REFERENCES expense_reports_intranet(id) ON DELETE CASCADE
);

COMMENT ON TABLE expense_items_intranet IS '경비 항목 상세';
COMMENT ON COLUMN expense_items_intranet.id IS '경비 항목 ID';
COMMENT ON COLUMN expense_items_intranet.expense_report_id IS '경비보고서 ID';
COMMENT ON COLUMN expense_items_intranet.expense_date IS '지출일';
COMMENT ON COLUMN expense_items_intranet.category IS '카테고리 (식비, 교통비, 복지비 등)';
COMMENT ON COLUMN expense_items_intranet.amount IS '금액';
COMMENT ON COLUMN expense_items_intranet.description IS '상세 내역';
COMMENT ON COLUMN expense_items_intranet.receipt_path IS '영수증 파일 경로';

-- ============================================
-- 7. APPROVAL_LINES_INTRANET (결재선)
-- ============================================
CREATE TABLE approval_lines_intranet (
    id NUMBER PRIMARY KEY,
    document_id NUMBER NOT NULL,
    step_order NUMBER NOT NULL,
    approver_id NUMBER NOT NULL,
    approver_name VARCHAR2(50),
    approver_position VARCHAR2(50),
    decision VARCHAR2(20) DEFAULT 'PENDING',
    approval_comment VARCHAR2(500),
    submitted_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    decided_at TIMESTAMP,
    CONSTRAINT fk_approval_intra_doc FOREIGN KEY (document_id)
        REFERENCES documents_intranet(id) ON DELETE CASCADE,
    CONSTRAINT fk_approval_intra_approver FOREIGN KEY (approver_id)
        REFERENCES members_intranet(id),
    CONSTRAINT chk_approval_intra_decision CHECK (decision IN ('PENDING', 'APPROVED', 'REJECTED')),
    CONSTRAINT uk_approval_intra_line UNIQUE (document_id, step_order)
);

COMMENT ON TABLE approval_lines_intranet IS '결재선';
COMMENT ON COLUMN approval_lines_intranet.id IS '결재선 ID';
COMMENT ON COLUMN approval_lines_intranet.document_id IS '문서 ID';
COMMENT ON COLUMN approval_lines_intranet.step_order IS '결재 순서 (1, 2, 3...)';
COMMENT ON COLUMN approval_lines_intranet.approver_id IS '결재자 ID';
COMMENT ON COLUMN approval_lines_intranet.approver_name IS '결재자 이름 (스냅샷)';
COMMENT ON COLUMN approval_lines_intranet.approver_position IS '결재자 직급 (스냅샷)';
COMMENT ON COLUMN approval_lines_intranet.decision IS '결재 결과 (PENDING:대기, APPROVED:승인, REJECTED:반려)';
COMMENT ON COLUMN approval_lines_intranet.approval_comment IS '결재 의견';
COMMENT ON COLUMN approval_lines_intranet.submitted_at IS '결재 요청일시';
COMMENT ON COLUMN approval_lines_intranet.decided_at IS '결재 처리일시';

-- ============================================
-- 8. ATTACHMENTS_INTRANET (첨부파일)
-- ============================================
CREATE TABLE attachments_intranet (
    id NUMBER PRIMARY KEY,
    document_id NUMBER NOT NULL,
    file_name VARCHAR2(255) NOT NULL,
    file_path VARCHAR2(500) NOT NULL,
    file_size NUMBER,
    file_type VARCHAR2(50),
    uploaded_by NUMBER NOT NULL,
    uploaded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_attach_intra_doc FOREIGN KEY (document_id)
        REFERENCES documents_intranet(id) ON DELETE CASCADE,
    CONSTRAINT fk_attach_intra_uploader FOREIGN KEY (uploaded_by)
        REFERENCES members_intranet(id)
);

COMMENT ON TABLE attachments_intranet IS '첨부파일';
COMMENT ON COLUMN attachments_intranet.id IS '첨부파일 ID';
COMMENT ON COLUMN attachments_intranet.document_id IS '문서 ID';
COMMENT ON COLUMN attachments_intranet.file_name IS '파일명';
COMMENT ON COLUMN attachments_intranet.file_path IS '파일 경로';
COMMENT ON COLUMN attachments_intranet.file_size IS '파일 크기 (bytes)';
COMMENT ON COLUMN attachments_intranet.file_type IS '파일 타입 (MIME type)';
COMMENT ON COLUMN attachments_intranet.uploaded_by IS '업로드한 사용자 ID';
COMMENT ON COLUMN attachments_intranet.uploaded_at IS '업로드 일시';

-- ============================================
-- 9. NOTICES_INTRANET (공지사항)
-- ============================================
CREATE TABLE notices_intranet (
    id NUMBER PRIMARY KEY,
    title VARCHAR2(200) NOT NULL,
    content CLOB NOT NULL,
    author_id NUMBER NOT NULL,
    is_pinned NUMBER(1) DEFAULT 0,
    view_count NUMBER DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_notice_intra_author FOREIGN KEY (author_id)
        REFERENCES members_intranet(id)
);

COMMENT ON TABLE notices_intranet IS '공지사항';
COMMENT ON COLUMN notices_intranet.id IS '공지사항 ID';
COMMENT ON COLUMN notices_intranet.title IS '제목';
COMMENT ON COLUMN notices_intranet.content IS '내용';
COMMENT ON COLUMN notices_intranet.author_id IS '작성자 ID';
COMMENT ON COLUMN notices_intranet.is_pinned IS '상단 고정 여부 (1:고정, 0:일반)';
COMMENT ON COLUMN notices_intranet.view_count IS '조회수';

-- ============================================
-- 10. SYSTEM_LOGS_INTRANET (시스템 로그)
-- ============================================
CREATE TABLE system_logs_intranet (
    id NUMBER PRIMARY KEY,
    user_id NUMBER,
    action VARCHAR2(100) NOT NULL,
    target_type VARCHAR2(50),
    target_id NUMBER,
    ip_address VARCHAR2(50),
    user_agent VARCHAR2(500),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_log_intra_user FOREIGN KEY (user_id)
        REFERENCES members_intranet(id)
);

COMMENT ON TABLE system_logs_intranet IS '시스템 접근 로그';
COMMENT ON COLUMN system_logs_intranet.id IS '로그 ID';
COMMENT ON COLUMN system_logs_intranet.user_id IS '사용자 ID';
COMMENT ON COLUMN system_logs_intranet.action IS '액션 (LOGIN, LOGOUT, APPROVE, REJECT 등)';
COMMENT ON COLUMN system_logs_intranet.target_type IS '대상 타입 (DOCUMENT, MEMBER 등)';
COMMENT ON COLUMN system_logs_intranet.target_id IS '대상 ID';
COMMENT ON COLUMN system_logs_intranet.ip_address IS 'IP 주소';
COMMENT ON COLUMN system_logs_intranet.user_agent IS 'User Agent';

-- ============================================
-- 11. COMMON_CODES_INTRANET (공통 코드)
-- ============================================
CREATE TABLE common_codes_intranet (
    code_type VARCHAR2(50) NOT NULL,
    code VARCHAR2(50) NOT NULL,
    name VARCHAR2(100) NOT NULL,
    display_order NUMBER DEFAULT 0,
    is_active NUMBER(1) DEFAULT 1,
    description VARCHAR2(200),
    PRIMARY KEY (code_type, code)
);

COMMENT ON TABLE common_codes_intranet IS '공통 코드';
COMMENT ON COLUMN common_codes_intranet.code_type IS '코드 타입 (LEAVE_TYPE, EXPENSE_CATEGORY 등)';
COMMENT ON COLUMN common_codes_intranet.code IS '코드';
COMMENT ON COLUMN common_codes_intranet.name IS '코드명';
COMMENT ON COLUMN common_codes_intranet.display_order IS '표시 순서';
COMMENT ON COLUMN common_codes_intranet.is_active IS '활성화 여부';
COMMENT ON COLUMN common_codes_intranet.description IS '설명';

-- ============================================
-- 12. EMAIL_TEMPLATES_INTRANET (메일 템플릿)
-- ============================================
CREATE TABLE email_templates_intranet (
    id NUMBER PRIMARY KEY,
    template_type VARCHAR2(50) NOT NULL UNIQUE,
    subject VARCHAR2(200) NOT NULL,
    body CLOB NOT NULL,
    variables VARCHAR2(500),
    is_active NUMBER(1) DEFAULT 1,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

COMMENT ON TABLE email_templates_intranet IS '메일 템플릿';
COMMENT ON COLUMN email_templates_intranet.id IS '템플릿 ID';
COMMENT ON COLUMN email_templates_intranet.template_type IS '템플릿 타입 (LEAVE_APPROVAL, EXPENSE_APPROVAL 등)';
COMMENT ON COLUMN email_templates_intranet.subject IS '메일 제목';
COMMENT ON COLUMN email_templates_intranet.body IS '메일 본문';
COMMENT ON COLUMN email_templates_intranet.variables IS '사용 가능한 변수 (JSON 형식)';
COMMENT ON COLUMN email_templates_intranet.is_active IS '활성화 여부';

-- ============================================
-- END OF TABLE CREATION SCRIPT
-- ============================================

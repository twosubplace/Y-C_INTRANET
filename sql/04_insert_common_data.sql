-- ============================================
-- YNC INTRANET SYSTEM - INITIAL DATA INSERT SCRIPT
-- ============================================
-- Version: 1.0
-- Created: 2025-12-31
-- Description: 인트라넷 시스템 기본 데이터 삽입 스크립트
-- ============================================

-- ============================================
-- 1. COMMON_CODES_INTRANET - 공통 코드
-- ============================================

-- 휴가 유형
INSERT INTO common_codes_intranet (code_type, code, name, display_order, is_active, description)
VALUES ('LEAVE_TYPE', 'ANNUAL', '연차', 1, 1, '연차 휴가');

INSERT INTO common_codes_intranet (code_type, code, name, display_order, is_active, description)
VALUES ('LEAVE_TYPE', 'HALF_AM', '오전 반차', 2, 1, '오전 반차');

INSERT INTO common_codes_intranet (code_type, code, name, display_order, is_active, description)
VALUES ('LEAVE_TYPE', 'HALF_PM', '오후 반차', 3, 1, '오후 반차');

INSERT INTO common_codes_intranet (code_type, code, name, display_order, is_active, description)
VALUES ('LEAVE_TYPE', 'SICK', '병가', 4, 1, '병가');

INSERT INTO common_codes_intranet (code_type, code, name, display_order, is_active, description)
VALUES ('LEAVE_TYPE', 'SPECIAL', '경조사', 5, 1, '경조사 휴가');

INSERT INTO common_codes_intranet (code_type, code, name, display_order, is_active, description)
VALUES ('LEAVE_TYPE', 'MATERNITY', '출산휴가', 6, 1, '출산 휴가');

INSERT INTO common_codes_intranet (code_type, code, name, display_order, is_active, description)
VALUES ('LEAVE_TYPE', 'CHILDCARE', '육아휴직', 7, 1, '육아 휴직');

-- 경비 카테고리
INSERT INTO common_codes_intranet (code_type, code, name, display_order, is_active, description)
VALUES ('EXPENSE_CATEGORY', 'MEAL', '식비', 1, 1, '식대, 회식비');

INSERT INTO common_codes_intranet (code_type, code, name, display_order, is_active, description)
VALUES ('EXPENSE_CATEGORY', 'TRANSPORT', '교통비', 2, 1, '대중교통, 택시, 주차비');

INSERT INTO common_codes_intranet (code_type, code, name, display_order, is_active, description)
VALUES ('EXPENSE_CATEGORY', 'WELFARE', '복지비', 3, 1, '복지 포인트 사용');

INSERT INTO common_codes_intranet (code_type, code, name, display_order, is_active, description)
VALUES ('EXPENSE_CATEGORY', 'OFFICE', '사무용품', 4, 1, '사무용품 구매비');

INSERT INTO common_codes_intranet (code_type, code, name, display_order, is_active, description)
VALUES ('EXPENSE_CATEGORY', 'COMMUNICATION', '통신비', 5, 1, '휴대폰, 인터넷');

INSERT INTO common_codes_intranet (code_type, code, name, display_order, is_active, description)
VALUES ('EXPENSE_CATEGORY', 'EDUCATION', '교육비', 6, 1, '교육, 도서 구매');

INSERT INTO common_codes_intranet (code_type, code, name, display_order, is_active, description)
VALUES ('EXPENSE_CATEGORY', 'ETC', '기타', 99, 1, '기타 경비');

-- 직급
INSERT INTO common_codes_intranet (code_type, code, name, display_order, is_active, description)
VALUES ('POSITION', 'STAFF', '사원', 1, 1, '사원');

INSERT INTO common_codes_intranet (code_type, code, name, display_order, is_active, description)
VALUES ('POSITION', 'SENIOR', '주임', 2, 1, '주임');

INSERT INTO common_codes_intranet (code_type, code, name, display_order, is_active, description)
VALUES ('POSITION', 'ASSISTANT_MANAGER', '대리', 3, 1, '대리');

INSERT INTO common_codes_intranet (code_type, code, name, display_order, is_active, description)
VALUES ('POSITION', 'MANAGER', '과장', 4, 1, '과장');

INSERT INTO common_codes_intranet (code_type, code, name, display_order, is_active, description)
VALUES ('POSITION', 'DEPUTY_MANAGER', '차장', 5, 1, '차장');

INSERT INTO common_codes_intranet (code_type, code, name, display_order, is_active, description)
VALUES ('POSITION', 'GENERAL_MANAGER', '부장', 6, 1, '부장');

INSERT INTO common_codes_intranet (code_type, code, name, display_order, is_active, description)
VALUES ('POSITION', 'DIRECTOR', '이사', 7, 1, '이사');

INSERT INTO common_codes_intranet (code_type, code, name, display_order, is_active, description)
VALUES ('POSITION', 'EXECUTIVE_DIRECTOR', '상무', 8, 1, '상무');

INSERT INTO common_codes_intranet (code_type, code, name, display_order, is_active, description)
VALUES ('POSITION', 'CEO', '대표', 9, 1, '대표이사');

COMMIT;

-- ============================================
-- 2. DEPARTMENTS_INTRANET - 기본 부서
-- ============================================

INSERT INTO departments_intranet (id, name, parent_id, manager_id, display_order, is_active)
VALUES (departments_intranet_seq.NEXTVAL, '경영지원팀', NULL, NULL, 1, 1);

INSERT INTO departments_intranet (id, name, parent_id, manager_id, display_order, is_active)
VALUES (departments_intranet_seq.NEXTVAL, '개발팀', NULL, NULL, 2, 1);

INSERT INTO departments_intranet (id, name, parent_id, manager_id, display_order, is_active)
VALUES (departments_intranet_seq.NEXTVAL, '영업팀', NULL, NULL, 3, 1);

COMMIT;

-- ============================================
-- 3. MEMBERS_INTRANET - 관리자 계정
-- ============================================
-- 비밀번호: admin1234 (BCrypt: $2a$10$...)
-- 실제 운영 시 BCrypt로 암호화된 비밀번호로 변경 필요

INSERT INTO members_intranet (
    id, email, password, name, phone, department_id, position, role,
    hire_date, annual_leave_granted, is_active, smtp_password
)
VALUES (
    members_intranet_seq.NEXTVAL,
    'admin@yncsmart.com',
    '$2a$10$N9qo8uLOickgx2ZNqJLqrOeQXLgZgZ6wYPPWlKdFJLqVWrLVj1Vka',  -- admin1234
    '시스템관리자',
    '010-0000-0000',
    1,
    'DIRECTOR',
    'ADMIN',
    TO_DATE('2020-01-01', 'YYYY-MM-DD'),
    15,
    1,
    NULL
);

COMMIT;

-- ============================================
-- 4. EMAIL_TEMPLATES_INTRANET - 메일 템플릿
-- ============================================

-- 휴가 승인 메일
INSERT INTO email_templates_intranet (
    id, template_type, subject, body, variables, is_active
)
VALUES (
    email_templates_intranet_seq.NEXTVAL,
    'LEAVE_APPROVAL',
    '[YNC] 휴가 신청이 승인되었습니다',
    '<html>
<body>
    <h2>휴가 신청 승인</h2>
    <p>안녕하세요, {{memberName}}님</p>
    <p>신청하신 휴가가 승인되었습니다.</p>

    <table border="1" cellpadding="10" style="border-collapse: collapse;">
        <tr>
            <th>휴가 유형</th>
            <td>{{leaveType}}</td>
        </tr>
        <tr>
            <th>휴가 기간</th>
            <td>{{startDate}} ~ {{endDate}}</td>
        </tr>
        <tr>
            <th>승인자</th>
            <td>{{approverName}}</td>
        </tr>
        <tr>
            <th>승인일</th>
            <td>{{approvalDate}}</td>
        </tr>
    </table>

    <p>즐거운 휴가 되시기 바랍니다.</p>
</body>
</html>',
    '["memberName", "leaveType", "startDate", "endDate", "approverName", "approvalDate"]',
    1
);

-- 휴가 반려 메일
INSERT INTO email_templates_intranet (
    id, template_type, subject, body, variables, is_active
)
VALUES (
    email_templates_intranet_seq.NEXTVAL,
    'LEAVE_REJECTION',
    '[YNC] 휴가 신청이 반려되었습니다',
    '<html>
<body>
    <h2>휴가 신청 반려</h2>
    <p>안녕하세요, {{memberName}}님</p>
    <p>신청하신 휴가가 반려되었습니다.</p>

    <table border="1" cellpadding="10" style="border-collapse: collapse;">
        <tr>
            <th>휴가 유형</th>
            <td>{{leaveType}}</td>
        </tr>
        <tr>
            <th>휴가 기간</th>
            <td>{{startDate}} ~ {{endDate}}</td>
        </tr>
        <tr>
            <th>반려자</th>
            <td>{{approverName}}</td>
        </tr>
        <tr>
            <th>반려 사유</th>
            <td>{{comment}}</td>
        </tr>
    </table>

    <p>문의사항이 있으시면 결재자에게 연락 부탁드립니다.</p>
</body>
</html>',
    '["memberName", "leaveType", "startDate", "endDate", "approverName", "comment"]',
    1
);

-- 경비보고서 승인 메일
INSERT INTO email_templates_intranet (
    id, template_type, subject, body, variables, is_active
)
VALUES (
    email_templates_intranet_seq.NEXTVAL,
    'EXPENSE_APPROVAL',
    '[YNC] 경비보고서가 승인되었습니다',
    '<html>
<body>
    <h2>경비보고서 승인</h2>
    <p>안녕하세요, {{memberName}}님</p>
    <p>제출하신 경비보고서가 승인되었습니다.</p>

    <table border="1" cellpadding="10" style="border-collapse: collapse;">
        <tr>
            <th>보고서 제목</th>
            <td>{{title}}</td>
        </tr>
        <tr>
            <th>보고 월</th>
            <td>{{reportMonth}}</td>
        </tr>
        <tr>
            <th>총 금액</th>
            <td>{{totalAmount}}원</td>
        </tr>
        <tr>
            <th>승인자</th>
            <td>{{approverName}}</td>
        </tr>
    </table>
</body>
</html>',
    '["memberName", "title", "reportMonth", "totalAmount", "approverName"]',
    1
);

-- 경비보고서 반려 메일
INSERT INTO email_templates_intranet (
    id, template_type, subject, body, variables, is_active
)
VALUES (
    email_templates_intranet_seq.NEXTVAL,
    'EXPENSE_REJECTION',
    '[YNC] 경비보고서가 반려되었습니다',
    '<html>
<body>
    <h2>경비보고서 반려</h2>
    <p>안녕하세요, {{memberName}}님</p>
    <p>제출하신 경비보고서가 반려되었습니다.</p>

    <table border="1" cellpadding="10" style="border-collapse: collapse;">
        <tr>
            <th>보고서 제목</th>
            <td>{{title}}</td>
        </tr>
        <tr>
            <th>보고 월</th>
            <td>{{reportMonth}}</td>
        </tr>
        <tr>
            <th>총 금액</th>
            <td>{{totalAmount}}원</td>
        </tr>
        <tr>
            <th>반려자</th>
            <td>{{approverName}}</td>
        </tr>
        <tr>
            <th>반려 사유</th>
            <td>{{comment}}</td>
        </tr>
    </table>

    <p>수정 후 재제출 부탁드립니다.</p>
</body>
</html>',
    '["memberName", "title", "reportMonth", "totalAmount", "approverName", "comment"]',
    1
);

-- 결재 요청 알림 메일
INSERT INTO email_templates_intranet (
    id, template_type, subject, body, variables, is_active
)
VALUES (
    email_templates_intranet_seq.NEXTVAL,
    'APPROVAL_REQUEST',
    '[YNC] 결재 요청이 도착했습니다',
    '<html>
<body>
    <h2>결재 요청</h2>
    <p>안녕하세요, {{approverName}}님</p>
    <p>새로운 결재 요청이 도착했습니다.</p>

    <table border="1" cellpadding="10" style="border-collapse: collapse;">
        <tr>
            <th>문서 유형</th>
            <td>{{documentType}}</td>
        </tr>
        <tr>
            <th>제목</th>
            <td>{{title}}</td>
        </tr>
        <tr>
            <th>작성자</th>
            <td>{{authorName}}</td>
        </tr>
        <tr>
            <th>요청일</th>
            <td>{{requestDate}}</td>
        </tr>
    </table>

    <p>인트라넷에 접속하여 결재를 진행해 주세요.</p>
    <p><a href="{{intranetUrl}}">인트라넷 바로가기</a></p>
</body>
</html>',
    '["approverName", "documentType", "title", "authorName", "requestDate", "intranetUrl"]',
    1
);

COMMIT;

-- ============================================
-- END OF INITIAL DATA INSERT SCRIPT
-- ============================================

-- 실행 결과 확인
SELECT '공통 코드: ' || COUNT(*) || '건' AS result FROM common_codes_intranet
UNION ALL
SELECT '부서: ' || COUNT(*) || '건' FROM departments_intranet
UNION ALL
SELECT '사용자: ' || COUNT(*) || '건' FROM members_intranet
UNION ALL
SELECT '메일 템플릿: ' || COUNT(*) || '건' FROM email_templates_intranet;

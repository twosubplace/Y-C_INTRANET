-- Create email templates table
CREATE TABLE EMAIL_TEMPLATES (
    id NUMBER PRIMARY KEY,
    template_name VARCHAR2(100) NOT NULL UNIQUE,
    subject VARCHAR2(500) NOT NULL,
    content CLOB NOT NULL,
    description VARCHAR2(1000),
    is_active NUMBER(1) DEFAULT 1,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create sequence for email templates
CREATE SEQUENCE email_templates_seq START WITH 1 INCREMENT BY 1;

COMMENT ON TABLE EMAIL_TEMPLATES IS '이메일 템플릿';
COMMENT ON COLUMN EMAIL_TEMPLATES.id IS 'ID';
COMMENT ON COLUMN EMAIL_TEMPLATES.template_name IS '템플릿 이름 (LEAVE_APPLICATION, LEAVE_APPROVAL, LEAVE_REJECTION 등)';
COMMENT ON COLUMN EMAIL_TEMPLATES.subject IS '이메일 제목 (변수: ${memberName}, ${leaveType} 등)';
COMMENT ON COLUMN EMAIL_TEMPLATES.content IS 'HTML 이메일 본문 (변수: ${memberName}, ${leaveType}, ${startDate}, ${endDate} 등)';
COMMENT ON COLUMN EMAIL_TEMPLATES.description IS '템플릿 설명';
COMMENT ON COLUMN EMAIL_TEMPLATES.is_active IS '활성 여부 (1: 활성, 0: 비활성)';

-- Insert default templates
INSERT INTO EMAIL_TEMPLATES (id, template_name, subject, content, description, is_active) VALUES (
    email_templates_seq.NEXTVAL,
    'LEAVE_APPLICATION',
    '[휴가 신청] ${memberName}님의 ${leaveType} 신청',
    '<html>
<body style="font-family: ''Malgun Gothic'', ''Apple SD Gothic Neo'', sans-serif; padding: 20px; background-color: #f5f5f5;">
    <div style="max-width: 600px; margin: 0 auto; background-color: white; border-radius: 10px; padding: 30px; box-shadow: 0 2px 10px rgba(0,0,0,0.1);">
        <h2 style="color: #3b82f6; border-bottom: 3px solid #3b82f6; padding-bottom: 10px;">📅 휴가 신청 알림</h2>

        <div style="margin-top: 20px;">
            <p style="font-size: 16px; color: #333;">
                <strong>${memberName}</strong>님이 <strong style="color: #3b82f6;">${leaveType}</strong>를 신청했습니다.
            </p>

            <div style="background-color: #f8fafc; border-left: 4px solid #3b82f6; padding: 15px; margin: 20px 0; border-radius: 5px;">
                <p style="margin: 5px 0; color: #555;">
                    <strong>시작일:</strong> ${startDate}
                </p>
                <p style="margin: 5px 0; color: #555;">
                    <strong>종료일:</strong> ${endDate}
                </p>
                <p style="margin: 5px 0; color: #555;">
                    <strong>사용 일수:</strong> ${leaveAmount}일
                </p>
            </div>

            <p style="color: #666; font-size: 14px; margin-top: 20px;">
                휴가 관리 시스템에서 확인하고 승인해주세요.
            </p>
        </div>

        <div style="margin-top: 30px; padding-top: 20px; border-top: 1px solid #e5e7eb; text-align: center;">
            <p style="color: #999; font-size: 12px;">
                이 메일은 YNC Smart 휴가 관리 시스템에서 자동으로 발송되었습니다.
            </p>
        </div>
    </div>
</body>
</html>',
    '휴가 신청 알림 템플릿 - 변수: ${memberName}, ${leaveType}, ${startDate}, ${endDate}, ${leaveAmount}',
    1
);

INSERT INTO EMAIL_TEMPLATES (id, template_name, subject, content, description, is_active) VALUES (
    email_templates_seq.NEXTVAL,
    'LEAVE_APPROVAL',
    '[휴가 승인] ${memberName}님의 ${leaveType} 승인 완료',
    '<html>
<body style="font-family: ''Malgun Gothic'', ''Apple SD Gothic Neo'', sans-serif; padding: 20px; background-color: #f5f5f5;">
    <div style="max-width: 600px; margin: 0 auto; background-color: white; border-radius: 10px; padding: 30px; box-shadow: 0 2px 10px rgba(0,0,0,0.1);">
        <h2 style="color: #10b981; border-bottom: 3px solid #10b981; padding-bottom: 10px;">✅ 휴가 승인 완료</h2>

        <div style="margin-top: 20px;">
            <p style="font-size: 16px; color: #333;">
                <strong>${memberName}</strong>님, 신청하신 <strong style="color: #10b981;">${leaveType}</strong>가 승인되었습니다.
            </p>

            <div style="background-color: #f0fdf4; border-left: 4px solid #10b981; padding: 15px; margin: 20px 0; border-radius: 5px;">
                <p style="margin: 5px 0; color: #555;">
                    <strong>시작일:</strong> ${startDate}
                </p>
                <p style="margin: 5px 0; color: #555;">
                    <strong>종료일:</strong> ${endDate}
                </p>
                <p style="margin: 5px 0; color: #555;">
                    <strong>승인자:</strong> ${approverName}
                </p>
            </div>

            <p style="color: #666; font-size: 14px; margin-top: 20px;">
                즐거운 휴가 보내세요! 😊
            </p>
        </div>

        <div style="margin-top: 30px; padding-top: 20px; border-top: 1px solid #e5e7eb; text-align: center;">
            <p style="color: #999; font-size: 12px;">
                이 메일은 YNC Smart 휴가 관리 시스템에서 자동으로 발송되었습니다.
            </p>
        </div>
    </div>
</body>
</html>',
    '휴가 승인 알림 템플릿 - 변수: ${memberName}, ${leaveType}, ${startDate}, ${endDate}, ${approverName}',
    1
);

INSERT INTO EMAIL_TEMPLATES (id, template_name, subject, content, description, is_active) VALUES (
    email_templates_seq.NEXTVAL,
    'LEAVE_REJECTION',
    '[휴가 반려] ${memberName}님의 ${leaveType} 반려',
    '<html>
<body style="font-family: ''Malgun Gothic'', ''Apple SD Gothic Neo'', sans-serif; padding: 20px; background-color: #f5f5f5;">
    <div style="max-width: 600px; margin: 0 auto; background-color: white; border-radius: 10px; padding: 30px; box-shadow: 0 2px 10px rgba(0,0,0,0.1);">
        <h2 style="color: #ef4444; border-bottom: 3px solid #ef4444; padding-bottom: 10px;">❌ 휴가 반려</h2>

        <div style="margin-top: 20px;">
            <p style="font-size: 16px; color: #333;">
                <strong>${memberName}</strong>님, 신청하신 <strong style="color: #ef4444;">${leaveType}</strong>가 반려되었습니다.
            </p>

            <div style="background-color: #fef2f2; border-left: 4px solid #ef4444; padding: 15px; margin: 20px 0; border-radius: 5px;">
                <p style="margin: 5px 0; color: #555;">
                    <strong>시작일:</strong> ${startDate}
                </p>
                <p style="margin: 5px 0; color: #555;">
                    <strong>종료일:</strong> ${endDate}
                </p>
                <p style="margin: 5px 0; color: #555;">
                    <strong>반려자:</strong> ${approverName}
                </p>
                <p style="margin: 15px 0 5px 0; color: #555;">
                    <strong>반려 사유:</strong>
                </p>
                <p style="margin: 5px 0; color: #666; background-color: white; padding: 10px; border-radius: 3px;">
                    ${reason}
                </p>
            </div>

            <p style="color: #666; font-size: 14px; margin-top: 20px;">
                문의사항이 있으시면 반려자에게 연락해주세요.
            </p>
        </div>

        <div style="margin-top: 30px; padding-top: 20px; border-top: 1px solid #e5e7eb; text-align: center;">
            <p style="color: #999; font-size: 12px;">
                이 메일은 YNC Smart 휴가 관리 시스템에서 자동으로 발송되었습니다.
            </p>
        </div>
    </div>
</body>
</html>',
    '휴가 반려 알림 템플릿 - 변수: ${memberName}, ${leaveType}, ${startDate}, ${endDate}, ${approverName}, ${reason}',
    1
);

COMMIT;

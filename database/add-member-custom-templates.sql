-- Add custom email template columns to MEMBERS table
ALTER TABLE MEMBERS ADD custom_leave_application_template_id NUMBER;
ALTER TABLE MEMBERS ADD custom_leave_approval_template_id NUMBER;
ALTER TABLE MEMBERS ADD custom_leave_rejection_template_id NUMBER;
ALTER TABLE MEMBERS ADD email_signature CLOB;

COMMENT ON COLUMN MEMBERS.custom_leave_application_template_id IS '개인 커스텀 휴가신청 템플릿 ID (NULL이면 기본 템플릿 사용)';
COMMENT ON COLUMN MEMBERS.custom_leave_approval_template_id IS '개인 커스텀 휴가승인 템플릿 ID (NULL이면 기본 템플릿 사용)';
COMMENT ON COLUMN MEMBERS.custom_leave_rejection_template_id IS '개인 커스텀 휴가반려 템플릿 ID (NULL이면 기본 템플릿 사용)';
COMMENT ON COLUMN MEMBERS.email_signature IS '개인 이메일 서명 (HTML)';

-- Add foreign key constraints
ALTER TABLE MEMBERS ADD CONSTRAINT fk_member_leave_app_template
    FOREIGN KEY (custom_leave_application_template_id) REFERENCES EMAIL_TEMPLATES(id);

ALTER TABLE MEMBERS ADD CONSTRAINT fk_member_leave_appr_template
    FOREIGN KEY (custom_leave_approval_template_id) REFERENCES EMAIL_TEMPLATES(id);

ALTER TABLE MEMBERS ADD CONSTRAINT fk_member_leave_rej_template
    FOREIGN KEY (custom_leave_rejection_template_id) REFERENCES EMAIL_TEMPLATES(id);

-- Add reason variable to templates (for leave application)
-- Update existing LEAVE_APPLICATION template to include reason
UPDATE EMAIL_TEMPLATES
SET description = '휴가 신청 알림 템플릿 - 변수: ${memberName}, ${leaveType}, ${startDate}, ${endDate}, ${leaveAmount}, ${reason}, ${signature}'
WHERE template_name = 'LEAVE_APPLICATION';

UPDATE EMAIL_TEMPLATES
SET description = '휴가 승인 알림 템플릿 - 변수: ${memberName}, ${leaveType}, ${startDate}, ${endDate}, ${approverName}, ${signature}'
WHERE template_name = 'LEAVE_APPROVAL';

UPDATE EMAIL_TEMPLATES
SET description = '휴가 반려 알림 템플릿 - 변수: ${memberName}, ${leaveType}, ${startDate}, ${endDate}, ${approverName}, ${reason}, ${signature}'
WHERE template_name = 'LEAVE_REJECTION';

-- Example: Create a custom template for a specific member
INSERT INTO EMAIL_TEMPLATES (id, template_name, subject, content, description, is_active) VALUES (
    email_templates_seq.NEXTVAL,
    'CUSTOM_LEAVE_APPLICATION_YOON',
    '휴가 신청 - ${memberName}',
    '<p style="margin: 0px; padding: 0px; color: rgb(32, 33, 36); font-family: ''Malgun Gothic'', ''맑은 고딕'', sans-serif; font-size: 13px;">
${memberName}<br><br>
휴가를 아래와 같이 신청합니다.</p>

<p style="margin: 0px; padding: 0px; color: rgb(32, 33, 36); font-family: ''Malgun Gothic'', ''맑은 고딕'', sans-serif; font-size: 13px;">
<br><span style="font-family: ''Malgun Gothic'', ''맑은 고딕'', sans-serif; font-size: 14px;">- 아 &nbsp; &nbsp; &nbsp;래 -</span><br></p>

<ol style="margin: 1em 0px; padding: 0px 0px 0px 40px; list-style: decimal; color: rgb(32, 33, 36); font-family: ''Malgun Gothic'', ''맑은 고딕'', sans-serif; font-size: 13px;">
<li style="margin: 0px; padding: 0px;">일시 : ${startDate} ~ ${endDate}</li>
<li style="margin: 0px; padding: 0px;"><div>사유 : ${reason}</div></li>
</ol>

<p style="margin: 0px; padding: 0px; color: rgb(32, 33, 36); font-family: ''Malgun Gothic'', ''맑은 고딕'', sans-serif; font-size: 13px;">
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━</p>

<p style="margin: 0px; padding: 0px; color: rgb(32, 33, 36); font-family: ''Malgun Gothic'', ''맑은 고딕'', sans-serif; font-size: 13px;">
###[<br>
&nbsp;<br>
[이름:${memberName}],<br>
&nbsp;<br>
[이메일:moods2@yncsmart.com],<br>
&nbsp;<br>
[휴가종류:${leaveType}|일자:${startDate} ~ ${endDate}|사용일수:${leaveAmount}|사유:${reason}]<br>
&nbsp;<br>
]###</p>

<p style="margin: 0px; padding: 0px; color: rgb(32, 33, 36); font-family: ''Malgun Gothic'', ''맑은 고딕'', sans-serif; font-size: 13px;">
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━</p>

${signature}',
    '윤대웅님 전용 휴가 신청 템플릿 - 변수: ${memberName}, ${leaveType}, ${startDate}, ${endDate}, ${leaveAmount}, ${reason}, ${signature}',
    1
);

-- Example email signature for a member
UPDATE MEMBERS
SET email_signature = '<div data-workseditor="sign-content">
<div style="font-size: 14px; font-family: ''Malgun Gothic'';">
<div style="font-family: Gulim, 굴림, sans-serif;">

<table style="max-width: 700px; width: 100%; border-width: 0px 0px 1px; border-bottom-style: solid; border-bottom-color: rgb(77, 86, 166); padding: 0px; border-collapse: collapse;">
    <tbody>
    <tr><td style="padding-top: 20px;"></td></tr>
    <tr>
        <td style="font-size: 17px; padding-bottom: 5px; color: rgb(77, 86, 166); font-weight: bold; font-family: -apple-system, BlinkMacSystemFont, ''Malgun Gothic'', Dotum, Helvetica, sans-serif;">
            윤 대 웅
            <span style="font-size: 13px; color: rgb(78, 78, 78); padding-right: 20px;">Unit장&nbsp;</span>
            <span style="color: rgb(102, 102, 102); font-size: 13px; white-space: nowrap;">공통 / FCM Unit | 기업사업1본부</span>
        </td>
        <td style="text-align: right; padding-bottom: 5px;">
            <img src="http://www.yncsmart.com/resources/img/mail/ync_logo.svg" width="140" height="20" style="vertical-align: bottom;">
        </td>
    </tr>
    </tbody>
</table>

<table style="max-width: 700px; width: 100%; border-width: 0px 0px 1px; border-bottom-style: solid; border-bottom-color: rgb(221, 221, 221); padding: 0px; border-collapse: collapse;">
    <tbody>
    <tr>
        <td style="font-family: -apple-system, BlinkMacSystemFont, ''Malgun Gothic'', Dotum, Helvetica, sans-serif;">
            <p style="line-height: 1.8; margin: 5px 0px;">
                <a style="vertical-align: top; color: rgb(51, 51, 51); white-space: nowrap; text-decoration: none;">
                    <img src="http://www.yncsmart.com/resources/img/mail/ico_ync_phone.png" width="16" height="16" style="vertical-align: -2px;">&nbsp;
                    010 6549 3554
                &nbsp;&nbsp;</a>
                <a href="mailto:moods2@yncsmart.com" style="vertical-align: top; color: rgb(51, 51, 51); white-space: nowrap; text-decoration: none;">
                    <img src="http://www.yncsmart.com/resources/img/mail/ico_ync_mail.png" width="16" height="16" style="vertical-align: -2px;">&nbsp;
                    moods2@yncsmart.com
                &nbsp;&nbsp;</a>
                <a href="http://www.yncsmart.com/" target="_blank" style="vertical-align: top; color: rgb(51, 51, 51); white-space: nowrap; text-decoration: none;">
                    <img src="http://www.yncsmart.com/resources/img/mail/ico_ync_home.png" width="16" height="16" style="vertical-align: -3px;">&nbsp;
                    www.yncsmart.com
                &nbsp;&nbsp;</a>
            </p>
        </td>
    </tr>
    </tbody>
</table>

</div>

<table style="width: 690px; border: 0px; padding: 0px; border-collapse: collapse; table-layout: fixed;">
    <tbody>
    <tr>
        <td style="padding: 1rem 0px; font-size: 0.75rem; line-height: 1.8; font-family: -apple-system, BlinkMacSystemFont, ''Malgun Gothic'', Dotum, Helvetica, sans-serif; background-color: rgb(255, 255, 255);">
            <p><strong>㈜와이앤씨스마트앱스</strong><br>
            <strong>서울 사무소</strong> | 서울시 강남구 봉은사로20길 41</p>
        </td>
    </tr>
    </tbody>
</table>

</div>
</div>'
WHERE name = '윤대웅';

COMMIT;

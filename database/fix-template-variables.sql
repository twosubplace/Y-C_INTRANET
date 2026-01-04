-- LEAVE_APPLICATION 템플릿의 변수 형식 수정
UPDATE EMAIL_TEMPLATES
SET content = REPLACE(content, '{department}', '${department}')
WHERE template_name = 'LEAVE_APPLICATION';

UPDATE EMAIL_TEMPLATES
SET content = REPLACE(content, '{division}', '${division}')
WHERE template_name = 'LEAVE_APPLICATION';

UPDATE EMAIL_TEMPLATES
SET content = REPLACE(content, '{phone}', '${phone}')
WHERE template_name = 'LEAVE_APPLICATION';

-- LEAVE_APPLICATION2 템플릿도 동일하게 수정 (존재한다면)
UPDATE EMAIL_TEMPLATES
SET content = REPLACE(content, '{department}', '${department}')
WHERE template_name = 'LEAVE_APPLICATION2';

UPDATE EMAIL_TEMPLATES
SET content = REPLACE(content, '{division}', '${division}')
WHERE template_name = 'LEAVE_APPLICATION2';

UPDATE EMAIL_TEMPLATES
SET content = REPLACE(content, '{phone}', '${phone}')
WHERE template_name = 'LEAVE_APPLICATION2';

COMMIT;

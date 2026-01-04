-- Add email column to MEMBERS table
ALTER TABLE MEMBERS ADD email VARCHAR2(255);

COMMENT ON COLUMN MEMBERS.email IS '이메일 주소';

-- Update existing members with email (example data - adjust as needed)
-- UPDATE MEMBERS SET email = name || '@yncsmart.com' WHERE email IS NULL;

-- schedules_intranet 테이블에 approver_id 컬럼 추가
ALTER TABLE schedules_intranet ADD approver_id NUMBER;

-- 외래키 제약조건 추가
ALTER TABLE schedules_intranet
ADD CONSTRAINT fk_schedule_approver
FOREIGN KEY (approver_id) REFERENCES members_intranet(id);

-- 인덱스 추가
CREATE INDEX idx_schedules_approver ON schedules_intranet(approver_id);

COMMENT ON COLUMN schedules_intranet.approver_id IS '결재자 ID (연차/반차 신청시)';

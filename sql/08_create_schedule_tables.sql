-- ================================================================
-- 일정/휴가 관리 테이블 생성 스크립트
-- ================================================================

-- 1. 일정/휴가 테이블
CREATE TABLE schedules_intranet (
    id NUMBER PRIMARY KEY,
    member_id NUMBER NOT NULL,                      -- 작성자 (members_intranet.id 참조)
    schedule_type VARCHAR2(50) NOT NULL,            -- 일정 유형 (VACATION: 연차, HALF_DAY: 반차, BUSINESS_TRIP: 출장, MEETING: 회의)
    title VARCHAR2(200) NOT NULL,                   -- 제목
    description CLOB,                                -- 설명
    start_date DATE NOT NULL,                        -- 시작일
    end_date DATE NOT NULL,                          -- 종료일
    start_time VARCHAR2(5),                          -- 시작 시간 (HH:MI)
    end_time VARCHAR2(5),                            -- 종료 시간 (HH:MI)
    days_used NUMBER(3,1) DEFAULT 0,                 -- 사용 일수 (0.5, 1, 1.5 등)
    document_id NUMBER,                              -- 연결된 결재 문서 ID (documents_intranet.id 참조)
    status VARCHAR2(20) DEFAULT 'DRAFT',             -- 상태 (DRAFT: 초안, SUBMITTED: 제출, APPROVED: 승인, REJECTED: 반려, CANCELLED: 취소)
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_schedule_member FOREIGN KEY (member_id) REFERENCES members_intranet(id),
    CONSTRAINT fk_schedule_document FOREIGN KEY (document_id) REFERENCES documents_intranet(id) ON DELETE SET NULL
);

-- 인덱스 생성
CREATE INDEX idx_schedules_member ON schedules_intranet(member_id);
CREATE INDEX idx_schedules_dates ON schedules_intranet(start_date, end_date);
CREATE INDEX idx_schedules_type ON schedules_intranet(schedule_type);
CREATE INDEX idx_schedules_status ON schedules_intranet(status);

-- 시퀀스 생성
CREATE SEQUENCE schedules_intranet_seq
START WITH 1
INCREMENT BY 1
NOCACHE
NOCYCLE;

-- 코멘트 추가
COMMENT ON TABLE schedules_intranet IS '일정 및 휴가 정보';
COMMENT ON COLUMN schedules_intranet.id IS '일정 ID';
COMMENT ON COLUMN schedules_intranet.member_id IS '작성자 ID';
COMMENT ON COLUMN schedules_intranet.schedule_type IS '일정 유형 (VACATION, HALF_DAY, BUSINESS_TRIP, MEETING)';
COMMENT ON COLUMN schedules_intranet.title IS '일정 제목';
COMMENT ON COLUMN schedules_intranet.description IS '일정 설명';
COMMENT ON COLUMN schedules_intranet.start_date IS '시작일';
COMMENT ON COLUMN schedules_intranet.end_date IS '종료일';
COMMENT ON COLUMN schedules_intranet.start_time IS '시작 시간';
COMMENT ON COLUMN schedules_intranet.end_time IS '종료 시간';
COMMENT ON COLUMN schedules_intranet.days_used IS '사용 일수';
COMMENT ON COLUMN schedules_intranet.document_id IS '연결된 결재 문서 ID';
COMMENT ON COLUMN schedules_intranet.status IS '상태 (DRAFT, SUBMITTED, APPROVED, REJECTED, CANCELLED)';

-- 샘플 데이터 추가
INSERT INTO schedules_intranet (
    id, member_id, schedule_type, title, description,
    start_date, end_date, days_used, status
) VALUES (
    schedules_intranet_seq.NEXTVAL,
    1,  -- admin 계정
    'VACATION',
    '연차 (개인 사유)',
    '개인 사유로 인한 연차 사용',
    TO_DATE('2026-01-15', 'YYYY-MM-DD'),
    TO_DATE('2026-01-15', 'YYYY-MM-DD'),
    1.0,
    'APPROVED'
);

INSERT INTO schedules_intranet (
    id, member_id, schedule_type, title, description,
    start_date, end_date, start_time, end_time, days_used, status
) VALUES (
    schedules_intranet_seq.NEXTVAL,
    1,  -- admin 계정
    'HALF_DAY',
    '반차 (오후)',
    '병원 진료',
    TO_DATE('2026-01-20', 'YYYY-MM-DD'),
    TO_DATE('2026-01-20', 'YYYY-MM-DD'),
    '13:00',
    '18:00',
    0.5,
    'APPROVED'
);

INSERT INTO schedules_intranet (
    id, member_id, schedule_type, title, description,
    start_date, end_date, status
) VALUES (
    schedules_intranet_seq.NEXTVAL,
    2,  -- test 계정
    'BUSINESS_TRIP',
    '출장 (서울 본사)',
    '본사 업무 협의',
    TO_DATE('2026-01-22', 'YYYY-MM-DD'),
    TO_DATE('2026-01-23', 'YYYY-MM-DD'),
    'APPROVED'
);

INSERT INTO schedules_intranet (
    id, member_id, schedule_type, title, description,
    start_date, end_date, start_time, end_time, status
) VALUES (
    schedules_intranet_seq.NEXTVAL,
    1,  -- admin 계정
    'MEETING',
    '팀 회의',
    '월간 정기 회의',
    TO_DATE('2026-01-10', 'YYYY-MM-DD'),
    TO_DATE('2026-01-10', 'YYYY-MM-DD'),
    '14:00',
    '16:00',
    'APPROVED'
);

COMMIT;

SELECT '일정/휴가 테이블이 성공적으로 생성되었습니다.' AS result FROM dual;

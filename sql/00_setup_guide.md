# YNC INTRANET 데이터베이스 설정 가이드

## 📋 개요
이 문서는 YNC INTRANET 시스템의 데이터베이스 스키마를 구축하는 가이드입니다.

---

## 🗂️ SQL 파일 구조

```
sql/
├── 00_setup_guide.md              # 이 파일 (설정 가이드)
├── 01_create_tables.sql           # 테이블 생성 (12개)
├── 02_create_sequences.sql        # 시퀀스 생성 (11개)
├── 03_create_indexes.sql          # 인덱스 생성 (성능 최적화)
└── 04_insert_common_data.sql      # 기본 데이터 삽입
```

---

## 🚀 실행 순서

### 1단계: 테이블 생성
```sql
@01_create_tables.sql
```

**생성되는 테이블 (12개):**
1. departments_intranet (부서)
2. members_intranet (사원)
3. documents_intranet (문서 통합)
4. leave_requests_intranet (휴가 신청)
5. expense_reports_intranet (경비보고서)
6. expense_items_intranet (경비 항목)
7. approval_lines_intranet (결재선)
8. attachments_intranet (첨부파일)
9. notices_intranet (공지사항)
10. system_logs_intranet (시스템 로그)
11. common_codes_intranet (공통 코드)
12. email_templates_intranet (메일 템플릿)

---

### 2단계: 시퀀스 생성
```sql
@02_create_sequences.sql
```

**생성되는 시퀀스 (11개):**
- departments_intranet_seq
- members_intranet_seq
- documents_intranet_seq
- leave_requests_intranet_seq
- expense_reports_intranet_seq
- expense_items_intranet_seq
- approval_lines_intranet_seq
- attachments_intranet_seq
- notices_intranet_seq
- system_logs_intranet_seq
- email_templates_intranet_seq

---

### 3단계: 인덱스 생성
```sql
@03_create_indexes.sql
```

**주요 인덱스:**
- 검색 성능: author_id, status, document_type
- 결재 조회: approver_id, decision
- 날짜 범위: created_at, start_date, end_date

---

### 4단계: 기본 데이터 삽입
```sql
@04_insert_common_data.sql
```

**삽입되는 데이터:**
- 공통 코드 (휴가 유형, 경비 카테고리, 직급)
- 기본 부서 3개 (경영지원팀, 개발팀, 영업팀)
- 관리자 계정 1개
- 메일 템플릿 5개

---

## 🔐 기본 관리자 계정

```
이메일: admin@yncsmart.com
비밀번호: admin1234
권한: ADMIN
```

> ⚠️ **보안 주의:** 운영 환경에서는 반드시 비밀번호를 변경하세요!

---

## 📊 테이블 관계도

```
departments_intranet (부서)
    ↓
members_intranet (사원)
    ↓
documents_intranet (문서)
    ├─ leave_requests_intranet (휴가)
    ├─ expense_reports_intranet (경비)
    │   └─ expense_items_intranet (경비 항목)
    ├─ approval_lines_intranet (결재선)
    └─ attachments_intranet (첨부파일)

notices_intranet (공지사항)
system_logs_intranet (시스템 로그)
common_codes_intranet (공통 코드)
email_templates_intranet (메일 템플릿)
```

---

## ✅ 설치 확인

### 테이블 확인
```sql
SELECT table_name
FROM user_tables
WHERE table_name LIKE '%_INTRANET'
ORDER BY table_name;
```

### 시퀀스 확인
```sql
SELECT sequence_name
FROM user_sequences
WHERE sequence_name LIKE '%_INTRANET_%'
ORDER BY sequence_name;
```

### 데이터 확인
```sql
-- 공통 코드
SELECT code_type, COUNT(*) as cnt
FROM common_codes_intranet
GROUP BY code_type;

-- 부서
SELECT * FROM departments_intranet;

-- 관리자 계정
SELECT email, name, role FROM members_intranet;

-- 메일 템플릿
SELECT template_type FROM email_templates_intranet;
```

---

## 🔄 재설치 방법

### 전체 삭제 (주의!)
```sql
-- 테이블 삭제 (외래키 때문에 역순으로)
DROP TABLE system_logs_intranet CASCADE CONSTRAINTS;
DROP TABLE notices_intranet CASCADE CONSTRAINTS;
DROP TABLE attachments_intranet CASCADE CONSTRAINTS;
DROP TABLE approval_lines_intranet CASCADE CONSTRAINTS;
DROP TABLE expense_items_intranet CASCADE CONSTRAINTS;
DROP TABLE expense_reports_intranet CASCADE CONSTRAINTS;
DROP TABLE leave_requests_intranet CASCADE CONSTRAINTS;
DROP TABLE documents_intranet CASCADE CONSTRAINTS;
DROP TABLE members_intranet CASCADE CONSTRAINTS;
DROP TABLE departments_intranet CASCADE CONSTRAINTS;
DROP TABLE email_templates_intranet CASCADE CONSTRAINTS;
DROP TABLE common_codes_intranet CASCADE CONSTRAINTS;

-- 시퀀스 삭제
DROP SEQUENCE departments_intranet_seq;
DROP SEQUENCE members_intranet_seq;
DROP SEQUENCE documents_intranet_seq;
DROP SEQUENCE leave_requests_intranet_seq;
DROP SEQUENCE expense_reports_intranet_seq;
DROP SEQUENCE expense_items_intranet_seq;
DROP SEQUENCE approval_lines_intranet_seq;
DROP SEQUENCE attachments_intranet_seq;
DROP SEQUENCE notices_intranet_seq;
DROP SEQUENCE system_logs_intranet_seq;
DROP SEQUENCE email_templates_intranet_seq;
```

### 재설치
위 삭제 후 1~4단계 순서대로 다시 실행

---

## 📝 주요 설계 특징

### 1. 문서 통합 구조
- **documents_intranet** 테이블에 모든 문서 통합
- document_type으로 구분 (LEAVE, EXPENSE, GENERAL)
- 상세 정보는 별도 테이블 (1:1 관계)

### 2. 결재선 스냅샷
- approver_name, approver_position 저장
- 결재 당시 정보 보존 (나중에 직급 변경되어도 OK)

### 3. 소프트 삭제 준비
- is_active 컬럼으로 논리 삭제 가능
- 물리 삭제는 CASCADE로 관련 데이터 함께 삭제

### 4. 확장 가능 구조
- metadata 컬럼 (JSON 형식)
- 향후 필드 추가 없이 유연하게 확장 가능

---

## 🛠️ 트러블슈팅

### Q: 시퀀스가 이미 존재한다는 오류
```sql
-- 기존 시퀀스 삭제 후 재생성
DROP SEQUENCE [시퀀스명];
```

### Q: 외래키 제약조건 오류
```sql
-- 테이블 생성 순서 확인
-- departments → members → documents → 나머지
```

### Q: 권한 오류
```sql
-- 사용자에게 필요한 권한 부여
GRANT CREATE TABLE TO [사용자명];
GRANT CREATE SEQUENCE TO [사용자명];
```

---

## 📞 문의

문제 발생 시 개발팀에 문의하세요.

---

## 📅 버전 히스토리

- **v1.0** (2025-12-31)
  - 초기 스키마 설계
  - 12개 테이블 생성
  - 기본 데이터 삽입 스크립트 작성

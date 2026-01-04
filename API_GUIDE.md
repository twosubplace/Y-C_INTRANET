# YNC INTRANET API 가이드

## 📋 개요
YNC INTRANET 시스템의 REST API 문서입니다.
Session 기반 인증을 사용합니다.

---

## 🔐 인증 (Auth)

### 1. 로그인
```
POST /api/intranet/auth/login
```

**Request:**
```json
{
  "email": "admin@yncsmart.com",
  "password": "admin1234"
}
```

**Response (성공):**
```json
{
  "success": true,
  "message": "로그인 성공",
  "user": {
    "id": 1,
    "email": "admin@yncsmart.com",
    "name": "시스템관리자",
    "role": "ADMIN",
    "position": "DIRECTOR",
    "departmentName": "경영지원팀"
  }
}
```

**Response (실패):**
```json
{
  "success": false,
  "message": "비밀번호가 일치하지 않습니다."
}
```

---

### 2. 로그아웃
```
POST /api/intranet/auth/logout
```

**Response:**
```json
{
  "success": true,
  "message": "로그아웃되었습니다."
}
```

---

### 3. 현재 사용자 정보 조회
```
GET /api/intranet/auth/me
```

**Response:**
```json
{
  "success": true,
  "user": {
    "id": 1,
    "email": "admin@yncsmart.com",
    "name": "시스템관리자",
    "role": "ADMIN"
  }
}
```

---

### 4. 비밀번호 변경
```
POST /api/intranet/auth/change-password
```

**Request:**
```json
{
  "oldPassword": "admin1234",
  "newPassword": "newpassword123"
}
```

**Response:**
```json
{
  "success": true,
  "message": "비밀번호가 변경되었습니다."
}
```

---

### 5. 네이버웍스 이메일 로그인
```
POST /api/intranet/auth/naver-works-email
```

**Request:**
```json
{
  "email": "user@yncsmart.com",
  "password": "password123"
}
```

**Response (성공):**
```json
{
  "success": true,
  "message": "네이버웍스 로그인 성공",
  "user": {
    "id": 2,
    "email": "user@yncsmart.com",
    "name": "홍길동",
    "role": "USER",
    "position": "STAFF",
    "departmentName": "개발팀"
  }
}
```

**Response (실패):**
```json
{
  "success": false,
  "message": "회사 이메일(@yncsmart.com)만 사용 가능합니다."
}
```

---

## 👥 사원 관리 (Members)

### 1. 전체 사원 조회
```
GET /api/intranet/members
```

**Response:**
```json
[
  {
    "id": 1,
    "email": "admin@yncsmart.com",
    "name": "시스템관리자",
    "phone": "010-0000-0000",
    "departmentId": 1,
    "departmentName": "경영지원팀",
    "position": "DIRECTOR",
    "role": "ADMIN",
    "hireDate": "2020-01-01",
    "annualLeaveGranted": 15,
    "isActive": true,
    "createdAt": "2025-12-31T10:00:00",
    "updatedAt": "2025-12-31T10:00:00"
  }
]
```

---

### 2. 활성화된 사원만 조회
```
GET /api/intranet/members/active
```

---

### 3. ID로 사원 조회
```
GET /api/intranet/members/{id}
```

---

### 4. 부서별 사원 조회
```
GET /api/intranet/members/department/{departmentId}
```

---

### 5. 권한별 사원 조회
```
GET /api/intranet/members/role/{role}
```

**예시:**
```
GET /api/intranet/members/role/APPROVER
```

---

### 6. 사원 등록 (ADMIN 전용)
```
POST /api/intranet/members
```

**Request:**
```json
{
  "email": "user@yncsmart.com",
  "password": "password123",
  "name": "홍길동",
  "phone": "010-1234-5678",
  "departmentId": 2,
  "position": "STAFF",
  "role": "USER",
  "hireDate": "2025-01-01",
  "annualLeaveGranted": 15,
  "isActive": true
}
```

**Response:**
```json
{
  "success": true,
  "member": {
    "id": 2,
    "email": "user@yncsmart.com",
    "name": "홍길동",
    ...
  }
}
```

---

### 7. 사원 수정 (ADMIN 전용)
```
PUT /api/intranet/members/{id}
```

---

### 8. 사원 비활성화 (ADMIN 전용)
```
POST /api/intranet/members/{id}/deactivate
```

---

## ✅ 결재 (Approvals)

### 1. 내 대기중인 결재 목록
```
GET /api/intranet/approvals/pending
```

**Response:**
```json
{
  "success": true,
  "approvals": [
    {
      "id": 1,
      "documentId": 10,
      "stepOrder": 1,
      "approverId": 1,
      "approverName": "시스템관리자",
      "approverPosition": "DIRECTOR",
      "decision": "PENDING",
      "approvalComment": null,
      "submittedAt": "2025-12-31T14:00:00",
      "decidedAt": null
    }
  ]
}
```

---

### 2. 내 모든 결재 목록
```
GET /api/intranet/approvals/my
```

---

### 3. 문서의 결재선 조회
```
GET /api/intranet/approvals/document/{documentId}
```

---

### 4. 결재 승인
```
POST /api/intranet/approvals/{id}/approve
```

**Request:**
```json
{
  "comment": "승인합니다."
}
```

**Response:**
```json
{
  "success": true,
  "message": "승인되었습니다."
}
```

---

### 5. 결재 반려
```
POST /api/intranet/approvals/{id}/reject
```

**Request:**
```json
{
  "comment": "재작성이 필요합니다."
}
```

**Response:**
```json
{
  "success": true,
  "message": "반려되었습니다."
}
```

---

### 6. 결재 취소 (작성자)
```
POST /api/intranet/approvals/document/{documentId}/cancel
```

**Response:**
```json
{
  "success": true,
  "message": "결재가 취소되었습니다."
}
```

---

## 📅 일정/휴가 관리 (Schedules)

### 1. 일정 등록 및 결재 요청
```
POST /api/intranet/schedules
```

**Request:**
```json
{
  "scheduleType": "VACATION",
  "title": "연차",
  "description": "개인 사유",
  "startDate": "2026-01-15",
  "endDate": "2026-01-15",
  "startTime": null,
  "endTime": null,
  "daysUsed": 1.0
}
```

**Schedule Types:**
- `VACATION`: 연차 (1일 이상)
- `HALF_DAY`: 반차 (0.5일, 오전/오후)
- `BUSINESS_TRIP`: 출장
- `MEETING`: 회의 (시간 지정 필요)

**Response:**
```json
{
  "success": true,
  "message": "일정이 등록되고 결재 요청되었습니다.",
  "schedule": {
    "id": 1,
    "memberId": 1,
    "scheduleType": "VACATION",
    "title": "연차",
    "description": "개인 사유",
    "startDate": "2026-01-15",
    "endDate": "2026-01-15",
    "daysUsed": 1.0,
    "documentId": 10,
    "status": "SUBMITTED",
    "createdAt": "2026-01-01T10:00:00"
  }
}
```

---

### 2. 일정 목록 조회
```
GET /api/intranet/schedules
```

**Query Parameters:**
- `startDate` (optional): 조회 시작일 (YYYY-MM-DD)
- `endDate` (optional): 조회 종료일 (YYYY-MM-DD)
- `status` (optional): 상태 필터 (DRAFT, SUBMITTED, APPROVED, REJECTED, CANCELLED)
- `scheduleType` (optional): 유형 필터 (VACATION, HALF_DAY, BUSINESS_TRIP, MEETING)

**예시:**
```
GET /api/intranet/schedules?startDate=2026-01-01&endDate=2026-01-31&status=APPROVED
```

**Response:**
```json
{
  "success": true,
  "schedules": [
    {
      "id": 1,
      "memberId": 1,
      "memberName": "시스템관리자",
      "scheduleType": "VACATION",
      "title": "연차",
      "startDate": "2026-01-15",
      "endDate": "2026-01-15",
      "startTime": null,
      "endTime": null,
      "daysUsed": 1.0,
      "status": "APPROVED",
      "createdAt": "2026-01-01T10:00:00"
    }
  ]
}
```

---

### 3. 캘린더용 일정 조회
```
GET /api/intranet/schedules/calendar
```

**Query Parameters:**
- `start` (required): 캘린더 시작일 (YYYY-MM-DD)
- `end` (required): 캘린더 종료일 (YYYY-MM-DD)
- `departmentId` (optional): 부서 필터
- `memberId` (optional): 구성원 필터

**Response (FullCalendar 형식):**
```json
[
  {
    "id": "1",
    "title": "연차",
    "start": "2026-01-15",
    "end": "2026-01-16",
    "className": "event-vacation",
    "backgroundColor": "#ec4899",
    "extendedProps": {
      "scheduleType": "VACATION",
      "memberName": "시스템관리자",
      "description": "개인 사유",
      "status": "APPROVED",
      "daysUsed": 1.0
    }
  },
  {
    "id": "2",
    "title": "팀 회의",
    "start": "2026-01-10T14:00:00",
    "end": "2026-01-10T16:00:00",
    "className": "event-meeting",
    "backgroundColor": "#10b981",
    "extendedProps": {
      "scheduleType": "MEETING",
      "memberName": "홍길동",
      "startTime": "14:00",
      "endTime": "16:00",
      "status": "APPROVED"
    }
  }
]
```

---

### 4. 일정 상세 조회
```
GET /api/intranet/schedules/{id}
```

**Response:**
```json
{
  "success": true,
  "schedule": {
    "id": 1,
    "memberId": 1,
    "memberName": "시스템관리자",
    "scheduleType": "VACATION",
    "title": "연차",
    "description": "개인 사유",
    "startDate": "2026-01-15",
    "endDate": "2026-01-15",
    "startTime": null,
    "endTime": null,
    "daysUsed": 1.0,
    "documentId": 10,
    "status": "APPROVED",
    "createdAt": "2026-01-01T10:00:00",
    "updatedAt": "2026-01-01T15:00:00"
  }
}
```

---

### 5. 일정 수정
```
PUT /api/intranet/schedules/{id}
```

**Request:**
```json
{
  "title": "연차 (수정)",
  "description": "가족 행사",
  "startDate": "2026-01-16",
  "endDate": "2026-01-16"
}
```

**Response:**
```json
{
  "success": true,
  "message": "일정이 수정되었습니다.",
  "schedule": { ... }
}
```

**제약사항:**
- DRAFT 상태에서만 수정 가능
- 결재 진행 중이거나 승인된 일정은 수정 불가

---

### 6. 일정 삭제
```
DELETE /api/intranet/schedules/{id}
```

**Response:**
```json
{
  "success": true,
  "message": "일정이 삭제되었습니다."
}
```

**제약사항:**
- DRAFT 상태에서만 삭제 가능

---

### 7. 내 일정 목록
```
GET /api/intranet/schedules/my
```

**Query Parameters:**
- `year` (optional): 연도 필터 (default: 현재 연도)
- `month` (optional): 월 필터 (1-12)

**Response:**
```json
{
  "success": true,
  "schedules": [ ... ]
}
```

---

### 8. 내 휴가 현황
```
GET /api/intranet/schedules/leave-balance
```

**Query Parameters:**
- `year` (optional): 연도 (default: 현재 연도)

**Response:**
```json
{
  "success": true,
  "leaveBalance": {
    "year": 2026,
    "granted": 15.0,
    "used": 2.5,
    "remaining": 12.5,
    "details": [
      {
        "scheduleType": "VACATION",
        "title": "연차",
        "startDate": "2026-01-15",
        "daysUsed": 1.0
      },
      {
        "scheduleType": "HALF_DAY",
        "title": "반차 (오후)",
        "startDate": "2026-01-20",
        "daysUsed": 0.5
      }
    ]
  }
}
```

---

### 반차 예시

**오전 반차:**
```json
{
  "scheduleType": "HALF_DAY",
  "title": "반차 (오전)",
  "startDate": "2026-01-20",
  "endDate": "2026-01-20",
  "startTime": "09:00",
  "endTime": "13:00",
  "daysUsed": 0.5
}
```

**오후 반차:**
```json
{
  "scheduleType": "HALF_DAY",
  "title": "반차 (오후)",
  "startDate": "2026-01-20",
  "endDate": "2026-01-20",
  "startTime": "13:00",
  "endTime": "18:00",
  "daysUsed": 0.5
}
```

---

### 회의 예시

```json
{
  "scheduleType": "MEETING",
  "title": "팀 회의",
  "description": "월간 정기 회의",
  "startDate": "2026-01-10",
  "endDate": "2026-01-10",
  "startTime": "14:00",
  "endTime": "16:00",
  "daysUsed": 0
}
```

---

## 📝 에러 응답

### 401 Unauthorized
```json
{
  "success": false,
  "message": "로그인이 필요합니다."
}
```

### 403 Forbidden
```json
{
  "success": false,
  "message": "권한이 없습니다."
}
```

### 400 Bad Request
```json
{
  "success": false,
  "message": "이메일과 비밀번호를 입력해주세요."
}
```

### 404 Not Found
```json
{
  "success": false,
  "message": "존재하지 않는 사용자입니다."
}
```

---

## 🔒 권한 체계

### 역할 (Role)
- **USER**: 일반 사용자 (문서 작성, 조회)
- **APPROVER**: 결재권자 (USER 권한 + 결재 승인/반려)
- **ADMIN**: 관리자 (모든 권한 + 사원 관리)

### 권한별 허용 작업

| API | USER | APPROVER | ADMIN |
|-----|------|----------|-------|
| 로그인/로그아웃 | ✅ | ✅ | ✅ |
| 내 정보 조회 | ✅ | ✅ | ✅ |
| 비밀번호 변경 | ✅ | ✅ | ✅ |
| 사원 조회 | ✅ | ✅ | ✅ |
| 사원 등록/수정 | ❌ | ❌ | ✅ |
| 결재 승인/반려 | ❌ | ✅ | ✅ |
| 문서 작성 | ✅ | ✅ | ✅ |

---

## 🧪 테스트 시나리오

### 1. 기본 로그인 테스트
```bash
curl -X POST http://localhost:8083/api/intranet/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"admin@yncsmart.com","password":"admin1234"}' \
  -c cookies.txt
```

### 2. 네이버웍스 이메일 로그인 테스트
```bash
curl -X POST http://localhost:8083/api/intranet/auth/naver-works-email \
  -H "Content-Type: application/json" \
  -d '{"email":"admin@yncsmart.com","password":"admin1234"}' \
  -c cookies.txt
```

### 3. 내 정보 조회
```bash
curl -X GET http://localhost:8083/api/intranet/auth/me \
  -b cookies.txt
```

### 4. 대기중인 결재 조회
```bash
curl -X GET http://localhost:8083/api/intranet/approvals/pending \
  -b cookies.txt
```

### 5. 로그아웃
```bash
curl -X POST http://localhost:8083/api/intranet/auth/logout \
  -b cookies.txt
```

---

## 📅 개발 우선순위

### Phase 1 (완료)
- [x] 인증 API (로그인, 로그아웃, 비밀번호 변경)
- [x] 사원 관리 API
- [x] 결재 API (승인, 반려, 취소)
- [x] 일정/휴가 관리 API 🆕

### Phase 2 (추가 개발 필요)
- [ ] 문서 API (작성, 수정, 삭제, 상신)
- [ ] 경비보고서 API
- [ ] Dashboard API
- [ ] 공지사항 API

---

## 🛠️ 다음 단계

현재까지 **인증 + 사원 관리 + 결재 + 일정/휴가 관리** API가 완성되었습니다.

추가로 구현할 API:
1. ScheduleController (일정/휴가 관리 백엔드) 🔄
2. DocumentController (문서 작성/상신)
3. ExpenseReportController (경비보고서)
4. DashboardController (대시보드)
5. NoticeController (공지사항)

**참고**: 일정/휴가 관리 프론트엔드(schedule-calendar.html)는 완성되었으며, 백엔드 API 연동이 필요합니다.

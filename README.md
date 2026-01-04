# Team Leave & Schedule Management System

팀 휴가 및 일정을 관리하는 웹 애플리케이션입니다.

## 기술 스택

### Backend
- **Framework**: Spring Boot 3.2.0
- **Language**: Java 17
- **ORM**: Spring Data JPA
- **Database**: Oracle Database (개발환경: Oracle XE)
- **Build Tool**: Maven

### Frontend
- **Framework**: Vanilla JavaScript SPA
- **Calendar Library**: FullCalendar 6.1.10
- **Styling**: Custom CSS

## 주요 기능

1. **팀 멤버 관리**
   - 멤버 추가/수정/삭제
   - 부서, 직급, 연차 일수 관리
   - 활성/비활성 상태 관리

2. **일정 및 휴가 관리**
   - 통합 이벤트 관리 (휴가 + 일정)
   - 이벤트 유형:
     - 휴가: 연차, 반차-오전, 반차-오후, 병가
     - 일정: 회의, 외근, 개인일정
   - 캘린더 뷰 (월/주/목록)
   - 멤버별 필터링

3. **승인 워크플로우**
   - 상태 관리: DRAFT → SUBMITTED → APPROVED/REJECTED
   - 승인자 지정 및 승인/반려 처리
   - 승인 이력 추적

4. **휴가 현황**
   - 연도별 휴가 사용 현황 조회
   - 부여/사용/잔여 휴가 계산
   - 멤버별 상세 조회

## 데이터베이스 스키마

### ERD 구조

```
members (멤버)
├── id (PK)
├── name
├── department
├── position
├── annual_leave_granted
└── is_active

events (일정/휴가)
├── id (PK)
├── member_id (FK → members)
├── event_type (LEAVE/SCHEDULE)
├── event_subtype
├── start_date
├── end_date
├── leave_amount
├── title
├── description
└── status (DRAFT/SUBMITTED/APPROVED/REJECTED/CANCELED)

approvals (승인)
├── id (PK)
├── event_id (FK → events)
├── step_order
├── approver_id (FK → members)
├── decision (APPROVED/REJECTED)
├── comment
├── submitted_at
└── decided_at
```

## 설치 및 실행 방법

### 1. 사전 요구사항

- Java 17 이상
- Maven 3.6 이상
- Oracle Database (Oracle XE 권장)

### 2. 데이터베이스 설정

Oracle Database에 접속하여 스키마를 생성합니다:

```bash
# SQL*Plus 또는 SQL Developer로 접속
sqlplus system/ync1234!@localhost:1522/XEPDB1

# 스키마 생성 스크립트 실행
@database/schema-oracle.sql
```

### 3. 애플리케이션 설정

`src/main/resources/application.yml` 파일에서 데이터베이스 연결 정보를 확인/수정합니다:

```yaml
spring:
  datasource:
    url: jdbc:oracle:thin:@localhost:1522/XEPDB1
    username: system
    password: ync1234!
    driver-class-name: oracle.jdbc.OracleDriver
```

### 4. 빌드 및 실행

```bash
# 프로젝트 디렉토리로 이동
cd c:\smartWork\workspace\yncSchedule

# Maven 빌드
mvn clean package

# 애플리케이션 실행
mvn spring-boot:run
```

또는 JAR 파일로 실행:

```bash
java -jar target/schedule-1.0.0.jar
```

### 5. 애플리케이션 접속

브라우저에서 다음 URL로 접속:

```
http://localhost:8080/
```

## API 엔드포인트

### Members API

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/members` | 모든 멤버 조회 |
| GET | `/api/members?active=true` | 활성 멤버만 조회 |
| GET | `/api/members/{id}` | 특정 멤버 조회 |
| POST | `/api/members` | 멤버 추가 |
| PUT | `/api/members/{id}` | 멤버 수정 |
| DELETE | `/api/members/{id}` | 멤버 삭제 |
| GET | `/api/members/{id}/leave-balance?year={year}` | 멤버 휴가 현황 조회 |
| GET | `/api/members/leave-balances?year={year}` | 전체 휴가 현황 조회 |

### Events API

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/events` | 모든 이벤트 조회 |
| GET | `/api/events?startDate={date}&endDate={date}` | 기간별 이벤트 조회 |
| GET | `/api/events/{id}` | 특정 이벤트 조회 |
| GET | `/api/events/member/{memberId}` | 멤버별 이벤트 조회 |
| POST | `/api/events` | 이벤트 추가 |
| PUT | `/api/events/{id}` | 이벤트 수정 |
| DELETE | `/api/events/{id}` | 이벤트 삭제 |
| POST | `/api/events/{id}/submit` | 이벤트 제출 (승인 요청) |
| POST | `/api/events/{id}/approve` | 이벤트 승인 |
| POST | `/api/events/{id}/reject` | 이벤트 반려 |

### Approvals API

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/approvals/event/{eventId}` | 이벤트의 승인 이력 조회 |
| GET | `/api/approvals/approver/{approverId}/pending` | 승인자의 대기 중인 승인 조회 |

## 프론트엔드 LocalStorage 모드

백엔드 서버 없이 테스트하려면 `js/api.js` 파일의 설정을 변경합니다:

```javascript
const USE_BACKEND = false; // true → false로 변경
```

이 모드에서는 모든 데이터가 브라우저의 LocalStorage에 저장됩니다.

## 휴가 잔여일 계산 쿼리

특정 연도의 멤버별 휴가 잔여일을 계산하는 SQL 쿼리:

```sql
SELECT
    m.id,
    m.name,
    m.department,
    m.position,
    m.annual_leave_granted,
    NVL(SUM(CASE
        WHEN e.event_type = 'LEAVE'
        AND e.status = 'APPROVED'
        AND EXTRACT(YEAR FROM e.start_date) = 2025
        THEN e.leave_amount
        ELSE 0
    END), 0) AS used_leave,
    m.annual_leave_granted - NVL(SUM(CASE
        WHEN e.event_type = 'LEAVE'
        AND e.status = 'APPROVED'
        AND EXTRACT(YEAR FROM e.start_date) = 2025
        THEN e.leave_amount
        ELSE 0
    END), 0) AS remaining_leave
FROM
    members m
LEFT JOIN
    events e ON m.id = e.member_id
WHERE
    m.is_active = 1
GROUP BY
    m.id, m.name, m.department, m.position, m.annual_leave_granted
ORDER BY
    m.department, m.name;
```

## 프로젝트 구조

```
yncSchedule/
├── database/
│   ├── schema.sql              # PostgreSQL용 DDL (참고용)
│   └── schema-oracle.sql       # Oracle용 DDL (실제 사용)
├── src/
│   └── main/
│       ├── java/com/ync/schedule/
│       │   ├── ScheduleApplication.java
│       │   ├── controller/
│       │   │   ├── ApprovalController.java
│       │   │   ├── EventController.java
│       │   │   └── MemberController.java
│       │   ├── dto/
│       │   │   ├── ApprovalDto.java
│       │   │   ├── EventDto.java
│       │   │   ├── LeaveBalanceDto.java
│       │   │   └── MemberDto.java
│       │   ├── entity/
│       │   │   ├── Approval.java
│       │   │   ├── Event.java
│       │   │   └── Member.java
│       │   ├── repository/
│       │   │   ├── ApprovalRepository.java
│       │   │   ├── EventRepository.java
│       │   │   └── MemberRepository.java
│       │   └── service/
│       │       ├── ApprovalService.java
│       │       ├── EventService.java
│       │       └── MemberService.java
│       └── resources/
│           ├── application.yml
│           └── static/
│               ├── index.html
│               ├── css/
│               │   └── style.css
│               └── js/
│                   ├── api.js
│                   └── app.js
├── pom.xml
└── README.md
```

## 사용 방법

### 1. 멤버 관리
1. "멤버 관리" 버튼 클릭
2. "새 멤버 추가" 버튼으로 멤버 추가
3. 각 멤버의 "수정" 또는 "삭제" 버튼으로 관리

### 2. 일정/휴가 등록
1. 캘린더에서 날짜 클릭 또는 "새 일정 추가" 버튼 클릭
2. 양식 작성 (담당자, 유형, 날짜 등)
3. "저장" 버튼 클릭

### 3. 승인 프로세스
1. 일정 등록 후 "제출" 버튼 클릭
2. 승인자 ID 입력
3. 승인자가 해당 일정 클릭 후 승인/반려 처리

### 4. 휴가 현황 확인
1. "휴가 현황" 버튼 클릭
2. 연도 선택 후 "조회" 버튼
3. 멤버별 부여/사용/잔여 휴가 확인

## 문제 해결

### 데이터베이스 연결 오류
- Oracle Database가 실행 중인지 확인
- 연결 정보 (호스트, 포트, SID/서비스명) 확인
- 사용자 권한 확인

### 빌드 오류
```bash
# Maven 캐시 정리 후 재빌드
mvn clean install -U
```

### 포트 충돌
`application.yml`에서 포트 변경:
```yaml
server:
  port: 8081  # 원하는 포트로 변경
```

## 라이선스

이 프로젝트는 교육 및 개발 목적으로 작성되었습니다.

## 작성자

YNC Development Team

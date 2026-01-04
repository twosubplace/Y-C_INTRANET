# 네이버웍스 로그인 연동 가이드

## 📋 개요
YNC INTRANET에 네이버웍스(Naver Works) OAuth 2.0 로그인을 연동하는 방법입니다.

---

## 🔧 연동 방식 선택

### Option 1: OAuth 2.0 연동 (권장)
- 네이버웍스 계정으로 직접 로그인
- SSO(Single Sign-On) 가능
- 별도 비밀번호 관리 불필요

### Option 2: 이메일 기반 매칭
- 네이버웍스 이메일과 DB 이메일 매칭
- 최초 1회만 인증
- 간단한 구현

---

## 🚀 Option 1: OAuth 2.0 연동 (Full SSO)

### 1단계: 네이버웍스 개발자 센터 설정

#### 1.1 애플리케이션 등록
```
1. https://developers.worksmobile.com 접속
2. 콘솔 로그인
3. 앱 생성
   - 앱 이름: YNC INTRANET
   - 설명: 사내 인트라넷 시스템
   - Redirect URI: http://localhost:8083/api/intranet/auth/naver-works/callback
```

#### 1.2 OAuth Scope 설정
```
필요한 권한:
- user (사용자 기본 정보)
- user.email (이메일)
- user.profile (프로필 정보)
```

#### 1.3 Client ID/Secret 발급
```
발급받은 정보를 application.yml에 저장
```

---

### 2단계: application.yml 설정

```yaml
# 기존 설정에 추가
naver-works:
  oauth:
    client-id: YOUR_CLIENT_ID
    client-secret: YOUR_CLIENT_SECRET
    redirect-uri: http://localhost:8083/api/intranet/auth/naver-works/callback
    authorization-uri: https://auth.worksmobile.com/oauth2/v2.0/authorize
    token-uri: https://auth.worksmobile.com/oauth2/v2.0/token
    user-info-uri: https://www.worksapis.com/v1.0/users/me
```

---

### 3단계: pom.xml에 의존성 추가

```xml
<!-- OAuth 2.0 Client -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-oauth2-client</artifactId>
</dependency>

<!-- WebClient (API 호출용) -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-webflux</artifactId>
</dependency>
```

---

### 4단계: NaverWorksOAuthService 생성

```java
package com.ync.intranet.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.Map;

@Service
public class NaverWorksOAuthService {

    @Value("${naver-works.oauth.client-id}")
    private String clientId;

    @Value("${naver-works.oauth.client-secret}")
    private String clientSecret;

    @Value("${naver-works.oauth.redirect-uri}")
    private String redirectUri;

    @Value("${naver-works.oauth.token-uri}")
    private String tokenUri;

    @Value("${naver-works.oauth.user-info-uri}")
    private String userInfoUri;

    private final WebClient webClient;

    public NaverWorksOAuthService() {
        this.webClient = WebClient.builder().build();
    }

    /**
     * Authorization Code로 Access Token 발급
     */
    public Map<String, Object> getAccessToken(String code) {
        Map<String, String> body = new HashMap<>();
        body.put("grant_type", "authorization_code");
        body.put("client_id", clientId);
        body.put("client_secret", clientSecret);
        body.put("code", code);
        body.put("redirect_uri", redirectUri);

        return webClient.post()
                .uri(tokenUri)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(Map.class)
                .block();
    }

    /**
     * Access Token으로 사용자 정보 조회
     */
    public Map<String, Object> getUserInfo(String accessToken) {
        return webClient.get()
                .uri(userInfoUri)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .retrieve()
                .bodyToMono(Map.class)
                .block();
    }
}
```

---

### 5단계: AuthController에 OAuth 엔드포인트 추가

```java
package com.ync.intranet.controller;

import com.ync.intranet.domain.MemberIntranet;
import com.ync.intranet.service.AuthService;
import com.ync.intranet.service.NaverWorksOAuthService;
import com.ync.intranet.service.MemberIntranetService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/intranet/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthService authService;
    private final NaverWorksOAuthService naverWorksOAuthService;
    private final MemberIntranetService memberService;

    @Value("${naver-works.oauth.client-id}")
    private String clientId;

    @Value("${naver-works.oauth.redirect-uri}")
    private String redirectUri;

    @Value("${naver-works.oauth.authorization-uri}")
    private String authorizationUri;

    public AuthController(AuthService authService,
                         NaverWorksOAuthService naverWorksOAuthService,
                         MemberIntranetService memberService) {
        this.authService = authService;
        this.naverWorksOAuthService = naverWorksOAuthService;
        this.memberService = memberService;
    }

    /**
     * 네이버웍스 로그인 URL 생성
     * GET /api/intranet/auth/naver-works/login-url
     */
    @GetMapping("/naver-works/login-url")
    public ResponseEntity<Map<String, String>> getNaverWorksLoginUrl() {
        String loginUrl = authorizationUri +
            "?client_id=" + clientId +
            "&redirect_uri=" + redirectUri +
            "&response_type=code" +
            "&scope=user user.email user.profile";

        return ResponseEntity.ok(Map.of("loginUrl", loginUrl));
    }

    /**
     * 네이버웍스 OAuth Callback
     * GET /api/intranet/auth/naver-works/callback?code=xxx
     */
    @GetMapping("/naver-works/callback")
    public ResponseEntity<Map<String, Object>> naverWorksCallback(
            @RequestParam("code") String code,
            HttpSession session) {
        try {
            // 1. Access Token 발급
            Map<String, Object> tokenResponse = naverWorksOAuthService.getAccessToken(code);
            String accessToken = (String) tokenResponse.get("access_token");

            // 2. 사용자 정보 조회
            Map<String, Object> userInfo = naverWorksOAuthService.getUserInfo(accessToken);
            String email = (String) userInfo.get("email");
            String name = (String) userInfo.get("name");

            // 3. DB에서 사용자 찾기 (이메일 기준)
            MemberIntranet member = memberService.findByEmail(email);

            if (member == null) {
                // 신규 사용자 자동 등록 (옵션)
                member = MemberIntranet.builder()
                        .email(email)
                        .name(name)
                        .role("USER")
                        .isActive(true)
                        .password(authService.encodePassword("NAVER_WORKS_" + System.currentTimeMillis()))
                        .build();
                member = memberService.createMember(member);
            }

            // 4. 세션에 사용자 정보 저장
            session.setAttribute("userId", member.getId());
            session.setAttribute("userEmail", member.getEmail());
            session.setAttribute("userName", member.getName());
            session.setAttribute("userRole", member.getRole());

            // 5. Frontend로 리다이렉트
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "네이버웍스 로그인 성공",
                    "user", Map.of(
                            "id", member.getId(),
                            "email", member.getEmail(),
                            "name", member.getName(),
                            "role", member.getRole()
                    )
            ));

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("success", false, "message", "로그인 실패: " + e.getMessage()));
        }
    }

    // 기존 login, logout 메서드는 그대로 유지...
}
```

---

### 6단계: MemberIntranetService에 이메일 조회 메서드 추가

```java
/**
 * 이메일로 사원 조회 (네이버웍스 연동용)
 */
public MemberIntranet findByEmail(String email) {
    MemberIntranet member = memberMapper.findByEmail(email);
    if (member != null) {
        member.setPassword(null);  // 비밀번호 제거
        member.setSmtpPassword(null);
    }
    return member;
}
```

---

### 7단계: Frontend 연동 예시

```javascript
// 1. 로그인 버튼 클릭 시
async function loginWithNaverWorks() {
    // 네이버웍스 로그인 URL 가져오기
    const response = await fetch('http://localhost:8083/api/intranet/auth/naver-works/login-url');
    const data = await response.json();

    // 네이버웍스 로그인 페이지로 리다이렉트
    window.location.href = data.loginUrl;
}

// 2. Callback 처리 (페이지 로드 시)
// URL에 code 파라미터가 있으면 자동으로 백엔드의 callback API가 호출됨
// 백엔드에서 처리 완료 후 메인 페이지로 리다이렉트
```

---

## 🎯 Option 2: 간단한 이메일 기반 매칭 (추천)

더 간단한 방법으로, 네이버웍스 이메일과 DB 이메일을 매칭하는 방식입니다.

### 구현 방법

```java
/**
 * 네이버웍스 이메일 인증
 * POST /api/intranet/auth/naver-works-email
 */
@PostMapping("/naver-works-email")
public ResponseEntity<Map<String, Object>> loginWithNaverWorksEmail(
        @RequestBody Map<String, String> request,
        HttpSession session) {
    try {
        String email = request.get("email");

        // 네이버웍스 도메인 확인
        if (!email.endsWith("@yncsmart.com")) {
            return ResponseEntity.badRequest()
                    .body(Map.of("success", false, "message", "회사 이메일만 사용 가능합니다."));
        }

        // DB에서 사용자 찾기
        MemberIntranet member = memberService.findByEmail(email);

        if (member == null) {
            return ResponseEntity.badRequest()
                    .body(Map.of("success", false, "message", "등록되지 않은 사용자입니다."));
        }

        if (!member.getIsActive()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("success", false, "message", "비활성화된 계정입니다."));
        }

        // 세션 생성
        session.setAttribute("userId", member.getId());
        session.setAttribute("userEmail", member.getEmail());
        session.setAttribute("userName", member.getName());
        session.setAttribute("userRole", member.getRole());

        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "로그인 성공",
                "user", Map.of(
                        "id", member.getId(),
                        "email", member.getEmail(),
                        "name", member.getName(),
                        "role", member.getRole()
                )
        ));

    } catch (Exception e) {
        return ResponseEntity.badRequest()
                .body(Map.of("success", false, "message", e.getMessage()));
    }
}
```

---

## 🔐 보안 고려사항

### 1. Redirect URI 화이트리스트
```yaml
naver-works:
  oauth:
    allowed-redirect-uris:
      - http://localhost:8083/api/intranet/auth/naver-works/callback
      - https://intranet.yncsmart.com/api/intranet/auth/naver-works/callback
```

### 2. State 파라미터 (CSRF 방지)
```java
// 로그인 URL 생성 시 state 추가
String state = UUID.randomUUID().toString();
session.setAttribute("oauth_state", state);

String loginUrl = authorizationUri +
    "?client_id=" + clientId +
    "&redirect_uri=" + redirectUri +
    "&response_type=code" +
    "&state=" + state +
    "&scope=user user.email user.profile";

// Callback에서 state 검증
String receivedState = request.getParameter("state");
String sessionState = (String) session.getAttribute("oauth_state");
if (!receivedState.equals(sessionState)) {
    throw new RuntimeException("Invalid state parameter");
}
```

### 3. Token 저장 (선택)
```java
// Access Token을 세션에 저장하여 네이버웍스 API 호출 시 사용
session.setAttribute("naver_works_token", accessToken);
```

---

## 📊 DB 테이블 수정 (선택)

네이버웍스 연동 정보를 저장하려면:

```sql
-- members_intranet 테이블에 컬럼 추가
ALTER TABLE members_intranet ADD (
    naver_works_id VARCHAR2(100),      -- 네이버웍스 사용자 ID
    oauth_provider VARCHAR2(20),       -- 'NAVER_WORKS', 'LOCAL' 등
    last_login_at TIMESTAMP            -- 마지막 로그인 시간
);

CREATE INDEX idx_member_nw_id ON members_intranet(naver_works_id);
```

---

## 🧪 테스트 시나리오

### 1. OAuth 로그인 테스트
```bash
# 1단계: 로그인 URL 가져오기
curl http://localhost:8083/api/intranet/auth/naver-works/login-url

# 2단계: 브라우저에서 로그인 URL 접속
# 네이버웍스 로그인 후 자동으로 callback 호출됨

# 3단계: 세션 확인
curl -b cookies.txt http://localhost:8083/api/intranet/auth/me
```

### 2. 이메일 기반 로그인 테스트
```bash
curl -X POST http://localhost:8083/api/intranet/auth/naver-works-email \
  -H "Content-Type: application/json" \
  -d '{"email":"user@yncsmart.com"}' \
  -c cookies.txt
```

---

## 🎯 권장 구현 순서

### Phase 1: 간단한 방식부터 (1-2일)
1. 이메일 기반 매칭 구현
2. 기존 로그인과 병행 운영
3. 사용자 반응 확인

### Phase 2: Full OAuth (3-5일)
1. 네이버웍스 개발자 센터 등록
2. OAuth 2.0 플로우 구현
3. Frontend 연동
4. 테스트 및 배포

---

## 📞 추가 도움말

- **네이버웍스 API 문서**: https://developers.worksmobile.com/kr/document/
- **OAuth 2.0 가이드**: https://developers.worksmobile.com/kr/document/100500801

---

## ✅ 체크리스트

- [ ] 네이버웍스 개발자 센터 앱 등록
- [ ] Client ID/Secret 발급
- [ ] application.yml 설정
- [ ] pom.xml 의존성 추가
- [ ] NaverWorksOAuthService 작성
- [ ] AuthController에 엔드포인트 추가
- [ ] Frontend 로그인 버튼 연동
- [ ] 테스트 및 검증

---

어떤 방식으로 진행하시겠습니까?
1. **Option 1 (OAuth 2.0)** - 완전한 SSO, 보안 강화
2. **Option 2 (이메일 매칭)** - 간단하고 빠른 구현

# API Gateway 통합 전략 문서

## 📋 개요

Frontend와 Eureka 기반 마이크로서비스를 API Gateway 패턴으로 연동하는 전략입니다.

## 🏗️ 아키텍처

```
┌─────────────┐
│  Frontend   │ (Next.js - Port 3000)
│  (Next.js)  │
└──────┬──────┘
       │ HTTP Request
       │ /soccer/**
       ▼
┌─────────────────┐
│ Discovery       │ (Spring Cloud Gateway - Port 8765)
│ (API Gateway)   │
└──────┬──────────┘
       │ Service Discovery
       │ via Eureka
       ▼
┌─────────────────┐
│   Eureka        │ (Service Registry - Port 8761)
│   Server        │
└──────┬──────────┘
       │ Service Lookup
       │ "soccer-service"
       ▼
┌─────────────────┐
│ Soccer Service  │ (Spring Boot - Port 8085)
│                 │
└─────────────────┘
```

## ✅ 구현 완료 사항

### 1. Discovery Gateway 라우팅 설정

**파일**: `server/discovery/src/main/resources/application.yaml`

- ✅ Soccer Service 라우팅 추가 (`/soccer/**` → `lb://soccer-service`)
- ✅ 다른 마이크로서비스 라우팅 추가 (common, user, diary, calendar)
- ✅ Eureka Service Discovery 활성화
- ✅ CORS 설정 추가 (Frontend 접근 허용)

### 2. Frontend API 호출 변경

**파일**: `frontend/app/api/soccer/findByWord/route.ts`

- ✅ API Gateway URL 환경 변수 사용 (`NEXT_PUBLIC_API_GATEWAY_URL`)
- ✅ GET/POST 요청 모두 Gateway를 통해 전송
- ✅ 에러 메시지 업데이트 (API Gateway 연결 실패 시)

### 3. Docker 환경 설정

**파일**: `docker-compose.yaml`

- ✅ Frontend 환경 변수 추가:
  - `NEXT_PUBLIC_API_GATEWAY_URL=http://discovery:8765`
  - `NEXT_PUBLIC_API_BASE_URL=http://discovery:8765`
- ✅ Frontend가 Discovery와 Eureka에 의존하도록 설정

### 4. CORS 설정

**파일**: 
- `server/discovery/src/main/java/site/aiion/api/discovery/config/CorsConfig.java`
- `server/discovery/src/main/resources/application.yaml`

- ✅ Gateway 레벨 CORS 설정
- ✅ Frontend Origin 허용 (localhost:3000, frontend:3000)

## 🔄 요청 흐름

### 예시: Soccer Service 검색 요청

1. **Frontend** → `GET /api/soccer/findByWord?keyword=손흥민`
2. **Next.js API Route** → `GET http://discovery:8765/soccer/findByWord?keyword=손흥민`
3. **Discovery Gateway** → Eureka에서 `soccer-service` 조회
4. **Discovery Gateway** → `http://soccer-service:8085/soccer/findByWord?keyword=손흥민`로 라우팅
5. **Soccer Service** → 요청 처리 후 응답 반환
6. **Discovery Gateway** → Frontend로 응답 전달
7. **Frontend** → 사용자에게 결과 표시

## 📝 환경 변수

### 개발 환경 (로컬)
```bash
NEXT_PUBLIC_API_GATEWAY_URL=http://localhost:8765
```

### 프로덕션 환경 (Docker)
```bash
NEXT_PUBLIC_API_GATEWAY_URL=http://discovery:8765
```

## 🚀 실행 방법

### 1. 전체 서비스 실행
```bash
docker-compose up -d
```

### 2. 서비스 확인
- **Eureka Dashboard**: http://localhost:8761
- **Discovery Gateway**: http://localhost:8765
- **Frontend**: http://localhost:3000
- **Soccer Service**: http://localhost:8085

### 3. API 테스트
```bash
# Gateway를 통한 Soccer Service 호출
curl http://localhost:8765/soccer/findByWord?keyword

# Frontend API Route를 통한 호출
curl http://localhost:3000/api/soccer/findByWord?keyword
```

## 🔍 라우팅 규칙

| 경로 패턴 | 대상 서비스 | 설명 |
|---------|-----------|------|
| `/soccer/**` | `soccer-service` | 축구 관련 API |
| `/common/**` | `common-service` | 공통 서비스 API |
| `/user/**` | `user-service` | 사용자 관리 API |
| `/diary/**` | `diary-service` | 일기 관리 API |
| `/calendar/**` | `calendar-service` | 캘린더 관리 API |

## ⚠️ 주의사항

1. **서비스 등록 순서**: Eureka → Discovery Gateway → Soccer Service → Frontend
2. **네트워크**: 모든 서비스는 `spring-network`에 연결되어 있어야 함
3. **포트 충돌**: 각 서비스의 포트가 고유해야 함
4. **CORS**: Gateway에서 CORS를 처리하므로 각 서비스에서 별도 CORS 설정 불필요

## 🐛 트러블슈팅

### 문제: Frontend에서 API 호출 실패
- **확인**: Discovery Gateway가 실행 중인지 확인
- **확인**: Eureka에 soccer-service가 등록되어 있는지 확인
- **확인**: 환경 변수 `NEXT_PUBLIC_API_GATEWAY_URL` 설정 확인

### 문제: CORS 에러
- **확인**: Gateway의 CORS 설정 확인
- **확인**: Frontend Origin이 허용 목록에 있는지 확인

### 문제: 404 Not Found
- **확인**: 라우팅 경로가 올바른지 확인 (`/soccer/**`)
- **확인**: Soccer Service의 실제 엔드포인트 경로 확인

## 📚 참고 자료

- [Spring Cloud Gateway Documentation](https://spring.io/projects/spring-cloud-gateway)
- [Eureka Service Discovery](https://spring.io/projects/spring-cloud-netflix)
- [Next.js Environment Variables](https://nextjs.org/docs/basic-features/environment-variables)


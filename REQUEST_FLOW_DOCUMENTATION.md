# 요청 흐름 문서

## 📊 데이터 이동 순서

### 요청 흐름: Frontend → Eureka → Discovery Gateway → Soccer Service

```
┌─────────────┐
│  Frontend   │ (Next.js - Port 3000)
│  page.tsx   │
└──────┬──────┘
       │ Step 1: Eureka에서 서비스 조회
       ▼
┌─────────────┐
│   Eureka    │ (Service Registry - Port 8761)
│   Server    │ GET /eureka/apps/gateway-server
└──────┬──────┘
       │ Gateway 서비스 정보 반환
       ▼
┌─────────────┐
│  Discovery  │ (API Gateway - Port 8080)
│  Gateway    │ Eureka를 통해 Soccer Service 조회
└──────┬──────┘
       │ Step 3: 라우팅 (/soccer/**)
       ▼
┌─────────────┐
│   Soccer    │ (Backend Service - Port 8085)
│   Service   │ 요청 처리 및 응답
└─────────────┘
```

## 🔄 상세 단계

### Step 1: Frontend → Eureka
**위치**: `frontend/app/api/soccer/findByWord/route.ts`

```typescript
// Eureka에서 Discovery Gateway 서비스 조회
const gatewayInstance = await getServiceFromEureka('gateway-server');
```

**Eureka API 호출**:
- URL: `http://eureka:8761/eureka/apps/GATEWAY-SERVER`
- Method: GET
- Response: Gateway 서비스 인스턴스 정보 (hostName, port 등)

### Step 2: Eureka → Discovery Gateway
**위치**: `frontend/app/api/soccer/findByWord/route.ts`

```typescript
// Eureka에서 받은 Gateway 정보로 요청 전송
const gatewayUrl = `http://${gatewayInstance.hostName}:${gatewayInstance.port.$}`;
const url = `${gatewayUrl}/soccer/findByWord?...`;
```

**Discovery Gateway 요청**:
- URL: `http://discovery:8080/soccer/findByWord?keyword=...`
- Method: GET/POST
- Gateway는 내부적으로 Eureka를 통해 Soccer Service를 조회

### Step 3: Discovery Gateway → Soccer Service
**위치**: `server/discovery/src/main/resources/application.yaml`

```yaml
gateway:
  routes:
    - id: soccer-service
      uri: lb://soccer-service  # Eureka를 통한 로드 밸런싱
      predicates:
        - Path=/soccer/**
```

**라우팅 과정**:
1. Gateway가 Eureka에서 `soccer-service` 조회
2. 활성 인스턴스 선택 (로드 밸런싱)
3. `http://soccer-service:8085/soccer/findByWord`로 요청 전달

### Step 4: Soccer Service 응답
**위치**: `service/soccer-service/src/main/java/...`

- 요청 처리
- 데이터베이스 조회
- 응답 반환

## 📝 로그 예시

```
[Step 1] Eureka에서 Discovery Gateway 조회 중...
[Eureka] 서비스 조회: http://eureka:8761/eureka/apps/GATEWAY-SERVER
[Eureka] 서비스 발견: gateway-server { hostName: 'discovery', port: { $: 8765 }, ... }

[Step 2] Discovery Gateway를 통해 요청 전송: http://discovery:8765/soccer/findByWord?keyword=...
[Step 3] Soccer Service로 요청 라우팅 중...

[Step 4] Soccer Service 응답 수신 성공
```

## 🔧 환경 변수

### Frontend
```bash
NEXT_PUBLIC_EUREKA_URL=http://eureka:8761
NEXT_PUBLIC_API_GATEWAY_URL=http://discovery:8765
```

## ⚠️ 주의사항

1. **서비스 등록 순서**: Eureka → Discovery Gateway → Soccer Service
2. **Eureka 서비스 이름**: 대문자로 변환되어 저장됨 (`gateway-server` → `GATEWAY-SERVER`)
3. **네트워크**: 모든 서비스는 `spring-network`에 연결되어 있어야 함
4. **의존성**: Frontend는 Eureka와 Discovery에 의존

## 🐛 트러블슈팅

### 문제: Eureka에서 서비스를 찾을 수 없음
- **확인**: Eureka Dashboard (http://localhost:8761)에서 서비스 등록 확인
- **확인**: 서비스 이름이 올바른지 확인 (대소문자 구분)

### 문제: Gateway 라우팅 실패
- **확인**: Gateway의 라우팅 설정 확인 (`/soccer/**`)
- **확인**: Soccer Service가 Eureka에 등록되어 있는지 확인


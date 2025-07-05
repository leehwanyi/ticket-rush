# 🎟️ 티켓러시 (Ticket-Rush)

**MSA(Microservice Architecture) 기반의 대용량 트래픽 처리 콘서트 티켓팅 시스템**

이 프로젝트는 단순히 기능 구현을 넘어, 대규모 사용자가 동시에 몰리는 '티켓 예매 전쟁' 상황을 안정적으로 처리하기 위한 아키텍처 설계와 문제 해결 과정에 초점을 맞춘 포트폴리오용 프로젝트입니다.

---

## 🎯 1. 프로젝트 핵심 목표 및 도전 과제

본 프로젝트는 아래와 같이 현대적인 웹 서비스가 마주하는 현실적인 엔지니어링 문제들을 해결하는 것을 목표로 합니다.

* **동시성 제어 (Concurrency Control):** 한정된 좌석을 두고 수만 명의 사용자가 동시에 예매를 요청할 때 발생하는 **레이스 컨디션(Race Condition)**을 제어하고 데이터 정합성을 보장합니다.
    * **해결 전략:** `Redis`의 분산 락(Distributed Lock)을 활용하여 동시 요청을 처리합니다.
* **분산 트랜잭션 관리 (Distributed Transaction Management):** 여러 마이크로서비스에 걸쳐있는 비즈니스 로직(주문→결제→재고)의 데이터 일관성을 보장합니다.
    * **해결 전략:** `Kafka`를 이용한 이벤트 기반의 **사가 패턴(Saga Pattern)**을 적용하여 서비스 간의 결합도를 낮추고 트랜잭션 무결성을 유지합니다.
* **성능 최적화 및 안정성 확보 (Performance & Stability):** 대규모 트래픽 상황에서도 빠르고 안정적인 응답 속도를 유지합니다.
    * **해결 전략:** `Redis`를 활용한 다계층 캐싱 전략을 적용하고, ORM 사용 시 발생하는 N+1 쿼리 문제 등을 탐지하고 최적화합니다.

---

## 🛠️ 2. 기술 스택 (Tech Stack)

| 구분 | 기술 |
| :--- | :--- |
| **Backend** | Java 17, Spring Boot 3.x, Spring Cloud Gateway, Spring Security, Spring Data JPA |
| **Database** | MySQL, Redis |
| **Messaging** | Apache Kafka |
| **DevOps** | Docker, Docker Compose, GitHub Actions, (Kubernetes 개념 학습) |
| **Tools** | IntelliJ IDEA, Gradle, Git |

---

## 🗺️ 3. 시스템 아키텍처 (System Architecture)

### 아키텍처 다이어그램

*(주: 프로젝트 진행에 따라 아래 다이어그램을 직접 작성하여 업데이트합니다.)*

![Architecture Diagram](https://i.imgur.com/example.png)

### 마이크로서비스 책임 및 역할

| 서비스 명 | 핵심 책임 | 주요 기술 및 패턴 |
| :--- | :--- | :--- |
| **API Gateway** | 단일 진입점, 라우팅, 인증/인가, 로드 밸런싱 | Spring Cloud Gateway |
| **User Service** | 회원가입, 로그인, 프로필 관리, JWT 발급 | Spring Security, JPA |
| **Event Service** | 공연 정보, 좌석 배치도 등 메타 데이터 CRUD | JPA, Redis (Caching) |
| **Inventory Service**| 좌석 상태 관리, 동시성 제어 | **Redis (Distributed Lock)**, JPA |
| **Order Service** | 주문 생성 및 관리, Saga 트랜잭션 오케스트레이션| **Kafka, Saga Pattern**, JPA |
| **Payment Service** | 외부 결제 연동, 결제 처리 | **Kafka Listener**, JPA |
| **Notification Service**| 이메일, SMS 등 비동기 알림 발송 | **Kafka Listener** |

---

## 🚀 4. 로컬 개발 환경 설정 (Local Development Setup)

### 사전 준비 사항

* Java 17
* Gradle
* Docker & Docker Compose
* Git

### 실행 방법

1.  프로젝트를 Git으로 클론합니다.
2.  프로젝트 루트 디렉토리에서 아래 명령어를 실행하여 모든 외부 인프라(DB, Redis, Kafka)를 실행합니다.
    ```bash
    docker-compose up -d
    ```
3.  IntelliJ IDEA에서 `ticket-rush` Gradle 프로젝트를 엽니다.
4.  각 마이크로서비스(`user-service` 등)의 `Application.java`를 실행합니다.

---

## ✅ 5. 구현 로드맵 및 체크리스트

### Phase 1: 프로젝트 기초 공사 및 환경 설정
- [x] IntelliJ 멀티 모듈 프로젝트 구조 생성 (`ticket-rush` 부모, `user-service` 자식)
- [x] Homebrew 및 Git 설치 및 초기 설정
- [x] 로컬 프로젝트 Git 저장소로 초기화 (`git init`)
- [x] `.gitignore` 파일 설정
- [x] GitHub 원격 저장소 생성 및 연동 (`git push`)
- [ ] `docker-compose.yml` 파일 작성 (MySQL, Redis, Kafka)

### Phase 2: `user-service` 구현 (인증/인가)
- [ ] **데이터 모델링**
    - [ ] `User` Entity 생성 (`id`, `email`, `password`, `username`, `role`)
    - [ ] `UserRole` Enum 생성 (`USER`, `ADMIN`)
- [ ] **데이터베이스 연동**
    - [ ] `UserRepository` Interface 생성 (`findByEmail` 메서드 추가)
- [ ] **API 설계**
    - [ ] `dto` 패키지 생성
    - [ ] `SignUpRequestDto`, `LoginRequestDto` 생성 (Validation 추가)
- [ ] **비즈니스 로직**
    - [ ] `UserService` 생성
    - [ ] 회원가입 로직 구현 (이메일 중복 확인, 비밀번호 암호화)
    - [ ] 로그인 로직 뼈대 구현
- [ ] **API 엔드포인트**
    - [ ] `UserController` 생성
    - [ ] `POST /api/users/signup`, `POST /api/users/login` 엔드포인트 구현
- [ ] **보안 설정**
    - [ ] `config` 패키지 및 `SecurityConfig` 생성
    - [ ] `PasswordEncoder` Bean 등록
    - [ ] URL별 접근 권한 설정 (`signup`, `login`은 `permitAll`)

### Phase 3: 핵심 서비스 기능 구현 (TBD)
- [ ] `event-service` 구현 (공연 정보 관리)
- [ ] `inventory-service` 구현 (좌석 재고 관리 - 동시성 처리 이전)
- [ ] `order-service` 구현 (기본 주문 로직)

### Phase 4: 핵심 문제 해결 (심화 구현) (TBD)
- [ ] **동시성 제어:** `inventory-service`에 Redisson 분산 락 적용
- [ ] **분산 트랜잭션:** `order-service` 중심으로 Kafka와 Saga Pattern 적용
- [ ] **성능 최적화:** `event-service`에 Redis 캐싱 적용 및 N+1 문제 해결

### Phase 5: DevOps 및 배포 (TBD)
- [ ] 각 서비스별 `Dockerfile` 작성
- [ ] GitHub Actions를 이용한 CI/CD 파이프라인 구축 (빌드, 테스트, 이미지 푸시 자동화)

---

## 📖 6. API 명세 (API Specification)

*(Swagger/OpenAPI 도입 전까지 Markdown으로 관리합니다.)*

### User Service

| Method | Endpoint | 설명 | Request Body | Success Response |
| :--- | :--- | :--- | :--- | :--- |
| `POST` | `/api/users/signup`| 회원가입 | `SignUpRequestDto` | `201 Created` + 성공 메시지 |
| `POST` | `/api/users/login` | 로그인 | `LoginRequestDto` | `200 OK` + JWT |
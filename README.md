# 🎟️ 티켓러시 (Ticket-Rush)

**MSA(Microservice Architecture) 기반의 대용량 트래픽 처리 콘서트 티켓팅 시스템**

이 프로젝트는 단순히 기능 구현을 넘어, 대규모 사용자가 동시에 몰리는 '티켓 예매 전쟁' 상황을 안정적으로 처리하기 위한 아키텍처 설계와 문제 해결 과정에 초점을 맞춘 포트폴리오용 프로젝트입니다.

---

## 🎯 1. 프로젝트 핵심 목표 및 도전 과제

본 프로젝트는 아래와 같이 현대적인 웹 서비스가 마주하는 현실적인 엔지니어링 문제들을 해결하는 것을 목표로 합니다.

* **동시성 제어 (Concurrency Control):** 한정된 좌석을 두고 수만 명의 사용자가 동시에 예매를 요청할 때 발생하는 레이스 컨디션(Race Condition)을 제어하고 데이터 정합성을 보장합니다.
    * **해결 전략:** `Redis`의 분산 락(Distributed Lock)을 활용하여 동시 요청을 처리합니다.
* **분산 트랜잭션 관리 (Distributed Transaction Management):** 여러 마이크로서비스에 걸쳐있는 비즈니스 로직(주문→결제→재고)의 데이터 일관성을 보장합니다.
    * **해결 전략:** `Kafka`를 이용한 이벤트 기반의 사가 패턴(Saga Pattern)을 적용하여 서비스 간의 결합도를 낮추고 트랜잭션 무결성을 유지합니다.
* **성능 최적화 및 안정성 확보 (Performance & Stability):** 대규모 트래픽 상황에서도 빠르고 안정적인 응답 속도를 유지합니다.
    * **해결 전략:** `Redis`를 활용한 다계층 캐싱 전략을 적용하고, ORM 사용 시 발생하는 N+1 쿼리 문제 등을 탐지하고 최적화합니다.

---

## 🛠️ 2. 기술 스택 (Tech Stack)

| 구분 | 기술                                                                                 |
| :--- |:-----------------------------------------------------------------------------------|
| **Backend** | Java 17, Spring Boot 3.5.3, Spring Cloud Gateway, Spring Security, Spring Data JPA |
| **Database** | MySQL, Redis                                                                       |
| **Messaging** | Apache Kafka                                                                       |
| **DevOps** | Docker, Docker Compose, GitHub Actions, (Kubernetes 개념 학습)                         |
| **Tools** | IntelliJ IDEA, Gradle, Git                                                         |

---

## 🗺️ 3. 시스템 아키텍처 (System Architecture)

### 아키텍처 다이어그램



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

## 🤔 4. 아키텍처 설계 결정 (Architectural Decisions)

### 공통관리

* 문제상황
  * 여러 마이크로서비스(user-service, order-service 등)의 엔티티가 공통으로 BaseTimeEntity(생성/수정 시간) 클래스를 상속받아야 할 때, 이 공통 코드를 어떻게 관리할 것인가?
* 고려한 옵션들
  * 옵션 1 : 각 서비스에 코드 복사
    * 장점 : 서비스 간 의존성이 전혀 없어 MSA의 '완벽한 독립성' 철학에 부합함.
    * 단점 : 코드 중복 발생. 공통 코드 수정 시 모든 서비스에 반영해야 하므로 실수가 발생하기 쉽고 유지보수가 어려움.
  * 옵션 2 : 공통 모듈 분리
    * 장점 : 코드를 한 곳에서 관리하므로 중복이 없고 일관성 유지가 용이함.
    * 단점 : 모든 서비스가 common-core 모듈에 의존하게 되어 서비스 간 결합도가 소폭 증가함.
  * 최종 결정 및 결정 사유
    * 이 프로젝트는 포트폴리오로써, 모듈화 설계 능력과 유지보수성을 고려하는 모습을 보여주는 것이 더 중요하다고 판단했습니다. BaseTimeEntity 클래스와 같이 변경 가능성이 낮고 안정적인 코드인 경우, 결합도 증가로 인한 단점보다 중앙 관리의 이점이 더 크다고 판단했습니다.


---

## 🚀 5. 로컬 개발 환경 설정 (Local Development Setup)

### 사전 준비 사항

* Java 21
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

### 🌐 6. 형상 관리 전략 (Git-Flow Strategy)

본 프로젝트는 체계적인 코드 관리와 안정적인 배포를 위해 **Git-Flow** 브랜칭 모델을 따릅니다. 각 브랜치는 명확한 역할을 가지며, 이를 통해 효율적인 기능 개발과 배포 파이프라인을 관리합니다.

#### 브랜치 종류 및 역할

| 브랜치 | 역할 | 분기 시작 (From) | 병합 대상 (To) | 네이밍 규칙 |
| :--- | :--- | :--- | :--- | :--- |
| **`main`** | **프로덕션 릴리즈**<br>가장 안정적인 최신 버전 | `release`, `hotfix` | - | - |
| **`dev`** | **다음 버전 개발**<br>기능 통합 및 테스트 | `main` | `release` | - |
| **`feature`** | **신규 기능 개발**<br>독립적인 기능 단위 | `dev` | `dev` | `feature/{기능}` |
| **`release`** | **배포 준비**<br>최종 버그 수정, 버전 관리 | `dev` | `main`, `dev` | `release/{버전}` |
| **`hotfix`** | **긴급 버그 수정**<br>프로덕션 이슈 해결 | `main` | `main`, `dev` | `hotfix/{이슈}` |

<br>

#### 커밋 메시지 규칙 (Commit Message Convention)

프로젝트의 커밋 히스토리를 명확하게 관리하기 위해 **Conventional Commits** 규칙을 따릅니다.

### 🏛️ 7. 적용된 주요 디자인 패턴 (Key Design Patterns)

본 프로젝트는 MSA의 복잡성을 해결하고, 코드의 유연성, 확장성, 유지보수성을 높이기 위해 다음과 같은 디자인 패턴을 적극적으로 활용했습니다.

---

#### 1. 마이크로서비스 아키텍처 패턴 (Microservice Architectural Patterns)

| 패턴 명 | 문제 상황 | 해결 방법 및 구현 |
| :--- | :--- | :--- |
| **API Gateway** | 클라이언트가 여러 서비스의 엔드포인트를 모두 알아야 하고, 인증/로깅 등 공통 기능을 각 서비스마다 중복 구현해야 하는 문제 | **`Spring Cloud Gateway`**를 단일 진입점으로 사용하여 라우팅, 중앙 인증, 로드 밸런싱, 서킷 브레이커 등 Cross-Cutting Concern을 효과적으로 처리합니다. |
| **Service Discovery** | 서비스의 IP 주소나 포트가 변경될 때마다 이를 사용하는 다른 서비스의 설정을 모두 수정해야 하는 문제 | **`Netflix Eureka`**를 도입하여 각 서비스가 시작 시 자신의 위치를 Eureka 서버에 동적으로 등록하고, 다른 서비스들은 서비스 이름(e.g., `USER-SERVICE`)만으로 해당 서비스의 실제 위치를 찾아 통신합니다. |
| **Saga (Orchestration)** | 여러 서비스에 걸친 비즈니스 로직(주문→결제→재고)의 데이터 일관성을 보장해야 할 때, 분산 트랜잭션으로 인한 강한 결합과 성능 저하 문제 | **`Order Service`**가 오케스트레이터 역할을 맡아 전체 트랜잭션 흐름을 지휘합니다. **`Kafka`**를 통해 "OrderCreated" 이벤트를 발행하면, 다른 서비스들이 이를 구독하여 자신의 로컬 트랜잭션을 수행하고 결과 이벤트를 다시 발행하는 방식으로 데이터 일관성을 유지합니다. |
| **Circuit Breaker** | 특정 서비스 하나에 장애가 발생하면, 해당 서비스를 호출하는 다른 서비스들까지 연쇄적으로 장애가 전파되어 시스템 전체가 마비될 수 있는 문제 | **`Spring Cloud Circuit Breaker (Resilience4j)`**를 API Gateway나 Feign Client에 적용합니다. 특정 서비스에 장애가 발생하면 요청을 일시적으로 차단하고, 미리 정의된 폴백(Fallback) 로직(e.g., "현재 서비스가 원활하지 않습니다.")을 실행하여 장애 전파를 막습니다. |
| **Cache-Aside** | 자주 조회되지만 변경은 드문 데이터를 매번 DB에서 조회하여 발생하는 불필요한 부하와 느린 응답 속도 문제 | **`Redis`**를 캐시 저장소로 활용합니다. 데이터를 조회할 때, 먼저 캐시를 확인(Cache Hit)하고 데이터가 없으면(Cache Miss) DB에서 조회한 뒤, 그 결과를 캐시에 저장하고 반환하여 DB 부하를 줄이고 응답 속도를 향상시킵니다. |

---

#### 2. 객체 지향 설계 패턴 (Object-Oriented Design Patterns)

| 구분 | 패턴 명                        | 적용 시나리오 및 기대 효과                                                                                                                                                                                     |
| :--- |:----------------------------|:----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **생성 (Creational)** | Builder                     | **`Order`, `Event`** 등 복잡한 객체 생성 시, `@Builder`를 사용하여 명확하고 가독성 높은 코드로 객체를 생성하고 불변성을 유지합니다.                                                                                                           |
| | **Factory Method**          | `Notification Service`에서 알림 종류(EMAIL, SMS, PUSH)에 따라 각기 다른 `NotificationSender` 객체를 생성할 때 유용합니다. 클라이언트는 구체적인 생성 클래스를 알 필요 없이, 팩토리에 타입만 넘겨주면 원하는 객체를 얻을 수 있어 결합도를 낮춥니다.                              |
| | **Singleton**               | `Spring Framework`의 IoC 컨테이너가 모든 Bean(`@Service`, `@Repository` 등)을 기본적으로 싱글턴으로 관리합니다. 이를 통해 애플리케이션 전역에서 유일한 인스턴스를 공유하며 리소스를 효율적으로 사용합니다.                                                           |
| **구조 (Structural)** | **Adapter**                 | `Payment Service`가 다양한 외부 결제 PG사(카카오페이, 토스페이먼츠 등)와 연동해야 할 때, 각기 다른 PG사의 API 명세를 우리 시스템의 표준 인터페이스에 맞게 변환해주는 `Adapter` 클래스를 구현하여 호환성 문제를 해결하고 새로운 PG사 추가를 용이하게 합니다.                                   |
| **행동 (Behavioral)** | **Strategy**                | **다양한 할인 정책 적용** 시, 각 할인 로직(VIP 할인, 쿠폰 할인 등)을 별도의 `Strategy` 클래스로 캡슐화합니다. 런타임에 주문 조건에 맞는 할인 전략을 동적으로 선택하여 적용하므로, 새로운 할인 정책이 추가되어도 기존 코드를 수정할 필요가 없어 **OCP(개방-폐쇄 원칙)**를 만족시킵니다.                      |
| | **Chain of Responsibility** | **주문 유효성 검증** 시, 여러 검증 로직(사용자 인증, 재고 확인, 연령 제한 등)을 각각의 `Handler` 클래스로 만들고 체인으로 연결합니다. 요청은 체인을 따라 순서대로 검증을 통과해야만 최종 처리되므로, 각 검증 로직의 책임을 명확히 분리하고 순서 변경이나 규칙 추가에 유연하게 대처할 수 있습니다.                     |
| | **Observer**                | **`Kafka`를 이용한 비동기 이벤트 처리** 자체가 분산 환경에서 구현된 옵저버 패턴입니다. `Order Service`(Subject)가 이벤트를 발행하면, 이를 구독하는 `Payment Service`(Observer)가 알림을 받고 작업을 수행합니다. 발행자와 구독자 간의 결합도를 크게 낮춰 시스템 전체의 유연성과 확장성을 극대화합니다. |
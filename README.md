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
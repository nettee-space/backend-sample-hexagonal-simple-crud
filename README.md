# Introduction.

이 샘플 프로젝트는 레이어드 아키텍처를 넘어 확장성과 유연성이 보장되고 MSA 철학에 걸맞은 헥사고날 아키텍처 기반으로 구현되었습니다.

기본적인 CRUD 동작과 헥사고날 구조를 파악과 개념을 익힐 수 있도록 하였습니다.

다음 항목을 포함합니다.

- 헥사고날 아키텍처의 의미 수준의 구현을을 따릅니다.
- 폴더 구조의 시작점은 **높은 응집도를 위해** 도메인 기준으로 설계합니다. 

```
me.nettee
├── common
└── board
    ├── adapter                 -- 외부와의 상호작용을 처리하는 계층, application 입출력을 담당
    │   ├── driving             -- 외부로부터의 입력을 처리하는 어댑터
    │   │   └── web             -- Web 요청을 처리하는 컨트롤러 및 DTO 정의
    │   │       ├── dto
    │   │       └── mapper      -- DTO ↔ Domain 매핑 클래스
    │   └── driven              -- 외부로의 출력을 처리하는 어댑터 (DB, Event, Messaging 처리)
    │       └── persistence     -- 영속성 계층의 구현체 (예: JPA, MyBatis 등)
    │           ├── entity
    │           └── mapper      -- Entity ↔ Domain 매핑 클래스
    └── application             -- 핵심 비즈니스 로직이 포함된 계층, 도메인과 유스케이스를 정의
        ├── domain              -- 비즈니스 도메인 정의
        ├── port                -- adpater와 상호작용을 위한 인터페이스 정의
        ├── service             -- 비즈니스 로직을 처리하는 서비스 클래스
        └── usecase             -- 특정 유스케이스(기능) 인터페이스 정의
```

Adapter는 Application 계층에 정의된 port를 구현합니다.  

- Driving Adapter는 시스템 외부에서 들어오는 요청을 담당합니다. ( HTTP 요청, 메시지 소비 ) 이후 애플리케이션에 전달합니다.
- Driven Adpater는 시스템이 외부로 나가는 작업을 담당합니다. ( DB CRUD 작업, 외부 API 호출, 메시지 전달)  
  - Driven Adapter는 Spring Data JPA를 사용합니다.

# Prerequisites

- JDK 21  
  You can use OpenJDK e.g. Amazon Corretto 21

# Branch Rule 

개발자들은 다음과 같은 Branch Rule을 꼭 숙지하고 준수해 주시기 바랍니다.

- **main 브랜치는 읽기 전용 입니다.**
  - main 브랜치는 관리자(`@merge-simpson`, `@silberbullet`)만 force push가 가능합니다.
  - 모든 변경 사항은 feature 브랜치를 생성 후, main 브랜치로 병합해야 합니다.
  
- **병합 전 Pull Request는 필수입니다.**
  - Pull Request를 생성할 때, 최소 2명의 reviewer를 지정해야 합니다.
  - 관리자(@merge-simpson, @silberbullet)는 리뷰 없이 병합이 가능합니다.
  - **코드에 대한 모든 논의(conversations)가 해결되지 않은 상태에서는 Pull Request를 병합할 수 없습니다.**
    - 예시)
      1. @silberbullet 님이 pull request 생성 후, reviewer를 @merge-simpson 에게 신청하였습니다.
      2. @merge-simpson 님은 코드 수정을 위해 comment를 남겼습니다. 
      3. @silberbullet 님은 해당 코드를 수정하여 push 후  @merge-simpson님이 남긴 comment에 수정사항을 적어놓았습니다.
      4. @merge-simpson "Resolve conversation" 버튼을 클릭하여 피드백이 해결되었음을 표시합니다.
      5. 비로서 @silberbullet 님은 코드 병합이 가능합니다.

# Basic Commit Message Type

기본적인 커밋 메시지의 첫 단어는 작업의 목적을 명확히 하기 위해 타입으로 시작합니다. 

다음 아래의 타입을 실습으로 사용해보시면 좋습니다.

- feat: 새로운 기능 추가
- fix: 버그 수정
- docs: 문서 수정 (README.md 등)
- refactor: 코드 리팩토링 (기능 변화 없음)
- test: 테스트 코드 추가 또는 수정
- chore: 빌드 작업, 패키지 매니저 설정, 기타 작업 등

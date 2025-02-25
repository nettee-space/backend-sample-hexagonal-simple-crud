- [**Sample Code Registry**](https://github.com/nettee-space/backend-sample-code-registry)  
  1. ⠀⠀ [**Layerd**](https://github.com/nettee-space/backend-sample-layered-simple-crud)  
  2. ▶⠀ <ins>**Hexagonal**</ins> (Here)  
  3. ⠀⠀ **Multi-Module Project** (Coming soon)

# Introduction.

이 샘플 프로젝트는 레이어드 아키텍처를 넘어 확장성과 유연성을 보장하고 DDD 철학에 걸맞는 헥사고날 아키텍처 기반으로 구현되었습니다.

기본적인 CRUD 동작과 헥사고날 아키텍처의 구조를 파악하고 관점을 익힐 수 있도록 하였습니다.

다음 항목을 포함합니다.

- 헥사고날 아키텍처의 의미 수준의 구현을 따릅니다.
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

- Driving Adapter는 시스템 외부에서 들어오는 요청을 담당합니다. (HTTP 요청, 메시지 소비) 이후 애플리케이션에 전달합니다.
- Driven Adpater는 시스템이 외부로 나가는 작업을 담당합니다. (DB CRUD 작업, 외부 API 호출, 메시지 전달)  
  - 실습에서 Driven Adapter는 RDB Adapter로, Spring Data JPA를 사용합니다.

# Prerequisites

- JDK 21  
  You can use OpenJDK e.g. Amazon Corretto 21

# Branch Rule 

개발자들은 다음과 같은 Branch Rule을 꼭 숙지하고 준수해 주시기 바랍니다. (간소화된 브랜치 운영)

- **main 브랜치는 읽기 전용 입니다.**
  - main 브랜치는 관리자([`@merge-simpson`](https://github.com/merge-simpson), [`@silberbullet`](https://github.com/silberbullet))만 force push가 가능합니다.
- **feature 브랜치**: 모든 변경 사항은 <ins>feature 브랜치</ins>를 생성 후, main 브랜치로 병합해야 합니다.
  - `feature/기능명` 양식으로 명명하며, 영문 소문자, 숫자 및 하이픈(케밥 케이스)를 사용합니다. (추가적인 슬래시를 사용하지 않습니다.)
    
    ```mermaid
      gitGraph
      commit
      commit
      branch feature/board-example
      branch feature/board-something
      checkout feature/board-example
      commit
      checkout feature/board-something
      commit
      commit
      checkout feature/board-example
      commit
      checkout main
      merge feature/board-example
      checkout main
      merge feature/board-something
      commit
    ```
  
- **주요 브랜치에 병합 전 Pull Request(PR)는 필수입니다.**
  - Pull Request를 생성할 때, 최소 2명의 reviewer를 지정해야 합니다.
  - 관리자([@merge-simpson](https://github.com/merge-simpson), [@silberbullet](https://github.com/silberbullet))는 리뷰 없이 병합이 가능합니다.
  - **코드에 대한 모든 논의(conversations)가 해결(resolved)되지 않은 상태에서는 Pull Request를 병합할 수 없습니다.**
    <details>
    <summary>conversations 예시 보기</summary>
    
    1. @silberbullet 님이 pull request 생성 후, reviewer를 @merge-simpson 에게 신청하였습니다.  
    2. @merge-simpson 님은 코드 수정을 위해 comment를 남겼습니다.  
    3. @silberbullet 님은 해당 코드를 수정하여 push 후 @merge-simpson 님이 남긴 comment에 수정사항을 적어 놓았습니다.  
    4. @merge-simpson "Resolve conversation" 버튼을 클릭하여 피드백이 해결되었음을 표시합니다.  
    5. 비로소 @silberbullet 님은 코드 병합이 가능합니다.  
    
    </details>

# Commit Message

커밋 메시지의 제1 규칙은 '알아볼 수 있는 메시지 전달'입니다.  
보편적인 앵귤러 커밋 메시지 컨벤션을 따르면서, 각 포맷의 바운더리와 표현 수준은 팀에 맞게 차근차근 조정해 가면 좋겠습니다.

## Basic Commit Message Format

커밋 메시지의 첫 단어는 작업의 목적을 명확히 하기 위해 커밋 타입으로 시작합니다.  

> **type**(scope): subject in lowercase  

아래의 타입을 실습으로 사용해 보시면 좋습니다.

- **feat**: 새로운 기능 추가
- **fix**: 버그 수정
- **docs**: 문서 생성 및 수정 (README.md 등)
- **refactor**: 코드 리팩토링 (기능 변화 없음: 성능 개선, 패키지 이동, 파일·식별자 수정 등)
- **test**: 테스트 코드 추가 또는 수정
- **chore**: 코드의 구조나 동작에 영향을 주지 않는 기타 작업
- **build**: 빌드 관련 작업, 패키지 매니저 설정 등

# Contact.

- [:octocat: Merge Simpson](https://github.com/merge-simpson)
- [:octocat: Silberbullet](https://github.com/silberbullet) (No silver bullet)

# Introduction.

이 샘플 프로젝트는 레이어드 아키텍처를 넘어 확장성과 유연성이 보장되고 MSA 철학에 걸맞은 헥사고날 아키텍처 기반으로 구현되었습니다.

기본적인 CRUD 동작과 헥사고날 구조를 파악과 개념을 익힐 수 있도록 하였습니다.

다음 항목을 포함합니다.

- 헥사고날 아키텍처의 의미 수준의 구현을을 따릅니다.
- 폴더 구조의 시작점은 **낮은 응집도를 위해** 도메인 기준으로 설계합니다. 

```
      └─me.nettee
          ├─board
          │  ├─adapter              --  외부와의 상호작용을 처리하는 계층, application 입출력을 담당
          │  │  ├─driving                  --  외부로부터의 입력을 처리하는 어댑터
          │  │  │  ├─mapper             --  DTO <-> Domain 매핑 클래스
          │  │  │  └─web                -- Web 요청을 처리하는 컨트롤러 및 DTO 정의
          │  │  │      └─dto
          │  │  └─driven                 --  외부로부터의 출력을 처리하는 어댑터 ( DB, Event, Message 처리 ) 
          │  │      ├─mapper            --  Entity <-> Domain 매핑 클래스
          │  │      └─persistence       -- 영속성 계층의 구현체 (예: JPA, MyBatis 등)
          │  │          └─entity
          │  └─application            -- 핵심 비즈니스 로직이 포함된 계층, 도메인과 유스케이스를 정의
          │      ├─domain               -- 비즈니스 도메인 정의
          │      ├─port                 -- adpater와 상호작용을 위한 인터페이스 정의
          │      ├─service              -- 비즈니스 로직을 처리하는 서비스 클래스
          │      └─usecase              -- 특정 유스케이스(기능) 인터페이스 정의
          └─common
```

Adapter는 Application 계층에 정의된 port를 구현합니다.  

- Driving Adapter는 시스템 외부에서 들어오는 요청을 담당합니다. ( HTTP 요청, 메시지 소비 ) 이후 애플리케이션에 전달합니다.
- Driven Adpater는 시스템이 외부로 나가는 작업을 담당합니다. ( DB CRUD 작업, 외부 API 호출, 메시지 전달)  
  - Driven Adapter는 Spring Data JPA를 사용합니다.

# Prerequisites

- JDK 21  
  You can use OpenJDK e.g. Amazon Corretto 21

# Branch Rule

- main 브랜치는 push 가 불가능 합니다.
  - feature 브랜치 생성 후 병합을 해야합나다.
- 병합 전 무조건 PR을 받도록 설정 하였습니다.
  - 리뷰어는 최소 2명을 만족해야 합나디.
  - 리뷰어가 모두 



# Board

Spring Boot 기반의 간단한 게시판 프로젝트입니다. 글 작성, 댓글, 카테고리 관리와 같은 기본 기능을 연습하기 위해 만들어졌습니다.

## 주요 기능
- 회원 가입 및 로그인 (Spring Security, BCrypt)
- 게시글 작성/조회/수정/삭제
- 댓글 및 대댓글 작성/수정/삭제
- 카테고리 관리
- 파일 업로드 지원
- Querydsl을 이용한 게시글 검색 및 페이징
- 로그인 세션 주입을 위한 커스텀 어노테이션 및 인터셉터

## 기술 스택
- Java 21
- Spring Boot 3.x / Spring MVC
- Spring Data JPA, Querydsl
- Thymeleaf 3, Bootstrap 5
- MySQL (개발 시 H2 사용 가능)
- Gradle

## 프로젝트 구조
```
src/main/java/kkmm/back/board
├─ BoardApplication.java
├─ security/               # 보안 설정
├─ domain/                 # 엔티티, 레포지토리, 서비스
└─ web/                    # 컨트롤러, DTO, 인터셉터 등
```

## 로컬 실행 방법
1. `src/main/resources/application.properties`의 데이터베이스 설정을 환경에 맞게 수정합니다. (또는 별도의 `application-local.properties` 파일을 사용합니다.)
2. 다음 명령어로 실행합니다.

```bash
./gradlew bootRun
```

`local` 프로파일로 실행하면 `TestDataInit` 클래스가 샘플 데이터를 자동으로 삽입합니다. 애플리케이션 기본 포트는 8080이며 `/` 경로로 접속하면 게시글 목록 페이지로 이동합니다.

```


# 📌 Board – 연습용 게시판 프로젝트
> Spring Boot · JPA · MySQL · Thymeleaf · Docker  
> 개인 학습을 위해 구축한 CRUD 기반 게시판

## 1. 프로젝트 개요
간단한 **게시글 등록·조회·수정·삭제(CRUD)** 기능을 구현하면서  
Spring 생태계 전반(JPA, 인증·인가, 테스트, 배포)에 대한 경험을 쌓기 위한 연습용 저장소입니다.

## 2. 기술 스택
|  Layer  |  사용 기술 |
|---------|-----------|
| **Language** | Java 17 | 
| **Backend**  | Spring Boot 3.x, Spring MVC, Spring Security |
| **DB** | MySQL 8.x <br> (Test: H2 in‑memory) | 
| **ORM** | Spring Data JPA, Hibernate | 
| **View** | Thymeleaf 3, Bootstrap 5 | 
| **Test** | JUnit 5, Spring Boot Test, Mockito | 
| **Build / CI** | Gradle (Kotlin DSL) or Maven <br> GitHub Actions | 
| **Deploy** | Docker, Docker Compose | 

## 3. 주요 기능
- **게시판**
  - 글 목록 페이징 & 검색(제목/내용/작성자)
  - 상세 보기, 글쓰기, 수정, 삭제(Soft Delete)
  - 첨부파일 업로드 (S3 또는 로컬)
- **회원**
  - 회원가입 / 로그인 (Spring Security & BCrypt)
  - OAuth2 소셜 로그인(Google, GitHub) · 선택 사항
  - 권한에 따른 메뉴/버튼 노출
- **공통**
  - 전역 Exception 처리(ControllerAdvice)
  - AOP 기반 로깅 · 성능 모니터링
  - API 문서(Swagger 3 / SpringDoc)
  - GitHub Actions + DockerHub 자동 배포 파이프라인

## 4. 프로젝트 구조
```
board
├─ src
│ ├─ main
│ │ ├─ java/com/example/board
│ │ │ ├─ config
│ │ │ ├─ domain # 엔티티 & 레포지토리
│ │ │ ├─ service
│ │ │ ├─ controller
│ │ │ └─ dto
│ │ └─ resources
│ │ ├─ application.yml
│ │ └─ templates # Thymeleaf
│ └─ test
│ └─ … # 단위/통합 테스트
└─ docker-compose.yml
```

## 5. 빠른 시작

### 5‑1. 로컬 실행
```bash
# 1) .env 혹은 application-local.yml 준비
cp .env.example .env   # DB, JWT 시크릿 등 채워넣기

# 2) 빌드 & 실행
./gradlew clean build
java -jar build/libs/board-0.0.1-SNAPSHOT.jar


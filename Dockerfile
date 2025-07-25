# 1) 빌드 스테이지
FROM gradle:8.7-jdk21 AS build
WORKDIR /app
COPY . .
RUN gradle clean build -x test

# 2) 런타임 스테이지
FROM eclipse-temurin:21-jre
WORKDIR /app

# 프로파일/포트 환경 변수
ENV SPRING_PROFILES_ACTIVE=prod \
    TZ=Asia/Seoul

# JAR 복사
COPY build/libs/spring-thyme-app-0.0.1-SNAPSHOT.jar /app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar"]

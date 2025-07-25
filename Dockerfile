# ---------- 1) Build stage ----------
FROM gradle:8.7-jdk21 AS builder
WORKDIR /app
COPY . .
# 테스트 제외하고 빌드
RUN ./gradlew bootJar -x test --no-daemon

# ---------- 2) Runtime stage ----------
FROM eclipse-temurin:21-jre
WORKDIR /app

ENV SPRING_PROFILES_ACTIVE=prod \
    TZ=Asia/Seoul

# 실행 가능한 jar만 복사
COPY --from=builder /app/build/libs/*-SNAPSHOT.jar /app.jar

EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar"]

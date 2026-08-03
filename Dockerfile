# ---- 빌드 단계 ----
FROM eclipse-temurin:21-jdk AS build
WORKDIR /app
COPY . .
RUN chmod +x gradlew && ./gradlew bootJar -x test --no-daemon

# ---- 실행 단계 ----
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/build/libs/Healthcare_BE-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
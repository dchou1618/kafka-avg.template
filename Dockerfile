FROM gradle:9.2.1-jdk21 AS builder
WORKDIR /app

COPY . .

RUN gradle clean build --no-daemon

FROM eclipse-temurin:21-jre
WORKDIR /app

COPY --from=builder /app/app/build/libs/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
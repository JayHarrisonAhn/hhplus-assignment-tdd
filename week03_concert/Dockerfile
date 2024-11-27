# java 17 multi stage build
FROM gradle:8.4.0-jdk17 AS builder
ENV spring_profiles_active qa
WORKDIR /app
COPY . .
RUN gradle clean build -x test

FROM openjdk:17-ea-11-jdk-slim
ENV spring_profiles_active qa
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
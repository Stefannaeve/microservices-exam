FROM maven:3.9.4-eclipse-temurin-21 AS builder
WORKDIR /app
COPY pom.xml pom.xml
COPY src ./src
RUN mvn package

FROM amazoncorretto:21-alpine3.20-jdk
WORKDIR /app
COPY --from=builder /app/target/*.jar /app/application.jar
ENTRYPOINT ["java", "-jar", "/app/application.jar"]
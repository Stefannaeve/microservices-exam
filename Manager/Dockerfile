FROM maven:3.9-amazoncorretto-23-alpine as builder
WORKDIR /app
COPY pom.xml pom.xml
COPY src/ src/
RUN mvn package

FROM amazoncorretto:23-alpine-full
COPY --from=builder /app/target/*.jar /app/application.jar
CMD ["java","-jar", "-Dspring.profiles.active=docker", "/app/application.jar"]
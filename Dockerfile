FROM ubuntu:latest
LABEL authors="leo_m"

FROM openjdk:17-oracle

WORKDIR /app

COPY ./target/*.jar /app/

EXPOSE 8090

ENTRYPOINT ["java", "-jar", "app.jar"]
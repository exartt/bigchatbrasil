FROM openjdk:17-oracle

LABEL authors="leo_m"

WORKDIR /app

COPY ./.env /app/
COPY ./target/*.jar bigchatbrasil-0.0.1.jar

EXPOSE 8090

ENTRYPOINT ["java", "-jar", "bigchatbrasil-0.0.1.jar"]
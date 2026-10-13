FROM eclipse-temurin:17-jdk

WORKDIR /app

COPY target/*.jar spring-mqtt-client.jar

EXPOSE 8080 

ENTRYPOINT  ["java", "-jar", "spring-mqtt-client.jar"]

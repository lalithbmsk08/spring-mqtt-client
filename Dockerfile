FROM eclipse-temurin:17-jdk
WORKDIR /app
COPY .target/spring-mqtt-client.jar /app
EXPOSE 8080 
CMD ["java", "-jar", "/spring-mqtt-client.jar"]

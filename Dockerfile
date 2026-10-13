FROM eclipse-temurin:17-jdk
EXPOSE 8080 
ADD target/spring-mqtt-client.jar spring-mqtt-client.jar
CMD ["java", "-jar", "/spring-mqtt-client.jar"]

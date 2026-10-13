FROM openjdk:8
EXPOSE 8080
ADD target/spring-mqtt-client.jar spring-mqtt-client.jar 
ENTRYPOINT ["java", "-jar", "/spring-mqtt-client.jar"]
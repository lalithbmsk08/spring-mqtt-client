FROM openjdk:8
WORKDIR /app
COPY .target/spring-mqtt-client.jar /app
EXPOSE 8080 
CMD ["java", "-jar", "/spring-mqtt-client.jar"]

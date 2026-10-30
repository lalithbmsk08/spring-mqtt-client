FROM public.ecr.aws/amazoncorretto/amazoncorretto:17

WORKDIR /app

COPY target/*.jar spring-mqtt-client.jar

EXPOSE 8080 

ENTRYPOINT  ["java", "-jar", "spring-mqtt-client.jar"]

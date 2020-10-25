FROM openjdk:8
ADD target/web-service-hotel-api.jar web-service-hotel-api.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "web-service-hotel-api.jar"]
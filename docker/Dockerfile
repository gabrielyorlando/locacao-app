FROM amazoncorretto:21.0.6
WORKDIR /app
COPY build/libs/*.jar app.jar
EXPOSE 9090
ENTRYPOINT ["java", "-jar", "app.jar"]
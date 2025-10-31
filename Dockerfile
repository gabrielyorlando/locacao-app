FROM gradle:8.5-jdk21 AS build
WORKDIR /app

COPY settings.gradle.kts .
COPY build.gradle.kts .
COPY gradlew .
COPY gradle ./gradle

RUN chmod +x gradlew

COPY . .

RUN ./gradlew clean build -x test --no-daemon

FROM amazoncorretto:21.0.6
WORKDIR /app

COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 9090
ENTRYPOINT ["java", "-jar", "app.jar"]

FROM eclipse-temurin:26-jdk AS build
WORKDIR /app
COPY . .
RUN chmod +x gradlew && ./gradlew build -x test

FROM eclipse-temurin:26-jdk
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
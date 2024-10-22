# Use Maven image to build the project
FROM maven:3.9.0-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean install

# Use a lightweight JDK image to run the application
FROM eclipse-temurin:17.0.6_10-jdk
WORKDIR /app
COPY --from=build /app/target/gestion-station-ski-1.0.jar /app/
EXPOSE 8080
CMD ["java", "-jar", "gestion-station-ski-1.0.jar"]
# Stage 1: Build the application
FROM maven:3.8.4-openjdk-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Stage 2: Run the application
FROM eclipse-temurin:17.0.6_10-jdk
WORKDIR /app
COPY --from=build /app/target/gestion-station-ski-1.4.jar /app/
EXPOSE 8080
CMD ["java", "-jar", "gestion-station-ski-1.4.jar"]
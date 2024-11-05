# Use the official Maven image to build the application
FROM maven:3.8.4-openjdk-17 AS build
WORKDIR /app

# Copy the source code into the container
COPY . .

# Build the application and create a JAR file
RUN mvn package -DskipTests

# Use the official OpenJDK image to run the application
FROM openjdk:17-jdk
WORKDIR /app

# Copy the JAR file from the build stage to the runtime image
COPY --from=build /app/target/gestion-station-ski-1.0.jar app.jar

# Expose the port the app will run on (adjust as necessary)
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]

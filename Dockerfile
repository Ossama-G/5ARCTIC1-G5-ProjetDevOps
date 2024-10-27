# Build stage
FROM maven:3.8.7-openjdk-18 AS build
WORKDIR /build

# Copy the pom.xml and download dependencies to speed up subsequent builds
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy the source code and build the application
COPY src ./src
RUN mvn clean package -DskipTests

# Runtime stage
FROM amazoncorretto:17-alpine3.20
WORKDIR /app

# Define build arguments
ARG PROFILE=dev
ARG JAR_FILE=gestion-station-ski.jar
# Default value, overwritten in build

# Copy the JAR file from the build stage
COPY --from=build /build/target/${JAR_FILE} /app/app.jar

# Expose application port
EXPOSE 8089

# Environment variables
ENV DB_URL=jdbc:mysql://mysql-database:3306/badiaa
ENV ACTIVE_PROFILE=${PROFILE}

# Command to run the application
CMD java -jar -Dspring.profiles.active=${ACTIVE_PROFILE} -Dspring.datasource.url=${DB_URL} /app/app.jar

# Use a minimal runtime image for the application
FROM amazoncorretto:17-alpine3.20
WORKDIR /app

# Define build arguments
ARG PROFILE=dev
ARG JAR_FILE=gestion-station-ski-1.1.jar
 # Specify exact filename or pass dynamically

# Copy the downloaded JAR file from the host to the container
COPY ${JAR_FILE} /app/app.jar

# Expose the application port
EXPOSE 8089

# Environment variables

ENV ACTIVE_PROFILE=${PROFILE}
ENV DB_URL=jdbc:mysql://mysql-database2:3306/stationSki
# Command to run the application
CMD ["java", "-jar","-Dspring.datasource.url=${DB_URL}" ,"-Dspring.profiles.active=${ACTIVE_PROFILE}", "/app/app.jar"]

FROM openjdk:17-jdk-alpine

WORKDIR /app

COPY target/gestion-station-ski-1.0.jar /app/gestion-station-ski-1.0.jar

EXPOSE 8099

ENTRYPOINT ["java", "-jar", "gestion-station-ski-1.0.jar"]


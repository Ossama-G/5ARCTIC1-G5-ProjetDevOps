# Utiliser l'image de base OpenJDK 17
FROM openjdk:17-jdk-slim

# Définir le répertoire de travail dans le conteneur
WORKDIR /app

# Copier le fichier JAR généré par Maven dans le conteneur
COPY target/*.jar app.jar

# Exposer le port utilisé par votre application (8089)
EXPOSE 8089

# Démarrer l'application
ENTRYPOINT ["java", "-jar", "app.jar"]

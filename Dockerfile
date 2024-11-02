# Étape 1 : Build
FROM maven:3.8.4-openjdk-17 AS build

# Définir le répertoire de travail pour Maven
WORKDIR /app

# Copier le fichier pom.xml et télécharger les dépendances sans construire
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copier le code source de l'application et construire le JAR
COPY src ./src
RUN mvn package -DskipTests

# Étape 2 : Image d'exécution
FROM openjdk:17-jdk-slim

# Définir le répertoire de travail dans l'image finale
WORKDIR /app

# Copier uniquement le fichier JAR généré depuis l'étape de build
COPY --from=build /app/target/*.jar app.jar

# Exposer le port utilisé par l'application
EXPOSE 8089

# Démarrer l'application
ENTRYPOINT ["java", "-jar", "app.jar"]

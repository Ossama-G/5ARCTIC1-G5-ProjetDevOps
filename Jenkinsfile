pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                git branch: 'Ahmed-5arctic1-G5', credentialsId: 'GitHub-PAT', url: 'https://github.com/Ossama-G/5ARCTIC1-G5-ProjetDevOps.git'
            }
        }
        stage('Build') {
            steps {
                sh 'mvn clean compile'
            }
        }
        stage('SonarQube ') {
            environment {
                SONAR_LOGIN = 'admin'
                SONAR_PASSWORD = 'abmahmed1099-'
            }
            steps {
                withCredentials([usernamePassword(credentialsId: 'sonar-credentials', usernameVariable: 'SONAR_LOGIN', passwordVariable: 'SONAR_PASSWORD')]) {
                    sh 'mvn sonar:sonar -Dsonar.login=$SONAR_LOGIN -Dsonar.password=$SONAR_PASSWORD'
                }
            }
        }
    }
}
pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                git branch: 'Oussama-5ARCTIC1-G5', credentialsId: 'github-token', url: 'https://github.com/Ossama-G/5ARCTIC1-G5-ProjetDevOps.git'
            }
        }

        stage('Compile') {
            steps {
                sh 'mvn clean compile'
            }
        }

        stage('SonarQube analysis') {
            steps {
                withSonarQubeEnv('sonar-scanner') {
                   sh 'mvn sonar:sonar'
                }
            }
        }

        stage('Quality Gate') {
             steps {
                 script {
                      waitForQualityGate abortPipeline: false, credentialsId: 'sonar-token'
                    }
             }
        }

    post {
        always {
            cleanWs()
        }
    }
}

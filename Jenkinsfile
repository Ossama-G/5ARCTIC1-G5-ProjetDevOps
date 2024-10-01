pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                git branch: 'Oussama-5ARCTIC1-G5', credentialsId: 'Git-Cred', url: 'https://github.com/Ossama-G/5ARCTIC1-G5-ProjetDevOps.git'
            }
        }

        stage('Build') {
            steps {
                sh 'mvn clean install'
            }
        }
    }

    post {
        always {
            cleanWs()
        }
    }
}

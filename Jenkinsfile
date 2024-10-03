pipeline {
    agent any

    tools {
        jdk 'jdk-11'
        maven 'Maven3'
    }

    stages {
        stage("Cleanup Workspace"){
            steps {
                cleanWs()
            }

        }


        stage('Checkout') {
            steps {
                git branch: 'Hassine-5Arctic1-G5', credentialsId: 'Git_jenkins', url: 'https://github.com/Ossama-G/5ARCTIC1-G5-ProjetDevOps.git'
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

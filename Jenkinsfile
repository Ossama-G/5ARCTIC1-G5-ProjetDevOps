pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                git branch: 'Ahmed-5arctic1-G5', credentialsId: 	'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', url: 'https://github.com/Ossama-G/5ARCTIC1-G5-ProjetDevOps.git'
            }
        }
        stage('Build') {
            steps {
                sh 'mvn clean compile'
            }
        }
    }
}

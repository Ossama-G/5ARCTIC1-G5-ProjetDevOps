pipeline {
    agent any

    tools {
        jdk 'Java17'
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

        stage("Build Application"){
            steps {
                sh "mvn clean package"
            }

        }
        stage("Test Application"){
            steps {
                sh "mvn test"
            }

        }
        stage("Sonarqube Analysis") {
            steps {
                script {
                    withSonarQubeEnv(credentialsId: 'jenkins-sonarqube-token') {
                        sh "mvn sonar:sonar"
                    }
                }
            }

        }
        
        stage('Quality Gate') {
            steps {
                script {
                    waitForQualityGate abortPipeline: false
                }
            }
        }
    }

    post {
        always {
            cleanWs()
        }
    }
}

pipeline {
    agent any

    tools {
        jdk 'Java17'
        maven 'Maven3'
    }
    environment {
        DOCKER_IMAGE = 'gestion-station-ski'
        DOCKER_TAG = "latest"

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

         stage('Code Coverage Report') {
            steps {
                sh 'mvn jacoco:report'
            }
        }

//         stage('Build Docker Image') {
//             steps {
//                 script {
//                     sh "docker build -t ${DOCKER_IMAGE}:${DOCKER_TAG} ."
//                 }
//             }
//         }
//         stage('Deploy to Nexus') {
//             steps {
//                 sh 'mvn deploy -Dnexus.login=admin -Dnexus.password=admin'
//             }
//         }

    }

    post {
        always {
            cleanWs()
        }
    }
}

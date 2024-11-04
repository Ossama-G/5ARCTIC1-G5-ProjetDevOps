pipeline {
    agent any

    tools {
        jdk 'Java17'
        maven 'Maven3'
    }
    environment {
        APP_NAME = "devopsproject-production-e2e-pipeline"
        RELEASE = "1.0.0"
        DOCKER_USER = "47746"
        DOCKER_PASS = 'dockerhub'
        IMAGE_NAME = "${DOCKER_USER}/${APP_NAME}"
        IMAGE_TAG = "latest"
        JENKINS_API_TOKEN = credentials("JENKINS_API_TOKEN")
        EMAIL_RECIPIENTS = 'ahm.hssin@gmail.com'
        EMAIL_RECIPIENTS_FAILURES = 'ahm.hssin@gmail.com'
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
                sh "mvn clean compile"
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
                    withSonarQubeEnv(credentialsId: 'sql-token') {
                        sh "mvn sonar:sonar"
                    }
                }
            }

        }


        stage('Code Coverage Report') {
            steps {
                sh 'mvn jacoco:report'
            }
        }
        stage('Deploy to Nexus') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'nexus-credentials', usernameVariable: 'NEXUS_USER', passwordVariable: 'NEXUS_PASS')]) {
                    sh 'mvn deploy -Dnexus.login=$NEXUS_USER -Dnexus.password=$NEXUS_PASS'
                }
            }
        }

        stage("Build Docker Image") {
            steps {
                script {
                    docker_image = docker.build "${IMAGE_NAME}"
                }
            }
        }

        stage("Trivy Scan") {
            steps {
                script {
                    sh "trivy image --exit-code 1 --severity HIGH,CRITICAL ${IMAGE_NAME}:${IMAGE_TAG}"
                }
            }
        }

        stage("Push Docker Image") {
            steps {
                script {
                    docker.withRegistry('', DOCKER_PASS) {
                        docker_image.push("${IMAGE_TAG}")
                        docker_image.push('latest')
                    }
                }
            }
        }
        // stage("Deploy with Docker Compose") {
        //     steps {
        //         sh 'docker-compose down'  
        //         sh 'docker-compose up -d'  
        //     }
        // }

        // stage("Deploy to Kubernetes") {
        //     steps {
        //         script {
        //             withKubeConfig([credentialsId: 'kubernetes-config']) {
        //                 sh 'kubectl apply -f kubernetes-deployment-manifest.yml'
        //                 sh 'kubectl rollout restart deployment devopsproject-app'
        //             }
        //         }
        //     }
        // }   
        stage("Trigger CD Pipeline") {
            steps {
                script {
                    sh "curl -v -k --user hssin:${JENKINS_API_TOKEN} -X POST -H 'cache-control: no-cache' -H 'content-type: application/x-www-form-urlencoded' --data 'IMAGE_TAG=${IMAGE_TAG}' 'http://localhost:8080/job/gitops-complete-pipeline/buildWithParameters?token=gitops-token'"
                }
            }
        }

    }

    post {
        failure {
            emailext subject: "Build Failed: ${JOB_NAME} ${BUILD_NUMBER}",
                    body: "Hi, Your build has failed. Please check your build at ${BUILD_URL}.",
                    to: "ahm.hssin@gmail.com",
                    from: "ahm.hssin@gmail.com"
        }
        success {
            emailext subject: "Build Successful: ${JOB_NAME} ${BUILD_NUMBER}",
                    body: "Hi, Your build was successful. Please check your build for details at ${BUILD_URL}.",
                    to: "ahm.hssin@gmail.com",
                    from: "ahm.hssin@gmail.com"
        }
        always {
            cleanWs()
        }
    }
}

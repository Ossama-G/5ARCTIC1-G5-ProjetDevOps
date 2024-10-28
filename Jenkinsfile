pipeline {
    agent any

    environment {
        DOCKERHUB_USERNAME = 'abm026'
    }

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
        stage('Test') {
            steps {
                sh 'mvn test'
            }
        }
        stage('SonarQube') {
            steps {
                withCredentials([string(credentialsId: 'sonar-token', variable: 'SONAR_TOKEN')]) {
                    sh 'mvn sonar:sonar -Dsonar.token=$SONAR_TOKEN'
                }
            }
        }
        stage('JaCoCo Report') {
            steps {
                sh 'mvn jacoco:report'
            }
        }
        stage('Deploy to Nexus') {
            steps {
                catchError(buildResult: 'SUCCESS', stageResult: 'UNSTABLE') {
                    withCredentials([usernamePassword(credentialsId: 'nexus-credentials', usernameVariable: 'NEXUS_USERNAME', passwordVariable: 'NEXUS_PASSWORD')]) {
                        sh 'mvn deploy -DskipTests -Dnexus.username=$NEXUS_USERNAME -Dnexus.password=$NEXUS_PASSWORD'
                    }
                }
            }
        }
        stage('Build Docker Image') {
            steps {
                script {
                    dockerImage = docker.build("${env.DOCKERHUB_USERNAME}/gestion-station-ski:latest")
                }
            }
        }
        stage('Push Docker Image') {
            steps {
                script {
                    docker.withRegistry('https://index.docker.io/v1/', 'dockerhub-credentials') {
                        dockerImage.push()
                    }
                }
            }
        }
        stage('Docker Compose') {
            steps {
                dir('/home/ahmedbm') {
                    sh 'docker-compose down'
                    sh 'docker-compose up -d'
                }
            }
        }
        stage('Monitoring') {
            steps {
                script {
                    // Health check for the Spring Boot application
                    def response = sh(script: "curl -s -o /dev/null -w '%{http_code}' http://localhost:8089/actuator/health", returnStdout: true).trim()
                    echo "Health check response: ${response}"
                    if (response != '200') {
                        error "Health check failed with status code ${response}"
                    }
                }
                dir('/home/ahmedbm') {
                    // Fetch the last 100 lines of logs for monitoring
                    sh 'docker-compose logs --tail=100'
                }
            }
        }
    }
    post {
        always {
            junit '**/target/surefire-reports/*.xml'
            jacoco execPattern: '**/target/jacoco.exec', classPattern: '**/classes', sourcePattern: '**/src/main/java', exclusionPattern: '**/src/test*'
            // Remove the docker-compose down command to keep the containers running
        }
         success {
                   emailext(
                       subject: "Jenkins Build Successful: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                       body: "Good news! The build was successful.\n\nJob: ${env.JOB_NAME}\nBuild Number: ${env.BUILD_NUMBER}\n\nCheck the details at: ${env.BUILD_URL}",
                       to: 'ahmed.belhajmohamed@esprit.tn',
                       from: 'ahmed.belhajmohamed@esprit.tn'
                   )
               }
               failure {
                   emailext(
                       subject: "Jenkins Build Failed: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                       body: "Unfortunately, the build failed.\n\nJob: ${env.JOB_NAME}\nBuild Number: ${env.BUILD_NUMBER}\n\nCheck the details at: ${env.BUILD_URL}",
                       to: 'ahmed.belhajmohamed@esprit.tn',
                       from: 'ahmed.belhajmohamed@esprit.tn'
                   )
               }
           }
       }
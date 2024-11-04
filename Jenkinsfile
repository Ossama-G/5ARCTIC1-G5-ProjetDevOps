pipeline {
    agent any

    environment {
        DOCKERHUB_USERNAME = 'abm026'
        IMAGE_NAME = "gestion-station-ski"
        IMAGE_TAG = "v1.0-dev-${env.BUILD_NUMBER}-${env.GIT_COMMIT.take(7)}"
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
        stage('Trivy Scan on Source Code') {
            steps {
                script {
                    sh 'mkdir -p reports'
                    def retries = 3
                    def waitTime = 10 // seconds

                    for (int i = 0; i < retries; i++) {
                        try {
                            sh '[ -f ~/.cache/trivy/db/trivy.db ] || trivy image --download-db-only'
                            sh 'trivy fs --format json -o reports/trivy-fs-report.json --ignore-unfixed --skip-dirs node_modules,venv .'
                            sh 'python3 $WORKSPACE/src/main/resources/templates/json_to_html.py reports/trivy-fs-report.json reports/trivy-fs-report.html'
                            archiveArtifacts artifacts: 'reports/trivy-fs-report.json, reports/trivy-fs-report.html', allowEmptyArchive: true
                            break
                        } catch (Exception e) {
                            if (i < retries - 1) {
                                sleep(waitTime)
                            } else {
                                error "Trivy scan failed after ${retries} attempts"
                            }
                        }
                    }
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
        stage('Build & Tag Docker Image') {
            steps {
                script {
                    sh "docker build -t ${env.IMAGE_NAME}:${env.IMAGE_TAG} ."
                }
            }
        }
        stage('Push Docker Image to DockerHub') {
            steps {
                script {
                    withCredentials([usernamePassword(credentialsId: 'dockerhub-cred', usernameVariable: 'DOCKERHUB_USERNAME', passwordVariable: 'DOCKERHUB_PASSWORD')]) {
                        sh "echo '$DOCKERHUB_PASSWORD' | docker login -u $DOCKERHUB_USERNAME --password-stdin"
                        sh "docker tag ${env.IMAGE_NAME}:${env.IMAGE_TAG} ${DOCKERHUB_USERNAME}/${env.IMAGE_NAME}:${env.IMAGE_TAG}"
                        sh "docker push ${DOCKERHUB_USERNAME}/${env.IMAGE_NAME}:${env.IMAGE_TAG}"
                    }
                }
            }
        }
        stage('Deploy to Kubernetes') {
            steps {
                script {
                    // Verify connection to the cluster
                    echo 'Verifying connection to the Kubernetes cluster...'
                    sh 'kubectl get nodes'

                    // Deploy MySQL and Spring Boot applications
                    echo 'Deploying MySQL and Spring Boot applications...'
                    sh 'kubectl apply -f k8s/mysql-deployment.yaml'
                    sh 'kubectl apply -f k8s/gestion-station-ski-deployment.yaml'
                }
            }
        }
    }

    post {
        always {
            junit '**/target/surefire-reports/*.xml'
            jacoco execPattern: '**/target/jacoco.exec', classPattern: '**/classes', sourcePattern: '**/src/main/java', exclusionPattern: '**/src/test*'
        }
        success {
            script {
                def emoji = '✅'
                def pipelineStatus = 'SUCCESS'
                def gradientColor = 'linear-gradient(135deg, #b8e994, #28a745)'

                def body = """
                <html>
                    <body style="font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; color: #333; padding: 0; margin: 0;">
                        <div style="margin: 20px; padding: 20px; border-radius: 10px; background: ${gradientColor}; box-shadow: 0 4px 12px rgba(0,0,0,0.1); text-align: center;">
                            <h1 style="color: white; margin: 0; font-size: 2em;">${emoji} ${env.JOB_NAME} - Build ${env.BUILD_NUMBER}</h1>
                            <p style="color: white; margin: 10px 0; font-size: 1.2em; padding: 10px; border-radius: 5px; background-color: rgba(0,0,0,0.2); display: inline-block;">
                                Pipeline Status: <strong>${pipelineStatus}</strong>
                            </p>
                            <p style="margin: 20px 0; font-size: 1.1em;">
                                Check the <a href="${env.BUILD_URL}" style="color: #ffffff; text-decoration: underline; font-weight: bold;">console output</a>.
                            </p>
                        </div>
                    </body>
                </html>
                """

                try {
                    emailext (
                        subject: "${env.JOB_NAME} - Build ${env.BUILD_NUMBER} - ${pipelineStatus}",
                        body: body,
                        to: 'belhajmed.ahmed99@gmail.com',
                        from: 'abmahmed1099@gmail.com',
                        replyTo: 'abmahmed1099@gmail.com',
                        mimeType: 'text/html',
                        attachmentsPattern: 'reports/trivy-fs-report.html'
                    )
                } catch (Exception e) {
                    echo "Error sending email: ${e.getMessage()}"
                }
            }
            publishHTML(target: [
                reportName: 'Trivy Vulnerability Code Source Report',
                reportDir: 'reports',
                reportFiles: 'trivy-fs-report.html',
                alwaysLinkToLastBuild: true,
                keepAll: false,
                allowMissing: false
            ])
        }
        failure {
            script {
                def emoji = '❌'
                def pipelineStatus = 'FAILURE'
                def gradientColor = 'linear-gradient(135deg, #e57373, #dc3545)'

                def body = """
                <html>
                    <body style="font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; color: #333; padding: 0; margin: 0;">
                        <div style="margin: 20px; padding: 20px; border-radius: 10px; background: ${gradientColor}; box-shadow: 0 4px 12px rgba(0,0,0,0.1); text-align: center;">
                            <h1 style="color: white; margin: 0; font-size: 2em;">${emoji} ${env.JOB_NAME} - Build ${env.BUILD_NUMBER}</h1>
                            <p style="color: white; margin: 10px 0; font-size: 1.2em; padding: 10px; border-radius: 5px; background-color: rgba(0,0,0,0.2); display: inline-block;">
                                Pipeline Status: <strong>${pipelineStatus}</strong>
                            </p>
                            <p style="margin: 20px 0; font-size: 1.1em;">
                                Check the <a href="${env.BUILD_URL}" style="color: #ffffff; text-decoration: underline; font-weight: bold;">console output</a>.
                            </p>
                        </div>
                    </body>
                </html>
                """

                try {
                    emailext (
                        subject: "${env.JOB_NAME} - Build ${env.BUILD_NUMBER} - ${pipelineStatus}",
                        body: body,
                        to: 'belhajmed.ahmed99@gmail.com',
                        from: 'abmahmed1099@gmail.com',
                        replyTo: 'abmahmed1099@gmail.com',
                        mimeType: 'text/html',
                        attachmentsPattern: 'reports/trivy-fs-report.html'
                    )
                } catch (Exception e) {
                    echo "Error sending email: ${e.getMessage()}"
                }
            }
        }
        cleanup {
            cleanWs()
        }
    }
}
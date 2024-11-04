pipeline {
    agent any

    environment {
        DOCKERHUB_USERNAME = 'abm026'
        registryName = "gestionstationacr"
        registryCredential = "acr-cred"
        registryUrl = "gestionstationacr.azurecr.io"
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
        stage('Push Docker Image to ACR') {
            steps {
                script {
                    withCredentials([usernamePassword(credentialsId: "${registryCredential}", usernameVariable: 'ACR_USERNAME', passwordVariable: 'ACR_PASSWORD')]) {
                        sh "echo '$ACR_PASSWORD' | docker login ${registryUrl} -u $ACR_USERNAME --password-stdin"
                        sh "docker tag ${env.IMAGE_NAME}:${env.IMAGE_TAG} ${registryUrl}/${env.IMAGE_NAME}:${env.IMAGE_TAG}"
                        sh "docker push ${registryUrl}/${env.IMAGE_NAME}:${env.IMAGE_TAG}"
                    }
                }
            }
        }
        stage('Prepare Deployment YAML') {
            steps {
                sh "sed -i 's|\\${IMAGE_TAG}|${env.IMAGE_TAG}|g' k8s/deployments/app-deployment.yaml"
            }
        }
        stage('Deploy to AKS') {
                    steps {
                        script {
                            withCredentials([string(credentialsId: 'k8s-cred', variable: 'KUBECONFIG_CONTENT')]) {
                                // Write the base64 content to a temporary file without interpolation
                                writeFile file: 'kubeconfig.base64', text: KUBECONFIG_CONTENT

                                // Decode the base64 content and write to kubeconfig
                                sh 'base64 --decode kubeconfig.base64 > $WORKSPACE/kubeconfig'
                                sh 'export KUBECONFIG=$WORKSPACE/kubeconfig'

                                // Verify connection to the cluster
                                echo 'Verifying connection to the AKS cluster...'
                                sh 'kubectl get nodes'

                                // Deploy Secrets and ConfigMaps
                                echo 'Deploying Secrets and ConfigMaps...'
                                sh 'kubectl apply -f k8s/secrets/mysql-secret.yaml'
                                sh 'kubectl apply -f k8s/configmaps/app-configmap.yaml'

                                // Deploy Applications (MySQL and Spring Boot)
                                echo 'Deploying MySQL and Spring Boot applications...'
                                sh 'kubectl apply -f k8s/deployments/mysql-deployment.yaml'
                                sh 'kubectl apply -f k8s/deployments/app-deployment.yaml'

                                // Deploy Services
                                echo 'Deploying internal and external services...'
                                sh 'kubectl apply -f k8s/services/mysql-service.yaml'
                                sh 'kubectl apply -f k8s/services/springboot-app.yaml'
                                sh 'kubectl apply -f k8s/services/springboot-service.yaml'  // LoadBalancer

                                // Verify LoadBalancer IP Address
                                echo 'Waiting for LoadBalancer IP address...'
                                retry(5) {
                                    sleep 30
                                    sh 'kubectl get svc springboot-service -o jsonpath="{.status.loadBalancer.ingress[0].ip}"'
                                }
                            }
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
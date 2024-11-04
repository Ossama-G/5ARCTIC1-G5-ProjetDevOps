pipeline {
    agent any
    triggers {
        githubPush()
    }
    environment {
        IMAGE_NAME = "gammoudioussama/skier-app"
        IMAGE_TAG = "v1.0-dev-${env.BUILD_NUMBER}-${env.GIT_COMMIT.take(7)}"

        // Détails ACR
        registryName = "oussamacontainerregistry01"
        registryCredential = "acr-cred"
        registryUrl = "oussamacontainerregistry01.azurecr.io"
    }

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

        stage('Unit Tests') {
            steps {
                sh 'mvn test -Dspring.profiles.active=local'
            }
        }

        stage('Package') {
            steps {
                sh 'mvn package -DskipTests'
            }
        }

        stage('Vulnerability Scan Using Trivy on Source Code') {
            steps {
                script {
                    sh 'mkdir -p reports'
                    sh 'trivy fs --cache-dir ~/.cache/trivy --format json -o reports/trivy-fs-report.json --ignore-unfixed --skip-dirs node_modules,venv .'
                    sh 'python3 $WORKSPACE/src/main/resources/templates/json_to_html.py reports/trivy-fs-report.json reports/trivy-fs-report.html'
                    archiveArtifacts artifacts: 'reports/trivy-fs-report.html', allowEmptyArchive: true
                }
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
                  waitForQualityGate abortPipeline: false
                }
            }
        }

        stage('Code Coverage Report') {
            steps {
                sh 'mvn jacoco:report'
            }
        }

        stage('Artifact Deployment to Nexus') {
            steps {
                withMaven(globalMavenSettingsConfig: 'global-settings', jdk: 'JAVA_HOME', maven: 'M2_HOME') {
                    sh 'mvn deploy -X'
                }
            }
        }

        stage('Build & Tag Docker Image') {
            steps {
                script {
                    // Construire l'image Docker avec les tags IMAGE_TAG et latest
                    sh "docker build -t ${registryUrl}/${env.IMAGE_NAME}:${env.IMAGE_TAG} -t ${registryUrl}/${env.IMAGE_NAME}:latest ."
                }
            }
        }

        stage('Push Docker Image to DockerHub') {
            steps {
                script {
                    withCredentials([usernamePassword(credentialsId: 'docker-cred', usernameVariable: 'DOCKER_USERNAME', passwordVariable: 'DOCKER_PASSWORD')]) {
                        // Authentification auprès de Docker Hub
                        sh 'echo "$DOCKER_PASSWORD" | docker login -u "$DOCKER_USERNAME" --password-stdin'
                        sh "docker push ${env.IMAGE_NAME}:${env.IMAGE_TAG}"
                    }
                }
            }
        }

        stage('Push Docker Image to Nexus') {
            steps {
                script {
                    withCredentials([usernamePassword(credentialsId: 'nexus-cred', usernameVariable: 'NEXUS_USERNAME', passwordVariable: 'NEXUS_PASSWORD')]) {
                        // Connexion explicite à Nexus
                        sh 'echo "$NEXUS_PASSWORD" | docker login -u "$NEXUS_USERNAME" --password-stdin http://localhost:8082'
                        // Pousser l'image vers Nexus
                        sh "docker tag ${env.IMAGE_NAME}:${env.IMAGE_TAG} localhost:8082/docker-images/${env.IMAGE_NAME}:${env.IMAGE_TAG}"
                        sh "docker push localhost:8082/docker-images/${env.IMAGE_NAME}:${env.IMAGE_TAG}"
                    }
                }
            }
        }

        stage('Push Docker Image to ACR') {
            steps {
                script {
                    withCredentials([usernamePassword(credentialsId: "${registryCredential}", usernameVariable: 'ACR_USERNAME', passwordVariable: 'ACR_PASSWORD')]) {
                        // Authentification auprès de ACR
                        sh "echo '$ACR_PASSWORD' | docker login ${registryUrl} -u $ACR_USERNAME --password-stdin"
                        // Push des tags de version et 'latest'
                        sh "docker push ${registryUrl}/${env.IMAGE_NAME}:${env.IMAGE_TAG}"
                        sh "docker push ${registryUrl}/${env.IMAGE_NAME}:latest"
                    }
                }
            }
        }

        stage('Deploy to AKS') {
            steps {
                script {
                    withCredentials([file(credentialsId: 'k8s-cred', variable: 'KUBECONFIG')]) {
                        // Vérification de la connexion au cluster
                        echo 'Vérification de la connexion au cluster AKS...'
                        sh 'kubectl get nodes'

                        // Déploiement des Secrets et ConfigMaps
                        echo 'Déploiement des Secrets et ConfigMaps...'
                        sh 'kubectl apply -f k8s/secrets/mysql-secret.yaml'
                        sh 'kubectl apply -f k8s/configmaps/app-configmap.yaml'

                        // Déploiement des Applications (MySQL et Spring Boot)
                        echo 'Déploiement des applications MySQL et Spring Boot...'
                        sh 'kubectl apply -f k8s/deployments/mysql-deployment.yaml'
                        sh 'kubectl apply -f k8s/deployments/app-deployment.yaml'

                        // Déploiement des Services
                        echo 'Déploiement des services internes et externes...'
                        sh 'kubectl apply -f k8s/services/mysql-service.yaml'
                        sh 'kubectl apply -f k8s/services/app-service.yaml'
                        sh 'kubectl apply -f k8s/services/springboot-service.yaml'  // LoadBalancer

                        // Vérification de l'Adresse IP du LoadBalancer
                        echo 'Attente pour l\'obtention de l\'adresse IP du LoadBalancer...'
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

                emailext (
                    subject: "${env.JOB_NAME} - Build ${env.BUILD_NUMBER} - ${pipelineStatus}",
                    body: body,
                    to: 'ossama.gammoudii@gmail.com',
                    from: 'ossama.gammoudii@gmail.com',
                    replyTo: 'ossama.gammoudii@gmail.com',
                    mimeType: 'text/html',
                    attachmentsPattern: 'reports/trivy-fs-report.html'
                )
            }
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

                emailext (
                    subject: "${env.JOB_NAME} - Build ${env.BUILD_NUMBER} - ${pipelineStatus}",
                    body: body,
                    to: 'ossama.gammoudii@gmail.com',
                    from: 'ossama.gammoudii@gmail.com',
                    replyTo: 'ossama.gammoudii@gmail.com',
                    mimeType: 'text/html',
                    attachmentsPattern: 'reports/trivy-fs-report.html'
                )
            }
        }

        cleanup {
            cleanWs()
        }
    }
}

pipeline {
    agent any
    triggers {
        githubPush()
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
                sh 'mvn test'
            }
        }

        stage('Vulnerability Scan Using Trivy') {
            steps {
                script {
                    // Créer le dossier pour les rapports une seule fois au début
                    sh 'mkdir -p reports'

                    // Télécharger la base de données de vulnérabilités seulement si elle n'est pas présente
                    sh '[ -f ~/.cache/trivy/db/trivy.db ] || trivy image --download-db-only'

                    // Scanner uniquement les dossiers pertinents, en excluant certains si nécessaire
                    sh 'trivy fs --format json -o reports/trivy-fs-report.json --ignore-unfixed --skip-dirs node_modules,venv .'

                    // Générer un rapport HTML à partir du JSON
                    sh 'python3 $WORKSPACE/src/main/resources/templates/json_to_html.py reports/trivy-fs-report.json reports/trivy-fs-report.html'

                    // Archiver les rapports JSON et HTML pour une meilleure traçabilité
                    archiveArtifacts artifacts: 'reports/trivy-fs-report.json, reports/trivy-fs-report.html', allowEmptyArchive: true
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

                try {
                    emailext (
                        subject: "${env.JOB_NAME} - Build ${env.BUILD_NUMBER} - ${pipelineStatus}",
                        body: body,
                        to: 'ossama.gammoudii@gmail.com',
                        from: 'ossama.gammoudii@gmail.com',
                        replyTo: 'ossama.gammoudii@gmail.com',
                        mimeType: 'text/html',
                        attachmentsPattern: 'reports/trivy-fs-report.html'
                    )
                } catch (Exception e) {
                    echo "Error sending email: ${e.getMessage()}"
                }
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

                try {
                    emailext (
                        subject: "${env.JOB_NAME} - Build ${env.BUILD_NUMBER} - ${pipelineStatus}",
                        body: body,
                        to: 'ossama.gammoudii@gmail.com',
                        from: 'ossama.gammoudii@gmail.com',
                        replyTo: 'ossama.gammoudii@gmail.com',
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

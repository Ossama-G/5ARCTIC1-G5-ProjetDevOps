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
                // Télécharge la base de données de vulnérabilités
                script {
                    sh 'trivy image --download-db-only'
                }

                sh 'trivy fs --format json -o trivy-fs-report.json .'

                sh 'trivy fs --format template --template "./templates/html.tpl" -o trivy-fs-report.html .'

                archiveArtifacts artifacts: 'trivy-fs-report.json, trivy-fs-report.html', allowEmptyArchive: true
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
        always {
            cleanWs()
        }
        success {
            publishHTML(target: [
                reportName: 'Trivy Vulnerability Report',
                reportDir: '',
                reportFiles: 'trivy-fs-report.html',
                alwaysLinkToLastBuild: true,
                keepAll: true,
                allowMissing: true
            ])
            recordIssues tools: [openTasks()]
        }
    }
}

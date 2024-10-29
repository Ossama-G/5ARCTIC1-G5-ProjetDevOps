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

        stage('Verify Template File') {
            steps {
                // Vérifier la présence du fichier template
                sh 'ls -l $WORKSPACE/src/main/resources/templates/'
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
                    // Télécharger la base de données de vulnérabilités
                    sh 'trivy image --download-db-only'
                }

                // Créer le dossier pour les rapports
                sh 'mkdir -p reports'

                // Générer le rapport HTML à partir de Trivy en utilisant un template
                sh 'trivy fs --format template --template /var/lib/jenkins/workspace/Oussama-Pipeline/src/main/resources/templates/html.tpl -o reports/trivy-fs-report.html . || echo "Trivy command failed"'

                // Afficher le rapport pour debug
                sh 'cat reports/trivy-fs-report.html'

                // Archiver le rapport HTML généré
                archiveArtifacts artifacts: 'reports/trivy-fs-report.html', allowEmptyArchive: true
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
            publishHTML(target: [
                reportName: 'Trivy Vulnerability Code Source Report',
                reportDir: 'reports',
                reportFiles: 'trivy-fs-report.html',
                alwaysLinkToLastBuild: true,
                keepAll: false,
                allowMissing: false
            ])
        }

        cleanup {
            cleanWs()
        }
    }
}

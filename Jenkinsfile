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
                   // Met à jour la base de données de vulnérabilités de Trivy
                   sh 'trivy image --download-db-only'
               }

               // Scan le système de fichiers et génère un rapport JSON
               sh 'trivy fs --format json -o trivy-fs-report.json .'

               // Utilise un template pour générer un rapport HTML basé sur le JSON
               sh 'trivy fs --format template --template "./templates/html.tpl" -o trivy-fs-report.html .'
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
    }
}

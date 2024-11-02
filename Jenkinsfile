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
        stage('Trivy Scan') {
            steps {
                script {
                    // Download the vulnerability database
                    sh 'trivy image --download-db-only'

                    // Create the reports directory
                    sh 'mkdir -p reports'

                    // Generate the JSON report
                    sh 'trivy fs --format json -o reports/trivy-fs-report.json .'

                    // Generate the HTML report using a Python script
                    sh 'python3 $WORKSPACE/src/main/resources/templates/json_to_html.py reports/trivy-fs-report.json reports/trivy-fs-report.html'

                    // Archive the HTML report
                    archiveArtifacts artifacts: 'reports/trivy-fs-report.html', allowEmptyArchive: true
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
                    sh 'docker-compose down --remove-orphans'
                    sh 'docker-compose up -d'
                }
            }
        }
        stage('Monitoring') {
            steps {
                script {
                    // Add a delay to allow the application to start
                    sleep(time: 30, unit: 'SECONDS')

                    // Check if Prometheus is running and follow redirects
                    def prometheusResponse = sh(script: "curl -L -s -o /dev/null -w '%{http_code}' http://localhost:9090", returnStdout: true).trim()
                    echo "Prometheus response: ${prometheusResponse}"
                    if (prometheusResponse != '200') {
                        error "Prometheus check failed with status code ${prometheusResponse}"
                    }

                    // Check if Grafana is running and follow redirects
                    def grafanaResponse = sh(script: "curl -L -s -o /dev/null -w '%{http_code}' http://localhost:3000", returnStdout: true).trim()
                    echo "Grafana response: ${grafanaResponse}"
                    if (grafanaResponse != '200') {
                        error "Grafana check failed with status code ${grafanaResponse}"
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
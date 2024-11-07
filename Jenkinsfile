pipeline {
    agent any

        environment {
            // Nexus credentials
            NEXUS_CREDENTIALS = credentials('nexus-credentials')
            DOCKER_CREDENTIALS = credentials('docker-credentials')
            registry = "rezguimedamine/gestion-station-ski"
            RELEASE_VERSION = "1.0"
            dockerImage = ''
            IMAGE_TAG = "${RELEASE_VERSION}-${env.BUILD_NUMBER}"
        }
    stages {
            stage('Checkout') {
                steps {
                    git branch: 'aminerezgui-5arctic5',
                    url: 'https://github.com/malieo1/5ARCTIC5-GestionSkieur.git';
                }
            }
            stage('Clean') {
                        steps {
                            echo 'Cleaning the workspace...'
                            sh 'mvn clean'
                        }
                    }
           stage('Package') {
                               steps {
                                   echo 'Packaging the application...'
                                   sh 'mvn package -DskipTests'
                               }
                           }
               stage('Build') {
                           steps {
                               echo 'Building the project...'
                               sh 'mvn install -Dmaven.test.skip=true'
                           }
                       }
            stage('Test') {
                        steps {
                            sh 'mvn test'
                         }
                     }

        stage('MVN Sonarqube') {
            steps {
                withCredentials([string(credentialsId: 'SonarQube', variable: 'SONAR_TOKEN')]) {
                    sh """
                        mvn clean verify sonar:sonar \
                        -Dsonar.projectKey=YourProjectKey \
                        -Dsonar.host.url=http://your-sonarqube-server-url \
                        -Dsonar.login=${SONAR_TOKEN} \
                        -Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml
                    """
                }
            }
        }


            stage('Deploy to Nexus') {
                        steps {
                            withCredentials([usernamePassword(credentialsId: 'nexus-credentials', usernameVariable: 'NEXUS_USERNAME', passwordVariable: 'NEXUS_PASSWORD')]) {
                                sh "mvn deploy -Dmaven.test.skip=true "
                            }
                        }
                    }


                    stage('Building our image') {
                        steps {
                            script {
                                // Use sh to build the Docker image
                                sh "docker build -t ${registry}:${IMAGE_TAG} ."
                            }
                        }
                    }

                     stage('Push to DockerHub') {
                         steps {
                             script {
                                 withCredentials([usernamePassword(credentialsId: 'docker-credentials', usernameVariable: 'DOCKER_USERNAME', passwordVariable: 'DOCKER_PASSWORD')]) {
                                     sh "echo $DOCKER_PASSWORD | docker login -u $DOCKER_USERNAME --password-stdin"
                                     sh "docker push ${registry}:${IMAGE_TAG}"
                                 }
                             }
                         }
                     }


                     stage('Deploy with Docker Compose') {
                                 steps {
                                     script {
                                         // Stop existing containers
                                         sh 'docker-compose down || true'

                                         // Start the applications
                                         sh 'docker-compose up -d'


                                         sh 'sleep 30'

                                         // Verify deployment
                                         sh 'docker-compose ps'
                                     }
                                 }
                             }

                       stage('Troubleshoot Deployment') {
                                   steps {
                                       script {

                                           sh 'docker-compose logs'


                                       }
                                   }
                               }







                 }




post {
    success {
        echo 'Build finished successfully!'
        mail to: 'darkamin22@gmail.com',
             subject: "Jenkins Job Successful: ${env.JOB_NAME} [${env.BUILD_NUMBER}]",
             body: "Good news Si amin! The job ${env.JOB_NAME} [${env.BUILD_NUMBER}] has finished successfully."
    }
    failure {
        echo 'Build failed!'
        mail to: 'darkamin22@gmail.com',
             subject: "Jenkins Job Failed: ${env.JOB_NAME} [${env.BUILD_NUMBER}]",
             body: "Sorry Si amin, the job ${env.JOB_NAME} [${env.BUILD_NUMBER}] has failed. Please check the Jenkins console output for details."
    }
}


}
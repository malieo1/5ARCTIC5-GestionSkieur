pipeline {
    agent any

        environment {
            // Nexus credentials
            NEXUS_CREDENTIALS = credentials('nexus-credentials')
            DOCKER_CREDENTIALS = credentials('docker-credentials')
            DOCKER_IMAGE = 'rezguimedamine/gestion-station-ski:1.0'

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
                                   sh 'mvn package'
                               }
                           }
               stage('Build') {
                           steps {
                               echo 'Building the project...'
                               sh 'mvn install -Dmaven.test.skip=true'
                           }
                       }
         /*  stage('Test') {
                        steps {
                            sh 'mvn test'
                         }
                     }*/

            stage('MVN Sonarqube') {
                               steps {
                                   withCredentials([string(credentialsId: 'SonarQube', variable: 'SONAR_TOKEN')]) {
                                       sh "mvn sonar:sonar -Dsonar.login=squ_be80c3f2f2118c43ca72c88e151369352f4f4a3c"
                                   }
                               }
                           }

             stage('Deploy to Nexus') {
                        steps {
                            withCredentials([usernamePassword(credentialsId: 'nexus-credentials', usernameVariable: 'NEXUS_USERNAME', passwordVariable: 'NEXUS_PASSWORD')]) {
                                sh "mvn deploy -DskipTests"
                            }
                        }
                    }

             stage('Build Docker Image') {
                         steps {
                             script {
                                 sh "docker build -t $DOCKER_IMAGE ."
                             }
                         }
                     }

                     stage('Push to DockerHub') {
                         steps {
                             script {
                                 withCredentials([usernamePassword(credentialsId: 'docker-credentials', usernameVariable: 'DOCKER_USERNAME', passwordVariable: 'DOCKER_PASSWORD')]) {
                                     sh "echo $DOCKER_PASSWORD | docker login -u $DOCKER_USERNAME --password-stdin"
                                     sh "docker push $DOCKER_IMAGE"
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
                                           // Get logs from all services
                                           sh 'docker-compose logs'

                                           // Optionally, open a shell in the Spring app container for interactive troubleshooting
                                           // Uncomment the next line if you want to drop into a shell
                                           // sh 'docker-compose exec spring_app /bin/sh'
                                       }
                                   }
                               }







                 }





                 post {
                     success {
                         echo 'Build finished successfully!'
                     }
                     failure {
                         echo 'Build failed!'
                     }
                 }


}
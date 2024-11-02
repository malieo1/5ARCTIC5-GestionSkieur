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
             stage('Build') {
                  steps {
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


                     stage('Docker Compose Down') {
                                 steps {
                                     script {
                                         sh 'docker compose down || true'  // Use "|| true" to avoid failure if no containers are running
                                     }
                                 }
                             }

                             stage('Docker Compose Up') {
                                 steps {
                                     script {
                                         sh 'docker compose up -d'
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
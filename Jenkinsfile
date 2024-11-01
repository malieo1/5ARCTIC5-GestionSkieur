pipeline {
    agent any

        environment {
            // Nexus credentials
            NEXUS_CREDENTIALS = credentials('nexus-credentials')

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
           stage('Test') {
                        steps {
                            sh 'mvn test'
                         }
                     }

            stage('MVN Sonarqube') {
                               steps {
                                   withCredentials([string(credentialsId: 'SonarQube', variable: 'SONAR_TOKEN')]) {
                                       sh "mvn sonar:sonar -Dsonar.login=squ_be80c3f2f2118c43ca72c88e151369352f4f4a3c"
                                   }
                               }
                           }

             stage('Deploy to Nexus') {
                        steps {
                            withCredentials([usernamePassword(credentialsId: 'nexus-credentials', usernameVariable: 'admin', passwordVariable: 'admin')]) {
                                sh "mvn deploy -DskipTests"
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
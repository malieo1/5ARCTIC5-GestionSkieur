pipeline {
    agent an
    stages {
            stage('Checkout') {
                steps {
                    git 'https://github.com/malieo1/5ARCTIC5-GestionSkieur.git'
                }
            }
             stage('Build') {
                  steps {
                      sh 'mvn clean install'
                  }
             }
             stage('Test') {
                         steps {
                             sh 'mvn test'
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
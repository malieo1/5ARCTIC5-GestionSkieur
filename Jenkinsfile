pipeline {
    agent any
    stages {
            stage('Checkout') {
                steps {
                    git branch: 'master',
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
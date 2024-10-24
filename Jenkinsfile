pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                git branch: 'malekzahmoul-5arctic5', url: 'https://github.com/malieo1/5ARCTIC5-GestionSkieur.git'
            }
        }

        stage('Build') {
            steps {
                sh 'mvn install -Dmaven.test.skip=true'
            }
        }
        stage('MVN Sonarqube') {
                    steps {
                        withCredentials([string(credentialsId: 'sonar-token', variable: 'SONAR_TOKEN')]) {
                            sh "mvn sonar:sonar -Dsonar.login=squ_73be9fe867b8e74888536bad650f5603593ff0dd"
                        }
                    }
                }

        // Commented out test stage for now

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

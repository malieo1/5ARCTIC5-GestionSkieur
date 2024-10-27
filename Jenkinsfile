pipeline {
    agent any
    stages {
        stage('Checkout') {
            steps {
                git branch: 'farahdiouani-5arctic5',
                url: 'https://github.com/malieo1/5ARCTIC5-GestionSkieur.git'
            }
        }

        stage('Clean') {
            steps {
                sh 'mvn clean'
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

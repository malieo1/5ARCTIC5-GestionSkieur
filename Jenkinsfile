pipeline {
    agent any

    environment {
            // Specify the SonarQube environment variable name as configured in Jenkins
            SONARQUBE_ENV = 'SonarQube'  // Replace with the actual name if different
        }

    stages {
        stage('Checkout') {
            steps {
                echo 'Checking out code...'
                git branch: 'farahdiouani-5arctic5',
                url: 'https://github.com/malieo1/5ARCTIC5-GestionSkieur.git'
            }
        }

        stage('Clean') {
            steps {
                echo 'Cleaning the workspace...'
                sh 'mvn clean'
            }
        }

        stage('Build') {
            steps {
                echo 'Building the project...'
                sh 'mvn install -Dmaven.test.skip=true'
            }
        }

        stage('SonarQube Analysis') {
                    steps {
                        echo 'Running SonarQube analysis...'
                        // Set the SonarQube environment variable configured in Jenkins
                        withSonarQubeEnv(SONARQUBE_ENV) {
                            // Adjust project key and other properties based on your project
                            sh 'mvn sonar:sonar -Dsonar.projectKey=my_project_key'
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

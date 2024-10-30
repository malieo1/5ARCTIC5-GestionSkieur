pipeline {
    agent any

    environment {
        SONARQUBE_ENV = 'SonarQube'  // Replace with your SonarQube environment name
        NEXUS_CREDENTIALS_ID = 'deploymentRepo'  // Nexus credentials ID in Jenkins

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
                withSonarQubeEnv(SONARQUBE_ENV) {
                    sh 'mvn sonar:sonar -Dsonar.projectKey=pipeline-1'
                }
            }
        }

         stage('Deploy to Nexus') {
                    steps {
                        withCredentials([usernamePassword(credentialsId: NEXUS_CREDENTIALS_ID, usernameVariable: 'NEXUS_USERNAME', passwordVariable: 'NEXUS_PASSWORD')]) {
                            sh '''
                                mvn deploy -Dmaven.test.skip=true \
                                  -DaltDeploymentRepository=nexus::default::http://localhost:8081/repository/maven-releases \
                                  -Dnexus.username=admin \
                                  -Dnexus.password=nexus
                            '''
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

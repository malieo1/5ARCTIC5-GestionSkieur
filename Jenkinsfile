pipeline {
    agent any
    environment {
        // Nexus credentials
        NEXUS_CREDENTIALS = credentials('nexus-admin-credentials')
        RELEASE_VERSION = "1.0"
        registry = "malekzahmoul20971/gestion-station-ski"
        registryCredential = 'docker-hub-credentials'
        dockerImage = ''
    }

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

        stage('SonarQube Analysis') {
            steps {
                withCredentials([string(credentialsId: 'sonar-token', variable: 'SONAR_TOKEN')]) {
                    sh '''
                        mvn sonar:sonar -Dsonar.login=$SONAR_TOKEN
                    '''
                }
            }
        }

        stage('Test') {
            steps {
                sh 'mvn test'
            }
        }

        stage('Deploy to Nexus') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'nexus-admin-credentials', usernameVariable: 'NEXUS_USERNAME', passwordVariable: 'NEXUS_PASSWORD')]) {
                    sh "mvn deploy -DskipTests"
                }
            }
        }

        stage('Building our image') {
            steps {
                script {
                    dockerImage = docker.build "${registry}:${RELEASE_VERSION}"

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

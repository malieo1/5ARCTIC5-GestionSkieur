pipeline {
    agent any
       environment {
            // Reference the Nexus credentials
            NEXUS_CREDENTIALS = credentials('nexus-admin-credentials')
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
         stage('Deploy to Nexus') {
                   steps {
                       withCredentials([usernamePassword(credentialsId: 'nexus-admin-credentials', usernameVariable: 'NEXUS_USERNAME', passwordVariable: 'NEXUS_PASSWORD')]) {
                           sh "mvn deploy -DskipTests"
                       }
                   }
               }

        stage('Docker Build') {
            steps {
                script {
                    docker.build("gestion-station-ski:latest", "--build-arg NEXUS_USERNAME=${env.NEXUS_USERNAME} --build-arg NEXUS_PASSWORD=${env.NEXUS_PASSWORD} .")
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

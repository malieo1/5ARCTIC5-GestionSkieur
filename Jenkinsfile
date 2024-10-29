pipeline {
    agent any
    environment {
        // Nexus credentials
        NEXUS_CREDENTIALS = credentials('nexus-admin-credentials')
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'malekzahmoul-5arctic5', url: 'https://github.com/malieo1/5ARCTIC5-GestionSkieur.git'
            }
        }

        stage('Set Version') {
            steps {
                sh 'mvn build-helper:parse-version versions:set -DnewVersion=1.0.${BUILD_NUMBER} -DgenerateBackupPoms=false'
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
                    sh "mvn sonar:sonar -Dsonar.login=${env.SONAR_TOKEN}"
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

        stage('Docker Build') {
            steps {
                script {
                    def version = readMavenPom().getVersion()
                    docker.build("gestion-station-ski:${version}", "--build-arg NEXUS_USERNAME=${env.NEXUS_USERNAME} --build-arg NEXUS_PASSWORD=${env.NEXUS_PASSWORD} .")
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

pipeline {
    agent any
    environment {
        NEXUS_VERSION = "nexus3"
        NEXUS_PROTOCOL = "http"
        NEXUS_URL = "192.168.50.4:8081"
        NEXUS_REPOSITORY = "maven-releases"
        NEXUS_CREDENTIAL_ID = "admin" // Ensure this is set up in Jenkins credentials
    }



    stages {
        stage('Git') {
            steps {
                echo 'Recup Code de Git:'
                git branch: 'khalilbelhedi-5arctic5',
                    url: 'https://github.com/malieo1/5ARCTIC5-GestionSkieur.git'
            }
        }

        stage('Maven Clean') {
            steps {
                echo 'Nettoyage du Projet:'
                sh 'mvn clean package'
            }
        }

        stage('Maven Compile') {
            steps {
                echo 'Construction du Projet:'
                sh 'mvn compile'
            }
        }

        stage('Test') {
            steps {
                echo 'Execution des Tests:'
                sh 'mvn test'
            }
        }

        stage('Remove Old Docker Image') {
            steps {
                script {
                    sh 'docker rmi -f khalilbelhedi336/skiback:latest || true' // `-f` forces removal, and `|| true` ignores errors
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    sh 'docker build -t khalilbelhedi336/skiback .'
                }
            }
        }

        stage('Docker Compose Down') {
            steps {
                dir('firstpipeline') {
                    sh 'docker compose down'
                }
            }
        }

        stage('Deploy with Docker Compose') {
            steps {
                dir('firstpipeline') {
                    sh 'docker compose up -d'
                }
            }
        }

        // stage('Final Docker Compose Down') { // Only include if shutdown is desired at the end
        //     steps {
        //         dir('firstpipeline') {
        //             sh 'docker compose down'
        //         }
        //     }
        // }
    }
}

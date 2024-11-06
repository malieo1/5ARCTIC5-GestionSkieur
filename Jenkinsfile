pipeline {
    agent any
    environment {
        NEXUS_VERSION = "nexus3"
        NEXUS_PROTOCOL = "http"
        NEXUS_URL = "192.168.50.4:8081"
        NEXUS_REPOSITORY = "maven-releases"
        NEXUS_CREDENTIAL_ID = "admin"
    }

    stages {
        stage('Git') {
            steps {
                echo 'Fetching Code from Git:'
                git branch: 'khalilbelhedi-5arctic5',
                    url: 'https://github.com/malieo1/5ARCTIC5-GestionSkieur.git'
            }
        }

        stage('Maven Clean and Package') {
            steps {
                script {
                    echo 'Cleaning and Building Project:'
                    def commitId = sh(script: 'git rev-parse --short HEAD', returnStdout: true).trim()
                    env.COMMIT_ID = commitId

                    // Build the jar with the commit ID as part of the version
                    sh "mvn clean package -Drevision=${commitId}"
                    sh 'ls target'

                }
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    sh "docker build -t khalilbelhedi336/skiback:${env.COMMIT_ID} ."
                }
            }
        }
     stage('Deploy to Nexus') {
                             steps {
                             script {
                                     sh "mvn deploy -Dmaven.test.skip=true "
                                 }
                             }
                         }


        stage('Deploy with Docker Compose') {
            steps {
                dir('firstpipeline') {
                    sh 'docker compose down'
                    sh "IMAGE_TAG=${env.COMMIT_ID} docker compose up -d"
                }
            }
        }
    }
}

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
        stage('Deploy to Nexus') {
                        steps {
                                sh "mvn deploy -Dmaven.test.skip=true "
                            }
                        }

        stage('Build Docker Image') {
            steps {
                script {
                    // Utiliser l'ID de commit comme tag pour l'image
                    def commitId = sh(script: 'git rev-parse --short HEAD', returnStdout: true).trim()
                    sh "docker build -t khalilbelhedi336/skiback:${commitId} ."
                    env.IMAGE_TAG = commitId // Stocke l'ID de commit comme tag d'image
                }
            }
        }
      stage('Scan Docker Image with Trivy') {
                steps {
                    script {
                        // Run Trivy to scan the Docker image
                        echo 'Scanning Docker image with Trivy:'
                        sh "sh "trivy image --cache-dir /path/to/cache --exit-code 1 --no-progress khalilbelhedi336/skiback:${env.IMAGE_TAG}"
"
                    }
                }
            }
        stage('Deploy with Docker Compose') {
            steps {
                dir('firstpipeline') {
                    sh 'docker compose down'
                    sh "IMAGE_TAG=${env.IMAGE_TAG} docker compose up -d"
                }
            }
        }
    }
}

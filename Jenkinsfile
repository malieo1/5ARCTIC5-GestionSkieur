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
           stage('SonarQube') {
                    steps {
                        echo 'Analyse de la Qualité du Code : '
                        sh 'mvn sonar:sonar -Dsonar.login=admin -Dsonar.password=Admin@dmin123'
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

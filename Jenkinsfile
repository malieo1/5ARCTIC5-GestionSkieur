pipeline {
    agent any
    environment {
        NEXUS_VERSION = "nexus3"
        NEXUS_PROTOCOL = "http"
        NEXUS_URL = "192.168.50.4:8081"
        NEXUS_REPOSITORY = "maven-releases"
        NEXUS_CREDENTIAL_ID = "admin"
        DOCKER_CREDENTIALS = credentials('docker-hub-credentials')

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
         stage('Login to Docker') {
                            steps {
                                echo 'Logging to DockerHub...'
                                script {
                                    withCredentials([usernamePassword(credentialsId: 'docker-hub-credentials', usernameVariable: 'DOCKER_USERNAME', passwordVariable: 'DOCKER_PASSWORD')]) {
                                        sh "docker login -u $DOCKER_USERNAME -p $DOCKER_PASSWORD"
                                        echo 'DockerHub login successful.'
                                    }
                                }
                                echo 'Login to DockerHub stage completed.'
                            }
                        }
        stage('push to dockerhub') {
                                    steps {
                                            sh "docker push khalilbelhedi336/skiback:${IMAGE_TAG}"
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
        stage('Pull Docker Image') {
                    steps {
                        sshagent(['k8s-target-ssh']) {
                            // Pull de l'image Docker sur la VM cible
                            sh 'ssh -o StrictHostKeyChecking=no production@192.168.133.130 "docker pull khalilbelhedi336/skiback:${IMAGE_TAG}"'
                        }
                    }
                }

                stage('Deploy to Kubernetes') {
                    steps {
                        sshagent(['k8s-target-ssh']) {
                            // Mise à jour de l'image dans le déploiement Kubernetes
                            sh 'ssh -o StrictHostKeyChecking=no production@192.168.133.130 "kubectl set image deployment/spring-boot-app app-container=khalilbelhedi336/skiback:${IMAGE_TAG}"'
                        }
                    }
                }
    }
}

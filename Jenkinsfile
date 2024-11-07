pipeline {
    agent any

    //ngrok
    triggers {
        githubPush()
    }

    environment {
        NEXUS_VERSION = "nexus3"
        NEXUS_PROTOCOL = "http"
        NEXUS_URL = "192.168.50.4:8081"
        NEXUS_REPOSITORY = "maven-releases"
        DOCKER_CREDENTIALS = credentials('docker-hub-credentials')
    }

    stages {
        stage('Git') {
            steps {
                echo 'Fetching Code from Git:'
                git branch: 'khalilbelhedi-5arctic5',
                    url: 'https://github.com/malieo1/5ARCTIC5-GestionSkieur.git'
            }
        }

        stage('Maven Clean') {
            steps {
                echo 'Cleaning the Project:'
                sh 'mvn clean package'
            }
        }

        stage('Maven Compile') {
            steps {
                echo 'Building the Project:'
                sh 'mvn compile'
            }
        }

        stage('Test') {
            steps {
                echo 'Running Tests:'
                sh 'mvn test'
            }
        }

        stage('SonarQube') {
            steps {
                echo 'Code Quality Analysis:'
                withCredentials([usernamePassword(credentialsId: 'sonar-credentials', usernameVariable: 'SONAR_USER', passwordVariable: 'SONAR_PASSWORD')]) {
                    sh "mvn sonar:sonar -Dsonar.login=$SONAR_USER -Dsonar.password=$SONAR_PASSWORD"
                }
            }
        }

        stage('Deploy to Nexus') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'nexus-credentials', usernameVariable: 'NEXUS_USER', passwordVariable: 'NEXUS_PASSWORD')]) {
                    sh "mvn deploy -Dmaven.test.skip=true -Dusername=$NEXUS_USER -Dpassword=$NEXUS_PASSWORD"
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    // Using the commit ID as the tag for the image
                    def commitId = sh(script: 'git rev-parse --short HEAD', returnStdout: true).trim()
                    sh "docker build -t khalilbelhedi336/skiback:${commitId} ."
                    env.IMAGE_TAG = commitId // Store the commit ID as the image tag
                }
            }
        }

        stage('Login to Docker') {
            steps {
                echo 'Logging into DockerHub...'
                script {
                    withCredentials([usernamePassword(credentialsId: 'docker-hub-credentials', usernameVariable: 'DOCKER_USERNAME', passwordVariable: 'DOCKER_PASSWORD')]) {
                        sh "docker login -u $DOCKER_USERNAME -p $DOCKER_PASSWORD"
                        echo 'DockerHub login successful.'
                    }
                }
                echo 'DockerHub login completed.'
            }
        }

        stage('Push to DockerHub') {
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

        stage('Start prometheus and grafana') {
            steps {
                sh 'docker start prometheus'
                sh 'docker start grafana'
            }
        }
    }

    post {
        success {
            slackSend channel: '#devops-slack-notifications', color: 'green', message: 'Build success', teamDomain: 'virtiverse', tokenCredentialId: 'slack-token'
        }
        failure {
            slackSend channel: '#devops-slack-notifications', color: 'red', message: 'Build failed', teamDomain: 'virtiverse', tokenCredentialId: 'slack-token'
        }
    }
}

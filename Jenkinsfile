pipeline {
    agent any

    environment {
        SONARQUBE_ENV = 'SonarQube'  // SonarQube environment name
        NEXUS_CREDENTIALS_ID = 'deploymentRepo'  // Nexus credentials ID in Jenkins
        DOCKER_CREDENTIALS = credentials('docker-hub-credentials')
        RELEASE_VERSION = "1.0"
        registry = "farahdiouani/gestion-station-ski"
        registryCredential = 'docker-hub-credentials'
        IMAGE_TAG = "${RELEASE_VERSION}-${env.BUILD_NUMBER}"
    }

    stages {
        stage('Checkout') {
            steps {
                echo 'Checking out code...'
                git branch: 'farahdiouani-5arctic5',
                url: 'https://github.com/malieo1/5ARCTIC5-GestionSkieur.git'
                echo 'Checkout completed.'
            }
        }

        stage('Clean') {
            steps {
                echo 'Cleaning the workspace...'
                sh 'mvn clean'
                echo 'Clean completed.'
            }
        }

        stage('Test') {
            steps {
                echo 'Running unit tests...'
                sh 'mvn test'
                echo 'Tests completed.'
            }
        }

        stage('Package') {
            steps {
                echo 'Packaging Stage...'
                sh 'mvn package'
                echo 'Package completed.'
            }
        }

        stage('Build') {
            steps {
                echo 'Building Stage...'
                sh 'mvn install -Dmaven.test.skip=true'
                echo 'Build completed.'
            }
        }

        stage('SonarQube Analysis') {
            steps {
                echo 'Starting SonarQube Analysis...'
                withSonarQubeEnv(SONARQUBE_ENV) {
                    echo 'Running SonarQube analysis...'
                    sh 'mvn sonar:sonar -Dsonar.projectKey=pipeline-1'
                }
                echo 'SonarQube analysis completed.'
            }
        }

        stage('Deploy to Nexus') {
            steps {
                echo 'Deploying to Nexus...'
                sh "mvn deploy -Dmaven.test.skip=true -DaltDeploymentRepository=deploymentRepo::default::http://192.168.33.10:8081/repository/FarahDiouani-5arctic5/"
                echo 'Deployment to Nexus completed.'
            }
        }

        stage('Build Image') {
            steps {
                echo 'Building Docker Image...'
                script {
                    dockerImage = docker.build("${registry}:${IMAGE_TAG}")
                    echo "Docker image built: ${dockerImage.imageName()}"
                }
                echo 'Docker Build Image stage completed.'
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

        stage('Push to DockerHub') {
            steps {
                echo 'Pushing to DockerHub...'
                script {
                    sh "docker push ${dockerImage.imageName()}"
                    echo "Docker image pushed: ${dockerImage.imageName()}"
                }
                echo 'Push to DockerHub stage completed.'
            }
        }

        stage('Deploy with Docker Compose') {
            steps {
                echo 'Deploying with Docker Compose...'
                script {
                    echo 'Stopping existing containers...'
                    sh 'docker compose down || true'

                    echo 'Starting containers...'
                    sh 'docker compose up -d'

                    echo 'Waiting for services to initialize...'
                    sh 'sleep 30'

                    echo 'Verifying deployment status...'
                    sh 'docker compose ps'
                }
                echo 'Deploy with Docker Compose stage completed.'
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

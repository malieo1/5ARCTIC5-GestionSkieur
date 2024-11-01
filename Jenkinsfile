pipeline {
    agent any
    environment {
        // Nexus credentials
        NEXUS_CREDENTIALS = credentials('nexus-admin-credentials')
        //Docker credentials
        DOCKER_HUB_CREDENTIALS = credentials('docker-hub-credentials')
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

        stage('Docker Build & Push') {
            steps {
                script {
                    // Specify the fixed version directly
                    def version = '1.0'
                    def imageName = "malekzahmoul20971/gestion-station-ski:${version}"

                    // Build the Docker image
                    docker.build(imageName, "--build-arg NEXUS_USERNAME=${env.NEXUS_CREDENTIALS_USR} --build-arg NEXUS_PASSWORD=${env.NEXUS_CREDENTIALS_PSW} .")

                    // Login and push to Docker Hub
                    withCredentials([usernamePassword(credentialsId: 'docker-hub-credentials', usernameVariable: 'DOCKER_USERNAME', passwordVariable: 'DOCKER_PASSWORD')]) {
                        sh "echo $DOCKER_PASSWORD | docker login -u $DOCKER_USERNAME --password-stdin"
                        sh "docker push ${imageName}"
                    }
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

pipeline {
    agent any
    environment {
        SONARQUBE_URL = 'http://192.168.33.10:9000/'
        SONARQUBE_TOKEN = credentials('sonar-token')
        NEXUS_CREDENTIALS_ID = 'nexus'
        NEXUS_URL = 'http://192.168.33.10:8081/repository/maven-releases/'
        DOCKER_CREDENTIALS = credentials('docker-hub-credentials')
    }
    stages {
        stage('Checkout') {
            steps {
                git branch: 'wadhahdaoud-5arctic5', url: 'https://github.com/malieo1/5ARCTIC5-GestionSkieur.git'
            }
        }



        stage('Build') {
            steps {
                sh 'mvn clean install -Dmaven.test.skip=true'
            }
        }

        stage('Test') {
            steps {
                catchError(buildResult: 'SUCCESS', stageResult: 'FAILURE') {
                    sh 'mvn test'
                }
            }
        }


        stage('SonarQube Analysis') {
                    steps {
                        echo 'Running SonarQube analysis...'
                        withCredentials([string(credentialsId: 'sonar-token', variable: 'SONARQUBE_TOKEN')]) {
                            sh """
                                mvn sonar:sonar \
                                -Dsonar.host.url=${SONARQUBE_URL} \
                                -Dsonar.login=${SONARQUBE_TOKEN}
                            """
                        }
                    }
                }



        stage('Deploy to Nexus') {
            steps {
                withCredentials([usernamePassword(credentialsId: NEXUS_CREDENTIALS_ID, usernameVariable: 'NEXUS_USERNAME', passwordVariable: 'NEXUS_PASSWORD')]) {
                    sh """
                        mvn deploy \
                        -DaltDeploymentRepository=deploymentRepo::default::${NEXUS_URL} \
                        -Dnexus.username=${NEXUS_USERNAME} \
                        -Dnexus.password=${NEXUS_PASSWORD}
                    """
                }
            }
        }

        stage('Remove Old Docker Image') {
            steps {
                script {
                    // Supprime l'image Docker existante si elle existe
                    sh 'docker rmi -f wadhahdaoud/skiback:latest || true'
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    // Construction d'une nouvelle image Docker avec le tag spécifié
                    sh 'docker build -t wadhahdaoud/skiback .'
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

                stage('Push to DockerHub') {
                    steps {
                        echo 'Pushing to DockerHub...'
                        script {
                            sh "docker push wadhahdaoud/skiback:latest"
                            echo "Docker image pushed: wadhahdaoud/skiback:latest"
                        }
                        echo 'Push to DockerHub stage completed.'
                    }
                }

        stage('Docker Compose Down') {
            steps {
                dir('firstpipeline') {
                    script {
                        // Arrête et supprime les conteneurs Docker si existants
                        sh 'docker compose down || true'
                    }
                }
            }
        }

        stage('Deploy with Docker Compose') {
            steps {
                dir('firstpipeline') {
                    script {
                        // Déploie les services avec Docker Compose en mode détaché
                        sh 'docker compose up -d'
                    }
                }
            }
        }
    }
        stage('restarting prometheus & grafana') {
                        steps {
                            echo 'Containers restarted :'
                            sh 'docker restart prometheus '
                            sh 'docker restart grafana '
                        }
                    }

    post {
        always {
            echo 'Pipeline terminé. Collecte des logs.'
            // Collecte les rapports de test générés par Maven Surefire pour une analyse dans Jenkins
            junit '**/target/surefire-reports/*.xml'
        }
        success {
            echo 'Build finished successfully!'
        }
        failure {
            echo 'Build or tests failed!'
        }
    }
}

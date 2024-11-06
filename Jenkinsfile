pipeline {
    agent any
    environment {
        NEXUS_VERSION = "nexus3"
        NEXUS_PROTOCOL = "http"
        NEXUS_URL = "192.168.50.4:8081"
        NEXUS_REPOSITORY = "maven-releases"
        NEXUS_CREDENTIAL_ID = "admin" // Set this in Jenkins credentials for Nexus access
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
                    // Get the Git commit ID to use as part of the artifact version
                    def commitId = sh(script: 'git rev-parse --short HEAD', returnStdout: true).trim()

                    // Set the commit ID as an environment variable for use in later stages
                    env.COMMIT_ID = commitId

                    // Use the commit ID to tag the build
                    sh "mvn clean package -Drevision=${commitId}"
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    // Build Docker image with the commit ID as the tag
                    sh "docker build -t khalilbelhedi336/skiback:${env.COMMIT_ID} ."
                }
            }
        }

        stage('Deploy to Nexus') {
            steps {
                script {
                    echo 'Deploying .jar to Nexus Repository:'
                    sh """
                        mvn deploy:deploy-file \
                        -Dfile=target/your-artifact-${env.COMMIT_ID}.jar \
                        -DgroupId=com.example \
                        -DartifactId=your-artifact \
                        -Dversion=1.0-${env.COMMIT_ID} \
                        -Dpackaging=jar \
                        -DrepositoryId=${NEXUS_CREDENTIAL_ID} \
                        -Durl=${NEXUS_PROTOCOL}://${NEXUS_URL}/repository/${NEXUS_REPOSITORY}
                    """
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

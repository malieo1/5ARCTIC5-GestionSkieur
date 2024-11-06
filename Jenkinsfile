pipeline {
    agent any

    stages {
        stage('Git') {
            steps {
                echo 'Récupération du Code de Git :'
                git(
                    branch: 'MohamedYoussefMathlouthi-5arctic5',
                    url: 'https://github.com/malieo1/5ARCTIC5-GestionSkieur.git',
                    credentialsId: 'github_token'
                )
            }
        }
        stage('Maven install') {
            steps {
                echo 'install :'
                sh 'mvn install'
            }
        }
        stage('Maven Clean') {
            steps {
                echo 'Nettoyage du Projet :'
                sh 'mvn clean package'
            }
        }

        stage('Maven Compile') {
            steps {
                echo 'Compilation du Projet :'
                sh 'mvn compile'
            }
        }

        stage('Test') {
            steps {
                echo 'Exécution des tests unitaires :'
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

        stage('Docker Compose Down') {
            steps {
                echo 'Arrêt des services Docker :'
                sh 'docker compose down'
            }
        }


         stage('Remove Old Docker Image') {
                    steps {
                        script {
                            // Supprime l'image Docker existante si elle existe
                            sh 'docker rmi -f youssefmathlouthi/skiback:latest || true'
                        }
                    }
                }
        stage('Build Docker Image') {
                    steps {
                        script {
                            sh 'docker build -t youssefmathlouthi/skiback .'
                        }
                    }
                }
        stage('Docker Compose Up') {
            steps {
                echo 'Démarrage des services avec Docker Compose :'
                sh 'docker compose up -d'
            }
        }
        stage('restarting prometheus & grafana') {
                    steps {
                        echo 'Containers restarted :'
                        sh 'docker restart prometheus '
                        sh 'docker restart grafana '
                    }
                }
    }

}
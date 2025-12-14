pipeline { 
    agent any

    environment {
        DOCKER_REGISTRY = "https://index.docker.io/v1/"
        DOCKER_REPO = "dchou1618/kafka-avg"
        IMAGE_TAG = "${env.BUILD_NUMBER}"
        DOCKER_CREDS = "dockerhub-credentials-id"
    }

    stages {
        stage("Checkout") {
            steps {
                checkout scm
            }
        }
        stage("Build") {
            steps {
                sh "./gradlew clean build"
            }
        }
        stage("Docker Build") {
            steps {
                script {
                    dockerImage = docker.build("${DOCKER_REPO}:${IMAGE_TAG}")
                }
            }
        }
        stage("Docker Push") {
            steps {
                script {
                    docker.withRegistry(DOCKER_REGISTRY, DOCKER_CREDS) {
                        dockerImage.push()
                        dockerImage.push("latest")
                    }
                }
            }
        }
    }
    post {
        always {
            cleanWs()
        }
        success {
            echo "Build and push successful!"
        }
        failure {
            echo "Build failed!"
        }
    }
}
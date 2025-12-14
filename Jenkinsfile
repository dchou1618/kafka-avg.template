pipeline {
  agent none

  environment {
    DOCKER_REGISTRY = "https://index.docker.io/v1/"
    DOCKER_REPO = "dchou1618/kafka-avg"
    IMAGE_TAG = "${env.BUILD_NUMBER}"
    DOCKER_CREDS = "dockerhub-credentials-id"
  }

  stages {
    stage('Checkout') {
      agent any
      steps {
        checkout scm
      }
    }

    stage('Build') {
      agent {
        docker {
          image 'gradle:9.2.1-jdk21'
          args '-v $HOME/.gradle:/home/gradle/.gradle' // optional cache mount
        }
      }
      steps {
        sh './gradlew clean build --no-daemon'
      }
      stash includes: 'app/build/libs/*.jar', name: 'jar'
    }

    stage('Docker Build & Push') {
      agent {
        docker {
          image 'docker:24.0-dind'
          args '-v /var/run/docker.sock:/var/run/docker.sock'
        }
      }
      steps {
        unstash 'jar'

        script {
          dockerImage = docker.build("${DOCKER_REPO}:${IMAGE_TAG}")
          docker.withRegistry(DOCKER_REGISTRY, DOCKER_CREDS) {
            dockerImage.push()
            dockerImage.push('latest')
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
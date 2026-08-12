pipeline {
    agent any
    stages {
        stage('Checkout') {
            steps {
                git url: 'https://github.com/Rajeshluffy/jenkinsLearning.git',
                    branch: 'master'
            }
        }
        stage('Build Docker Image') {
            steps {
                sh 'docker build -t sdet-test:latest .'
            }
        }
        stage('Load Image into Minikube') {
            steps {
                sh 'docker save sdet-test:latest | docker exec -i minikube ctr images import -'
            }
        }
       stage('Deploy to Kubernetes') {
    	    steps {
               sh 'docker exec minikube minikube kubectl -- delete job sdet-test-job --ignore-not-found=true'
                sh 'docker cp k8s/test-job.yaml minikube:/tmp/test-job.yaml'
                sh 'docker exec minikube minikube kubectl -- apply -f /tmp/test-job.yaml'
            }
        }
    }
    post {
        success { echo 'Pipeline complete' }
        failure { echo 'Pipeline failed — check Console Output' }
    }
}
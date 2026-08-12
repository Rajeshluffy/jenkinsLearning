cat > Jenkinsfile << 'EOF'
pipeline {
    agent any
    stages {
        stage('Checkout') {
            steps {
                git credentialsId: 'github-creds',
                    url: 'https://github.com/Rajeshluffy/jenkinsLearning.git',
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
                sh 'docker exec minikube kubectl delete job sdet-test-job --ignore-not-found=true'
                sh 'docker exec minikube kubectl apply -f /app/k8s/test-job.yaml'
            }
        }
    }
    post {
        success { echo 'Pipeline complete' }
        failure { echo 'Pipeline failed — check Console Output' }
    }
}
EOF
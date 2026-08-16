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
                sh 'docker build --platform linux/amd64 --provenance=false -t sdet-test:latest .'
            }
        }
        stage('Load Image into Minikube') {
            steps {
                sh 'docker save sdet-test:latest | docker exec -i minikube ctr --namespace=k8s.io images import -'
            }
        }
        stage('Deploy to Kubernetes') {
            steps {
                sh 'docker exec minikube /var/lib/minikube/binaries/v1.35.1/kubectl --kubeconfig=/etc/kubernetes/admin.conf delete job sdet-test-job --ignore-not-found=true'
                sh 'cat k8s/test-job.yaml | docker exec -i minikube /var/lib/minikube/binaries/v1.35.1/kubectl --kubeconfig=/etc/kubernetes/admin.conf apply -f -'
            }
        }
        stage('Collect Test Results') {
            steps {
                sh '''
                    KUBECTL="docker exec minikube /var/lib/minikube/binaries/v1.35.1/kubectl --kubeconfig=/etc/kubernetes/admin.conf"
                    
                    # Wait up to 5 minutes for the job to complete. If it fails or times out, Jenkins will fail here.
                    $KUBECTL wait --for=condition=complete job/sdet-test-job --timeout=300s
                    
                    mkdir -p target
                    
                    # Copy the reports. If the reports don't exist, Jenkins will fail here.
                    docker cp minikube:/tmp/surefire-reports ./target/surefire-reports
                '''
            }
        }
    }
    post {
        always {
            junit allowEmptyResults: true, testResults: '**/surefire-reports/*.xml'
        }
        success { echo 'Pipeline complete' }
        failure { echo 'Pipeline failed — check Console Output' }
    }
}
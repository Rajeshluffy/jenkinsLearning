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
                sh 'docker build --platform linux/amd64 --provenance=false -t sdet-test:${BUILD_ID} .'
            }
        }
        stage('Load Image into Minikube') {
            steps {
               sh '''
                    docker save -o sdet-test.tar sdet-test:${BUILD_ID}
                    docker cp sdet-test.tar minikube:/sdet-test.tar
                    docker exec minikube docker load -i /sdet-test.tar
                    rm sdet-test.tar
                    docker exec minikube rm /sdet-test.tar
                '''
            }
        }
        stage('Deploy to Kubernetes') {
            steps {
                sh '''
                    docker exec minikube /var/lib/minikube/binaries/v1.35.1/kubectl --kubeconfig=/etc/kubernetes/admin.conf delete job sdet-test-job --ignore-not-found=true
                    sed -i "s/sdet-test:latest/sdet-test:${BUILD_ID}/g" k8s/test-job.yaml
                    cat k8s/test-job.yaml | docker exec -i minikube /var/lib/minikube/binaries/v1.35.1/kubectl --kubeconfig=/etc/kubernetes/admin.conf apply -f -
                '''
            }
        }
        stage('Collect Test Results') {
            steps {
                sh '''
                    KUBECTL="docker exec minikube /var/lib/minikube/binaries/v1.35.1/kubectl --kubeconfig=/etc/kubernetes/admin.conf"
                    
                    # Wait for tests to finish successfully
                    $KUBECTL wait --for=condition=complete job/sdet-test-job --timeout=300s
                    
                    # Create the local target directory
                    mkdir -p target
                    
                    # MAGICAL FIX: Zip the reports inside minikube, stream them out, and unzip into the Jenkins workspace
                    docker exec minikube tar -c -C /tmp surefire-reports | tar -x -C ./target
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
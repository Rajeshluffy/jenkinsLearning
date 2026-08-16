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
                // Dynamically tag the image with the Jenkins build number to avoid K8s caching
                sh 'docker build --platform linux/amd64 --provenance=false -t sdet-test:${BUILD_ID} .'
            }
        }
        stage('Load Image into Minikube') {
            steps {
               sh '''
                    # 1. Save the image to a tar file using the unique Build ID
                    docker save -o sdet-test.tar sdet-test:${BUILD_ID}
                    
                    # 2. Copy the tar file directly to the root directory (/) of Minikube
                    docker cp sdet-test.tar minikube:/sdet-test.tar
                    
                    # 3. Load the image into Minikube's internal Docker daemon (Fix for ErrImageNeverPull)
                    docker exec minikube docker load -i /sdet-test.tar
                    
                    # 4. Clean up the large tar files so they don't eat up disk space
                    rm sdet-test.tar
                    docker exec minikube rm /sdet-test.tar
                '''
            }
        }
        stage('Deploy to Kubernetes') {
            steps {
                sh '''
                    # 1. Delete the old job
                    docker exec minikube /var/lib/minikube/binaries/v1.35.1/kubectl --kubeconfig=/etc/kubernetes/admin.conf delete job sdet-test-job --ignore-not-found=true
                    
                    # 2. MAGICAL FIX: Search and replace "latest" with the actual Build ID in the YAML file
                    sed -i "s/sdet-test:latest/sdet-test:${BUILD_ID}/g" k8s/test-job.yaml
                    
                    # 3. Apply the newly modified YAML file to Kubernetes
                    cat k8s/test-job.yaml | docker exec -i minikube /var/lib/minikube/binaries/v1.35.1/kubectl --kubeconfig=/etc/kubernetes/admin.conf apply -f -
                '''
            }
        }
        stage('Collect Test Results') {
            steps {
                sh '''
                    KUBECTL="docker exec minikube /var/lib/minikube/binaries/v1.35.1/kubectl --kubeconfig=/etc/kubernetes/admin.conf"
                    
                    # Wait up to 5 minutes for the job to complete. If it fails or times out, Jenkins will fail here.
                    $KUBECTL wait --for=condition=complete job/sdet-test-job --timeout=300s
                    
                    mkdir -p target
                    
                    # Copy the reports from the persistent /data folder instead of /tmp
                    docker cp minikube:/data/surefire-reports ./target/surefire-reports
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
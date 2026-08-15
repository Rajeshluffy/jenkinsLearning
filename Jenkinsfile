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
               sh 'docker build --platform linux/amd64 -t sdet-test:latest .'
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
        apiVersion: batch/v1
kind: Job
metadata:
  name: sdet-test-job
spec:
  template:
    spec:
      containers:
      - name: sdet-test
        image: sdet-test:latest
        imagePullPolicy: Never       # 1. Forces K8s to use your loaded local image
        volumeMounts:
        - name: test-reports
          mountPath: /app/target/surefire-reports
      restartPolicy: Never
      volumes:
      - name: test-reports
        hostPath:
          path: /tmp/surefire-reports # 2. Writes files directly to the Minikube node
          type: DirectoryOrCreate
    }
    post {
        always {
            junit allowEmptyResults: true, testResults: '**/surefire-reports/*.xml'
        }
        success { echo 'Pipeline complete' }
        failure { echo 'Pipeline failed — check Console Output' }
    }
}
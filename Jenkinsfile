pipeline {
    agent none
    
    parameters {
        string(name: 'REGISTRY_URL', defaultValue: 'docker.io/${GIT_USERNAME}', description: 'Docker registry URL (e.g., docker.io/user)')
        string(name: 'IMAGE_TAG', defaultValue: "build-${BUILD_NUMBER}", description: 'Image tag')
        string(name: 'KUBE_NAMESPACE', defaultValue: 'default', description: 'Kubernetes namespace')
        choice(name: 'DEPLOY_ENV', choices: ['dev', 'staging', 'prod'], description: 'Deployment environment')
    }
    
    environment {
        DOCKER_CREDENTIALS = credentials('docker-hub-credentials') // ID of Docker Hub creds in Jenkins
        KUBE_CONFIG = credentials('kubeconfig') // K8s kubeconfig secret ID
        SERVICES = ['api-gateway', 'customer_api']
    }
    
    stages {
        stage('Checkout') {
            agent { label 'master' }
            steps {
                checkout scm
            }
        }
        
        stage('Maven Build & Test') {
            parallel {
                stage('api-gateway') {
                    agent {
                        docker {
                            image 'maven:3.9.9-eclipse-temurin-21'
                            args '-v /var/run/docker.sock:/var/run/docker.sock'
                            reuseNode true
                        }
                    }
                    steps {
                        dir('api-gateway') {
                            sh '''
                                ./mvnw clean verify
                            '''
                        }
                    }
                    post {
                        always { junit 'api-gateway/target/surefire-reports/*.xml' }
                    }
                }
                stage('customer_api') {
                    agent {
                        docker {
                            image 'maven:3.9.9-eclipse-temurin-21'
                            args '-v /var/run/docker.sock:/var/run/docker.sock'
                            reuseNode true
                        }
                    }
                    steps {
                        dir('customer_api') {
                            sh '''
                                ./mvnw clean verify
                            '''
                        }
                    }
                    post {
                        always { junit 'customer_api/target/surefire-reports/*.xml' }
                    }
                }
            }
        }
        
        stage('Docker Build & Push') {
            parallel {
                stage('api-gateway') {
                    agent {
                        docker {
                            image 'docker:26.0-dind'
                            args '-v /var/run/docker.sock:/var/run/docker.sock'
                            reuseNode true
                        }
                    }
                    steps {
                        script {
                            docker.withRegistry("https://${params.REGISTRY_URL}", 'docker-hub-credentials') {
                                dir('api-gateway') {
                                    def image = docker.build("${params.REGISTRY_URL}/api-gateway:${params.IMAGE_TAG}")
                                    image.push()
                                    image.push('latest')
                                }
                            }
                        }
                    }
                }
                stage('customer_api') {
                    agent {
                        docker {
                            image 'docker:26.0-dind'
                            args '-v /var/run/docker.sock:/var/run/docker.sock'
                            reuseNode true
                        }
                    }
                    steps {
                        script {
                            docker.withRegistry("https://${params.REGISTRY_URL}", 'docker-hub-credentials') {
                                dir('customer_api') {
                                    def image = docker.build("${params.REGISTRY_URL}/customer-api:${params.IMAGE_TAG}")
                                    image.push()
                                    image.push('latest')
                                }
                            }
                        }
                    }
                }
            }
        }
        
        stage('Deploy to Kubernetes') {
            agent {
                docker {
                    image 'bitnami/kubectl:latest'
                    reuseNode true
                }
            }
            steps {
                withKubeConfig([credentialsId: 'kubeconfig', serverUrl: '']) {
                    sh """
                        sed -i 's|image: .*|image: ${params.REGISTRY_URL}/api-gateway:${params.IMAGE_TAG}|g' k8s/gateway-deployment.yaml
                        sed -i 's|image: .*|image: ${params.REGISTRY_URL}/customer-api:${params.IMAGE_TAG}|g' k8s/customer-deployment.yaml
                        kubectl apply -f k8s/ -n ${params.KUBE_NAMESPACE}
                    """
                }
            }
        }
    }
    
    post {
        always {
            sh 'docker system prune -f'
            cleanWs()
        }
        success {
            echo 'Pipeline completed successfully!'
        }
        failure {
            echo 'Pipeline failed!'
        }
    }
}

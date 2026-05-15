pipeline {
    agent any
    
    parameters {
        string(name: 'REGISTRY_URL', defaultValue: 'docker.io/gbenga12', description: 'Docker registry URL')
        string(name: 'IMAGE_TAG', defaultValue: "build-${BUILD_NUMBER}", description: 'Image tag')
        string(name: 'KUBE_NAMESPACE', defaultValue: 'student-app', description: 'Kubernetes namespace')
        choice(name: 'DEPLOY_ENV', choices: ['dev', 'staging', 'prod'], description: 'Deployment environment')
    }
    
    environment {
        DOCKER_CREDENTIALS = credentials('docker-hub-credentials')
        SERVICES = "api-gateway customer_api"
        HELM_CHART_DIR = 'helm/student-app'
        HELM_PACKAGE_DIR = 'dist/helm'
        HELM_RELEASE = 'student-app'
    }
    
    stages {

        stage('Checkout') {
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
                        }
                    }
                    steps {
                        dir('api-gateway') {
                            sh './mvnw clean verify'
                        }
                    }
                    post {
                        always {
                            junit 'api-gateway/target/surefire-reports/*.xml'
                        }
                    }
                }

                stage('customer_api') {
                    agent {
                        docker {
                            image 'maven:3.9.9-eclipse-temurin-21'
                            args '-v /var/run/docker.sock:/var/run/docker.sock'
                        }
                    }
                    steps {
                        dir('customer_api') {
                            sh './mvnw clean verify'
                        }
                    }
                    post {
                        always {
                            junit 'customer_api/target/surefire-reports/*.xml'
                        }
                    }
                }
            }
        }

        stage('Docker Build & Push') {
            parallel {

                stage('api-gateway') {
                    steps {
                        script {
                            docker.withRegistry("https://${params.REGISTRY_URL}", 'docker-hub-credentials') {
                                dir('api-gateway') {
                                    def image = docker.build("${params.REGISTRY_URL}/api-gateway:${params.IMAGE_TAG}")
                                    image.push()
                                    image.push("latest")
                                }
                            }
                        }
                    }
                }

                stage('customer_api') {
                    steps {
                        script {
                            docker.withRegistry("https://${params.REGISTRY_URL}", 'docker-hub-credentials') {
                                dir('customer_api') {
                                    def image = docker.build("${params.REGISTRY_URL}/customer-api:${params.IMAGE_TAG}")
                                    image.push()
                                    image.push("latest")
                                }
                            }
                        }
                    }
                }
            }
        }

        stage('Helm Lint & Package') {
            agent {
                docker {
                    image 'alpine/helm:3.15.4'
                    args '-v $WORKSPACE:$WORKSPACE -w $WORKSPACE'
                }
            }
            steps {
                sh """
                    mkdir -p ${HELM_PACKAGE_DIR}
                    helm lint ${HELM_CHART_DIR}
                    helm package ${HELM_CHART_DIR} --destination ${HELM_PACKAGE_DIR}
                """
            }
            post {
                success {
                    archiveArtifacts artifacts: "${HELM_PACKAGE_DIR}/*.tgz", fingerprint: true
                }
            }
        }

        stage('Deploy to Kubernetes with Helm') {
            agent {
                docker {
                    image 'alpine/helm:3.15.4'
                    args '-v $HOME/.kube:/root/.kube'
                }
            }
            steps {
                withKubeConfig([credentialsId: 'kubeconfig']) {
                    sh """
                        helm upgrade --install ${HELM_RELEASE} ${HELM_CHART_DIR} \
                          --namespace ${params.KUBE_NAMESPACE} \
                          --create-namespace \
                          --wait \
                          --set global.imageRegistry=${params.REGISTRY_URL} \
                          --set global.imageTag=${params.IMAGE_TAG}
                    """
                }
            }
        }
    }

    post {
        always {
            echo 'Cleaning up...'
            sh 'docker image prune -f'
            cleanWs()
        }
        success {
            echo '✅ Pipeline completed successfully!'
        }
        failure {
            echo '❌ Pipeline failed!'
        }
    }
}

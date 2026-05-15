# Jenkins Pipeline Setup Guide

## Overview
Added `Jenkinsfile` at root for CI/CD of **api-gateway** & **customer_api** services:
- Parallel Maven build/test
- Docker build/push to registry
- Helm chart lint/package
- Deploy to Kubernetes with `helm upgrade --install`

## Prerequisites
1. **Jenkins Server** w/ plugins:
   - Docker Pipeline
   - Kubernetes CLI
   - Maven Integration
   - Pipeline
   - GitHub (for webhook)
2. **Nodes/Agents**: Docker-enabled w/ Java21, Docker, kubectl.
   - Helm 3 is provided inside the pipeline container image (`alpine/helm:3.15.4`)
3. **Credentials** (Global or Folder):
   - `docker-hub-credentials`: Docker Hub username/password (Username w/ Password)
   - `kubeconfig`: Kubernetes kubeconfig file (Secret file)

## Usage
1. **Create Pipeline Job**:
   - New Item → Pipeline → SCM: Git repo URL
   - Script: `Jenkinsfile`
2. **Parameters**:
   | Param | Default | Desc |
   |-------|---------|------|
   | REGISTRY_URL | docker.io/${GIT_USERNAME} | Registry (e.g., yourdockerhub/api-gateway) |
   | IMAGE_TAG | build-${BUILD_NUMBER} | Tag (or git short SHA) |
   | KUBE_NAMESPACE | student-app | K8s namespace |
   | DEPLOY_ENV | dev/staging/prod | Env selector |
3. **GitHub Webhook**:
   - Repo Settings → Webhooks → Payload URL: `https://your-jenkins/job/your-pipeline/build`
   - Events: Push, PRs
4. **Run**: Build Now → Set params → Monitor console.

## Customization
- **Registry**: Update Docker Hub creds ID if different.
- **Helm**: Chart lives in `helm/student-app`. The pipeline lints and packages it to `dist/helm/*.tgz`.
- **K8s**: Deployment now uses Helm with `--set global.imageRegistry=... --set global.imageTag=...`.
- **Tests**: Runs `mvn verify` (unit + integration).
- **All Services**: Add parallel stages for others.

## Local Test
```bash
docker run -it --rm \
  -v %CD%:/workspace \
  -v /var/run/docker.sock:/var/run/docker.sock \
  -p 8080:8080 \
  jenkins/jenkins:lts-jdk21 \
  jenkins-cli -s http://localhost:8080/ build your-pipeline
```
(Setup Jenkins first.)

## Troubleshooting
- Maven fails: Check Java21 agent.
- Docker push: Verify creds/permissions.
- Helm deploy: Check kubeconfig/cluster access and confirm the target namespace exists or can be created.

Jenkinsfile ready for production use!

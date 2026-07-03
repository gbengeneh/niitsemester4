# AWS Complete Deployment Roadmap

This guide turns the work already done in this repository into a full teaching roadmap for AWS deployment.

It is designed for you and your students to move in this order:

1. Understand the local architecture already built in this repo
2. Create and secure an AWS account
3. Prepare networking and access
4. Connect with MobaXterm
5. Deploy on EC2 with Docker Compose first
6. Add monitoring and observability
7. Add CI/CD with Jenkins
8. Evolve to Kubernetes and Helm on AWS

## 1. What We Already Have Locally

This repository already contains a strong progression from development to deployment:

- `config-server/`: Spring Cloud Config server
- `eureka-server/`: Service discovery
- `api-gateway/`: API Gateway
- `customer_api/`: Customer service with PostgreSQL
- `bankingapi/`: Banking service with MySQL
- `docker-compose.yml`: root orchestration for shared infrastructure
- `customer_api/docker-compose.yml`: Customer API + PostgreSQL
- `bankingapi/docker-compose.yml`: Banking API + MySQL + Zipkin
- `k8s/`: raw Kubernetes manifests
- `helm/student-app/`: Helm chart for Kubernetes deployment
- `monitoring/`: Prometheus, Grafana, Loki, Promtail, Jaeger
- `Jenkinsfile`: pipeline for build, push, package, and Helm deployment

## 2. Recommended Teaching Path

For students, do not start with EKS first.

Use this teaching progression:

### Phase 1. Local machine
- Run the services locally
- Understand service discovery, gateway routing, and databases
- Test health endpoints and API flows

### Phase 2. AWS EC2 deployment
- Deploy the same application to one Linux server
- Use Docker and Docker Compose
- Connect with MobaXterm
- Learn security groups, key pairs, and server administration

### Phase 3. Monitoring and operations
- Add Prometheus, Grafana, Jaeger, Loki, and CloudWatch
- Learn logs, metrics, tracing, alerts, and health checks

### Phase 4. CI/CD
- Build Docker images automatically
- Push to Docker Hub or Amazon ECR
- Deploy from Jenkins

### Phase 5. Kubernetes on AWS
- Move to EKS only after students understand EC2 deployment
- Reuse the `k8s/` manifests and `helm/student-app/` chart

## 3. Architecture Summary

Current logical architecture:

```text
Users
  |
  v
API Gateway
  |
  +--> Customer API --> PostgreSQL
  |
  +--> Banking API  --> MySQL

Supporting services:
- Eureka Server
- Config Server
- Zipkin or Jaeger
- Prometheus
- Grafana
- Loki + Promtail
```

Important ports already used in this project:

| Component | Port |
|---|---:|
| API Gateway | 8080 |
| Eureka Server | 8761 |
| Config Server | 8888 |
| Customer API | 7074 |
| Banking API | 7075 |
| PostgreSQL | 5432 |
| MySQL | 3306 |
| Zipkin | 9411 |
| Prometheus | 9090 |
| Grafana | 3000 |
| Jaeger UI | 16686 |
| Loki | 3100 |
| Node Exporter | 9100 |

## 4. AWS Account Creation and Initial Security

### Step 1. Create the AWS account
- Create an AWS account with a lab email
- Add a payment method
- Verify the account

### Step 2. Secure the root account immediately
- Enable MFA on the root account
- Do not use the root account for daily work
- Store recovery codes safely

### Step 3. Create an IAM admin user
- Create an IAM user such as `student-admin`
- Grant AdministratorAccess for lab use
- Enable MFA for the IAM user
- Use the IAM user for console and CLI access

### Step 4. Set budget alarms
- Create a monthly budget
- Add email alerts at 50%, 80%, and 100%
- Teach students to stop or terminate resources after class

### Step 5. Choose a region
- Keep all services in one region
- Example: `eu-west-1`, `us-east-1`, or `eu-central-1`
- Use the same region for EC2, ECR, CloudWatch, and EKS

## 5. AWS Foundation Setup

### Networking
- Use the default VPC for simple labs, or create a custom VPC for advanced classes
- Create at least one public subnet for EC2 labs
- Attach an internet gateway
- Ensure route tables allow internet access

### Security groups

Create one security group for the application server and open only the ports needed.

Suggested inbound rules for the EC2 lab:

| Port | Purpose | Source |
|---|---|---|
| 22 | SSH/SFTP | Your IP only |
| 8080 | API Gateway | Your IP or class IP range |
| 8761 | Eureka dashboard | Your IP only |
| 8888 | Config server | Your IP only |
| 3000 | Grafana | Your IP only |
| 9090 | Prometheus | Your IP only |
| 16686 | Jaeger UI | Your IP only |

Do not expose database ports publicly unless you are intentionally teaching remote DB administration.

### Key pair
- Create an EC2 key pair
- Download the `.pem` file
- Store it safely
- This key will be used in MobaXterm

## 6. Local Tools Students Should Install

On Windows student machines:

- MobaXterm
- Git
- Docker Desktop
- Java 21
- Maven
- AWS CLI
- kubectl
- Helm

Optional:

- Postman
- VS Code

## 7. MobaXterm Connection Guide

MobaXterm is useful because it gives:

- SSH terminal access
- SFTP file transfer
- easy key management
- tabbed sessions for multiple servers

### Connect to EC2 with MobaXterm

1. Launch MobaXterm
2. Click `Session`
3. Choose `SSH`
4. Remote host: paste the EC2 public IP or public DNS
5. Check `Specify username`
6. Use username:
   - `ubuntu` for Ubuntu AMIs
   - `ec2-user` for Amazon Linux
7. Go to `Advanced SSH settings`
8. Select your downloaded `.pem` private key
9. Save the session with a readable name such as `semester4-ec2`
10. Connect

### If the connection fails
- Confirm the EC2 instance is running
- Confirm the public IP is correct
- Confirm port `22` is open in the security group
- Confirm the `.pem` file matches that instance
- Confirm the correct Linux username is used

## 8. Recommended AWS Deployment Path for This Project

For this repository, the best first AWS deployment target is:

### Option A. Single EC2 instance with Docker Compose

This is the best teaching choice because it matches your current repo most closely.

Students will learn:

- Linux server setup
- Docker image deployment
- environment variables
- networking
- logs and monitoring
- restart and recovery

### Option B. EKS with Helm

This is the advanced path after EC2.

Use it only after students are comfortable with:

- containers
- registry workflows
- health checks
- namespaces
- services
- ingress
- Helm upgrades

## 9. EC2 Deployment Roadmap

### Step 1. Launch the EC2 instance

Recommended baseline for a classroom demo:

- OS: Ubuntu 22.04 LTS
- Instance type: `t3.medium` or `t3.large`
- Storage: 20 to 30 GB
- Public IP: enabled
- Security group: custom lab group

Why `t3.medium`:

- enough memory for multiple containers
- cheaper than larger instances
- more realistic than a very small instance

### Step 2. Connect with MobaXterm

After login, verify the machine:

```bash
whoami
hostname
uname -a
```

### Step 3. Update packages

```bash
sudo apt update
sudo apt upgrade -y
```

### Step 4. Install Docker and Compose plugin

```bash
sudo apt install -y ca-certificates curl gnupg lsb-release
sudo install -m 0755 -d /etc/apt/keyrings
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo gpg --dearmor -o /etc/apt/keyrings/docker.gpg
echo \
  "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.gpg] https://download.docker.com/linux/ubuntu \
  $(. /etc/os-release && echo $VERSION_CODENAME) stable" | \
  sudo tee /etc/apt/sources.list.d/docker.list > /dev/null
sudo apt update
sudo apt install -y docker-ce docker-ce-cli containerd.io docker-buildx-plugin docker-compose-plugin
sudo usermod -aG docker $USER
newgrp docker
docker --version
docker compose version
```

### Step 5. Install Git

```bash
sudo apt install -y git
git --version
```

### Step 6. Clone the repository

```bash
git clone <your-repository-url>
cd semester4
```

### Step 7. Decide image strategy

Use one of these:

1. Build directly on EC2
2. Build locally and push to Docker Hub
3. Build in Jenkins and push to Docker Hub or ECR

For teaching, start with Docker Hub because your repo already references `gbenga12/...` images and Jenkins is already wired for registry pushes.

## 10. Important AWS Adjustments for This Repo

This repo is close to deployment-ready, but students should understand these AWS migration notes.

### Adjustment 1. `host.docker.internal`

Your local setup uses `host.docker.internal` in some places for Docker Desktop networking.

That is not the cleanest design for Linux EC2.

For AWS EC2, prefer all services on one shared Docker network and use service names instead.

Examples:

- `http://eureka-server:8761/eureka/`
- `jdbc:postgresql://postgres:5432/niitdevcustomerdb`
- `jdbc:mysql://mysql:3306/customerdb`

### Adjustment 2. Compose split across folders

Right now, infrastructure and business services are split across multiple compose files.

That is workable, but for EC2 teaching you may want one combined compose file such as:

- `docker-compose.aws.yml`

This reduces confusion and avoids cross-folder dependency issues.

### Adjustment 3. Kubernetes coverage is partial

Your Helm chart currently packages:

- `api-gateway`
- `customer-api`
- `postgres`

It does not yet fully package:

- `config-server`
- `eureka-server`
- `bankingapi`
- `mysql`
- full monitoring stack

That is fine for a staged learning path. Just make it explicit to students.

### Adjustment 4. Secrets

Some local values are hard-coded in project files.

For AWS, move credentials into:

- `.env` files not committed to Git
- Jenkins credentials
- AWS Systems Manager Parameter Store
- AWS Secrets Manager
- Kubernetes Secrets for EKS

## 11. Suggested EC2 Deployment Sequence

### Phase A. Infrastructure containers

Deploy first:

- Config Server
- Eureka Server
- API Gateway

Verify:

- Config Server health
- Eureka dashboard
- API Gateway health

### Phase B. Business services

Deploy next:

- PostgreSQL
- MySQL
- Customer API
- Banking API

Verify:

- both apps start
- both apps connect to databases
- both apps register with Eureka
- gateway routes traffic correctly

### Phase C. Observability

Deploy:

- Prometheus
- Grafana
- Jaeger
- Loki
- Promtail
- Node Exporter

Verify:

- Grafana dashboards load
- Prometheus can scrape metrics
- traces appear in Jaeger
- logs appear in Loki

## 12. Example Classroom Deployment Checklist

### Before deployment
- AWS account created
- IAM admin user created
- MFA enabled
- budget alarm configured
- EC2 key pair created
- MobaXterm installed
- security group configured

### During deployment
- EC2 launched
- SSH works
- Docker installed
- repo cloned
- images built or pulled
- containers started

### After deployment
- health endpoints return `UP`
- Eureka shows service registration
- API Gateway routes to services
- databases contain expected records
- Grafana dashboards load
- application logs are visible

## 13. Monitoring Roadmap

Use two layers of monitoring:

### Layer 1. Application-level monitoring inside the project

Already present or partially prepared in this repo:

- Prometheus
- Grafana
- Jaeger
- Loki
- Promtail
- Node Exporter

Teach students to observe:

- CPU and memory
- JVM health
- request count and latency
- database availability
- traces across services
- logs per service

### Layer 2. AWS-native monitoring

Add:

- CloudWatch metrics
- CloudWatch Logs
- CloudWatch alarms
- EC2 status checks
- SNS email notifications

Suggested alarms:

- CPU utilization too high
- memory too high
- disk space low
- service container stopped
- application health endpoint failing

## 14. CI/CD Roadmap Using What Is Already Here

Your repo already includes a Jenkins pipeline that does the following:

- checks out code
- runs Maven tests in parallel
- builds Docker images
- pushes images to a registry
- lints and packages the Helm chart
- deploys with Helm

### Recommended teaching sequence for CI/CD

1. Run Jenkins locally or on a dedicated EC2 instance
2. Connect Jenkins to GitHub
3. Add Docker Hub credentials or ECR credentials
4. Add kubeconfig for Kubernetes labs
5. Trigger builds on push
6. Review build logs and test reports

### Registry options

Use one of:

1. Docker Hub
2. Amazon ECR

For beginner labs:

- start with Docker Hub

For more AWS-native labs:

- migrate to ECR later

## 15. Kubernetes and Helm on AWS

When students are ready, move to EKS.

### Reuse from this repo
- `k8s/` raw manifests
- `helm/student-app/` chart
- `Jenkinsfile` Helm deployment stage

### Typical EKS rollout path

1. Create EKS cluster
2. Install `kubectl`
3. Configure kubeconfig
4. Create namespace
5. Deploy chart with Helm
6. Add AWS Load Balancer Controller or NGINX Ingress
7. Expose the API Gateway
8. Add persistent storage for databases
9. Move secrets into Kubernetes Secrets or AWS Secrets Manager

### Important note

At the moment, your Helm chart does not represent the entire microservice platform. It is a good starting point, not the final production chart.

## 16. Suggested Student Lab Plan

### Lab 1. Local architecture
- run services locally
- identify all ports
- test service registration and routing

### Lab 2. AWS account and IAM
- create account
- enable MFA
- create IAM admin
- create budget alarm

### Lab 3. EC2 and MobaXterm
- launch EC2
- connect using SSH key
- install Docker

### Lab 4. First deployment
- clone repo
- start containers
- verify health endpoints

### Lab 5. Monitoring
- deploy Grafana and Prometheus
- create a simple dashboard
- inspect traces and logs

### Lab 6. CI/CD
- configure Jenkins
- push a code change
- observe automated build and deployment

### Lab 7. Kubernetes
- deploy the Helm chart
- explain services, pods, configmaps, secrets, and ingress

## 17. Suggested Final Architecture Evolution

### Stage 1. Beginner
- one EC2 instance
- Docker Compose
- manual deployment

### Stage 2. Intermediate
- one app EC2 instance
- one Jenkins EC2 instance
- Docker Hub or ECR
- monitoring enabled

### Stage 3. Advanced
- EKS
- Helm
- ingress
- CloudWatch + Prometheus + Grafana
- secrets management
- automated CI/CD

## 18. Recommended Deliverables for Students

Each student or group should submit:

1. architecture diagram
2. AWS screenshots
3. security group configuration
4. MobaXterm connection proof
5. running container list
6. health endpoint results
7. Grafana dashboard screenshot
8. deployment notes and troubleshooting log

## 19. Quick Commands Reference

### Docker

```bash
docker ps
docker images
docker logs -f api-gateway
docker logs -f customer-api
docker logs -f bankingapi
docker compose up -d
docker compose down
```

### Health checks

```bash
curl http://localhost:8080/actuator/health
curl http://localhost:7074/actuator/health
curl http://localhost:7075/actuator/health
curl http://localhost:8761
```

### Kubernetes

```bash
kubectl get pods -n student-app
kubectl get svc -n student-app
helm list -n student-app
helm upgrade --install student-app helm/student-app --namespace student-app --create-namespace
```

## 20. Best-Practice Notes for Teaching

- Start simple and visible before going fully cloud-native
- Keep the first AWS deployment on one EC2 instance
- Introduce monitoring before introducing autoscaling
- Introduce CI/CD before introducing EKS
- Use EKS as the capstone, not the first lab
- Treat secrets management as part of deployment, not an afterthought

## 21. Final Recommendation

For this repository, the clearest end-to-end classroom roadmap is:

1. Local Docker and service discovery
2. AWS account, IAM, budget, key pairs
3. EC2 deployment with MobaXterm
4. Monitoring stack deployment
5. Jenkins CI/CD
6. Kubernetes and Helm on AWS

That path matches what you have already built here and gives students a smooth progression from fundamentals to production-style deployment.

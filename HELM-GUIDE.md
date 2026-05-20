# Helm and Ingress Guide for `student-app`

## Overview

This project includes a Helm chart for packaging and deploying part of the microservices stack to Kubernetes.

The chart lives in:

```text
helm/student-app
```

It currently manages:

- `api-gateway`
- `customer-api`
- `postgres`
- optional `Ingress` for `api-gateway`

Helm helps us avoid editing raw Kubernetes YAML by hand for every deployment. Instead, we define templates once, keep configurable settings in `values.yaml`, and then install or upgrade the application with Helm commands.

## What Helm Does in This Project

In this repository, Helm is used to:

- package Kubernetes resources into one reusable chart
- keep deployment settings in one place
- upgrade image tags without editing manifests manually
- support Jenkins CI/CD deployment
- optionally expose the API Gateway through Ingress

## Chart Structure

```text
helm/student-app/
├── Chart.yaml
├── values.yaml
├── .helmignore
└── templates/
    ├── _helpers.tpl
    ├── api-gateway-deployment.yaml
    ├── api-gateway-ingress.yaml
    ├── api-gateway-service.yaml
    ├── customer-configmap.yaml
    ├── customer-deployment.yaml
    ├── customer-service.yaml
    ├── postgres-deployment.yaml
    ├── postgres-pvc.yaml
    ├── postgres-secret.yaml
    └── postgres-service.yaml
```

## What Each File Does

- `Chart.yaml`: chart name, version, and metadata
- `values.yaml`: default values used by all templates
- `templates/`: actual Kubernetes resources rendered by Helm
- `_helpers.tpl`: reusable helpers for labels, names, and image references
- `api-gateway-ingress.yaml`: optional ingress resource for public access

## Why Helm Is Useful Here

Helm gives this project:

- repeatable deployments
- easier upgrades
- reusable configuration
- safer automation in Jenkins
- optional ingress without maintaining another raw YAML file

## Important Values

The most important settings are in [helm/student-app/values.yaml](/c:/Users/USER/Desktop/niit/semester4/helm/student-app/values.yaml).

### Global image values

```yaml
global:
  imageRegistry: docker.io/gbenga12
  imageTag: latest
```

These values are used for:

- `api-gateway`
- `customer-api`

### API Gateway service values

```yaml
apiGateway:
  service:
    type: NodePort
    port: 80
    targetPort: 8085
    nodePort: 30007
```

### API Gateway ingress values

```yaml
apiGateway:
  ingress:
    enabled: false
    className: nginx
    annotations: {}
    host: student-app.local
    path: /
    pathType: Prefix
    tls:
      enabled: false
      secretName: ""
```

### Customer API values

The chart includes values for:

- replica count
- resources
- liveness probe
- readiness probe
- environment variables
- ConfigMap settings

### Postgres values

The chart includes values for:

- database name
- username
- password
- PVC size
- resources

## How Helm Works

### Step 1. Helm reads the chart

Helm reads:

- `Chart.yaml`
- `values.yaml`
- everything in `templates/`

### Step 2. Helm injects values into templates

Template expressions like:

```yaml
{{ .Values.global.imageTag }}
```

are replaced with actual values.

### Step 3. Helm renders final Kubernetes YAML

You can preview that output with:

```bash
helm template student-app helm/student-app --namespace student-app
```

### Step 4. Helm installs or upgrades the release

Helm sends the rendered resources to Kubernetes.

- If the release does not exist, Helm installs it.
- If the release exists, Helm upgrades it.

## Step-by-Step Helm Usage

### 1. Make sure Helm is installed

```bash
helm version
```

### 2. Move into the project root

```bash
cd semester4
```

### 3. Lint the chart

```bash
helm lint helm/student-app
```

This checks for chart mistakes before deployment.

### 4. Preview the rendered manifests

```bash
helm template student-app helm/student-app --namespace student-app
```

This lets you inspect what Kubernetes will receive.

### 5. Package the chart

```bash
helm package helm/student-app --destination dist/helm
```

Expected output looks like:

```text
dist/helm/student-app-0.1.0.tgz
```

### 6. Install the chart
### if namespace gives error use 
```bash
kubectl delete namespace student-app
```
```bash
 
helm install student-app helm/student-app --namespace student-app --create-namespace
```

### 7. Upgrade the chart

```bash
helm upgrade student-app helm/student-app \
  --namespace student-app
```

### 8. Upgrade with a new image tag

```bash
helm upgrade student-app helm/student-app \
  --namespace student-app \
  --set global.imageRegistry=docker.io/gbenga12 \
  --set global.imageTag=build-25
```

### 9. Use the safer install-or-upgrade form

```bash
helm upgrade --install student-app helm/student-app \
  --namespace student-app \
  --create-namespace \
  --wait \
  --set global.imageRegistry=docker.io/gbenga12 \
  --set global.imageTag=build-25
```

### 10. Check release status

```bash
helm list -n student-app
helm status student-app -n student-app
```

### 11. Check deployed values

```bash
helm get values student-app -n student-app
```

### 12. Uninstall the release

```bash
helm uninstall student-app -n student-app
```

## Ingress in This Project

Ingress gives a cleaner way to reach the application through a hostname instead of exposing the gateway only through NodePort.

In this chart, ingress routes traffic to the `api-gateway` service, because the gateway is the public entry point into the microservices system.

## Step-by-Step Ingress Implementation

### 1. Make sure your cluster has an Ingress controller

An Ingress resource does nothing unless a controller exists.

Common controllers:

- NGINX Ingress Controller
- Traefik
- HAProxy Ingress

Check available ingress classes:

```bash
kubectl get ingressclass
```

If you see `nginx`, then `className: nginx` is a good default.

### 2. Decide how you want the gateway exposed

If you use ingress, the cleanest service type for `api-gateway` is usually `ClusterIP`.

That means you can deploy ingress with:

```bash
--set apiGateway.service.type=ClusterIP
```

You can still keep `NodePort` if needed, but `ClusterIP + Ingress` is usually cleaner.

### 3. Enable ingress in the chart

You can enable it from the command line:

```bash
helm upgrade --install student-app helm/student-app \
  --namespace student-app \
  --create-namespace \
  --wait \
  --set apiGateway.service.type=ClusterIP \
  --set apiGateway.ingress.enabled=true \
  --set apiGateway.ingress.className=nginx \
  --set apiGateway.ingress.host=student-app.local
```

Or enable it directly in `values.yaml`:

```yaml
apiGateway:
  service:
    type: ClusterIP
  ingress:
    enabled: true
    className: nginx
    host: student-app.local
    path: /
    pathType: Prefix
```

### 4. Preview the ingress manifest before deploying

```bash
helm template student-app helm/student-app --namespace student-app
```

Confirm that you see a rendered `Ingress` resource named `api-gateway`.

### 5. Deploy the ingress-enabled release

```bash
helm upgrade --install student-app helm/student-app \
  --namespace student-app \
  --create-namespace \
  --wait \
  --set apiGateway.service.type=ClusterIP \
  --set apiGateway.ingress.enabled=true \
  --set apiGateway.ingress.className=nginx \
  --set apiGateway.ingress.host=student-app.local
```

### 6. Verify the ingress resource

```bash
kubectl get ingress -n student-app
kubectl describe ingress api-gateway -n student-app
```

### 7. Map the hostname

For local testing, add an entry to your hosts file, for example:

```text
127.0.0.1 student-app.local
```

In cloud or production environments, point DNS to the ingress controller address instead.

### 8. Test access through ingress

```bash
curl http://student-app.local/
```

If your gateway routes specific APIs, test those through the hostname too.

### 9. Add annotations if needed

Example:

```yaml
apiGateway:
  ingress:
    enabled: true
    className: nginx
    annotations:
      nginx.ingress.kubernetes.io/proxy-body-size: "10m"
```

Apply the change with:

```bash
helm upgrade --install student-app helm/student-app \
  --namespace student-app \
  --wait
```

### 10. Enable TLS when ready

Example:

```yaml
apiGateway:
  ingress:
    tls:
      enabled: true
      secretName: student-app-tls
```

Then redeploy:

```bash
helm upgrade --install student-app helm/student-app \
  --namespace student-app \
  --wait
```

The TLS secret must exist in the same namespace unless you manage certificates automatically.

## Example Ingress Configurations

### Local development example

```yaml
apiGateway:
  service:
    type: ClusterIP
  ingress:
    enabled: true
    className: nginx
    host: student-app.local
    path: /
    pathType: Prefix
```

### Production-style example

```yaml
apiGateway:
  service:
    type: ClusterIP
  ingress:
    enabled: true
    className: nginx
    host: app.example.com
    path: /
    pathType: Prefix
    tls:
      enabled: true
      secretName: student-app-tls
```

## How Jenkins Uses Helm

Jenkins now uses Helm in three steps:

### 1. Lint

```bash
helm lint helm/student-app
```

### 2. Package

```bash
helm package helm/student-app --destination dist/helm
```

### 3. Deploy

```bash
helm upgrade --install student-app helm/student-app \
  --namespace student-app \
  --create-namespace \
  --wait \
  --set global.imageRegistry=<registry> \
  --set global.imageTag=<tag>
```

If you want Jenkins to also deploy ingress, extend that command with:

```bash
--set apiGateway.service.type=ClusterIP \
--set apiGateway.ingress.enabled=true \
--set apiGateway.ingress.className=nginx \
--set apiGateway.ingress.host=student-app.example.com
```

## Best Practices

### 1. Always lint before deploy

```bash
helm lint helm/student-app
```

### 2. Preview before important changes

```bash
helm template student-app helm/student-app --namespace student-app
```

### 3. Use separate values files for environments

Examples:

- `values-dev.yaml`
- `values-staging.yaml`
- `values-prod.yaml`

Deploy with:

```bash
helm upgrade --install student-app helm/student-app \
  --namespace student-app \
  -f values-prod.yaml
```

### 4. Keep changing settings in values

Good candidates for values:

- image tag
- replica count
- ports
- ingress hostname
- resource limits

### 5. Handle secrets more securely in production

The current chart templates a Kubernetes Secret for Postgres. In production, stronger patterns include:

- sealed secrets
- external secrets
- secret manager integration

## Troubleshooting

### Helm lint fails

```bash
helm lint helm/student-app
```

### Rendered output looks wrong

```bash
helm template student-app helm/student-app --namespace student-app
```

Check:

- image names
- ports
- selectors
- ingress host and path

### The new image is not used

Make sure you set:

```bash
--set global.imageTag=your-new-tag
```

and confirm the image exists in the registry.

### Ingress exists but is not reachable

Check:

```bash
kubectl get ingress -n student-app
kubectl describe ingress api-gateway -n student-app
kubectl get ingressclass
kubectl get svc -n student-app
```

Confirm:

- the ingress controller is installed
- the ingress class name matches
- the hostname points to the controller
- the `api-gateway` service is reachable

## Summary

Helm is now the packaging and deployment layer for this Kubernetes application, and ingress can be enabled to expose the gateway more cleanly.

The most important commands to remember are:

```bash
helm lint helm/student-app
helm template student-app helm/student-app --namespace student-app
helm package helm/student-app --destination dist/helm
helm upgrade --install student-app helm/student-app --namespace student-app --create-namespace --wait
helm upgrade --install student-app helm/student-app --namespace student-app --create-namespace --wait --set apiGateway.service.type=ClusterIP --set apiGateway.ingress.enabled=true --set apiGateway.ingress.className=nginx --set apiGateway.ingress.host=student-app.local
```

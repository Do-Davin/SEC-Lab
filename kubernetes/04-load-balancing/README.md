# Load Balancing in Kubernetes

## Overview
This directory demonstrates load balancing concepts in Kubernetes.

## Key Concepts

### 1. Load Balancing with Multiple Replicas
When you create a service that selects multiple pods, Kubernetes automatically distributes traffic across all healthy pods.

### 2. Session Affinity
- **None (default)**: Round-robin load balancing
- **ClientIP**: Sticky sessions - requests from the same client IP go to the same pod

## Files

### lb-deployment.yaml
Creates 5 replicas of a simple hostname server. Each pod returns its own hostname, making it easy to see which pod handled each request.

### lb-service.yaml
LoadBalancer service with round-robin load balancing (no session affinity).

### sticky-session-service.yaml
LoadBalancer service with ClientIP session affinity - requests from the same client always go to the same pod.

### test-load-balancing.sh
Bash script to test load balancing by sending multiple requests and showing which pod handles each one.

## Usage

### Deploy the application:
```bash
kubectl apply -f lb-deployment.yaml
kubectl apply -f lb-service.yaml
```

### Check the pods:
```bash
kubectl get pods -l app=lb-demo -o wide
```

### Test load balancing:
```bash
chmod +x test-load-balancing.sh
./test-load-balancing.sh
```

### Or manually test:
```bash
# Get service endpoint
kubectl get service loadbalancer-demo-service

# Send multiple requests (replace with actual service IP)
for i in {1..10}; do curl <SERVICE_IP>; done
```

### Scale the deployment:
```bash
kubectl scale deployment loadbalancer-demo --replicas=10
```

### View service endpoints:
```bash
kubectl get endpoints loadbalancer-demo-service
kubectl describe service loadbalancer-demo-service
```

## Expected Results
- Each request should be handled by a different pod (round-robin)
- You'll see different hostnames in the responses
- With sticky sessions, requests from the same client go to the same pod

## Clean Up
```bash
kubectl delete -f lb-service.yaml
kubectl delete -f lb-deployment.yaml
```

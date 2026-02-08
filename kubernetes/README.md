# Kubernetes Lab 09 - Complete Guide

## 📚 Overview

This lab provides a comprehensive hands-on introduction to Kubernetes, covering:

1. **Pods** - Basic building blocks of Kubernetes
2. **Deployments** - Managing and scaling applications
3. **Services** - Networking and service discovery
4. **Load Balancing** - Distributing traffic across pods
5. **Case Study** - Complete 3-tier e-commerce application

## 🗂️ Lab Structure

```
kubernetes/
├── 01-pods/                      # Pod examples
│   ├── simple-pod.yaml           # Basic single-container pod
│   ├── multi-container-pod.yaml  # Pod with multiple containers
│   └── pod-with-env.yaml         # Pod with environment variables
│
├── 02-deployments/               # Deployment examples
│   ├── simple-deployment.yaml
│   ├── rolling-update-deployment.yaml
│   ├── recreate-deployment.yaml
│   └── deployment-with-configmap.yaml
│
├── 03-services/                  # Service examples
│   ├── clusterip-service.yaml    # Internal cluster service
│   ├── nodeport-service.yaml     # Exposes on node IP
│   ├── loadbalancer-service.yaml # External load balancer
│   ├── headless-service.yaml     # Direct pod access
│   └── multi-port-service.yaml   # Multiple port exposure
│
├── 04-load-balancing/            # Load balancing demo
│   ├── lb-deployment.yaml
│   ├── lb-service.yaml
│   ├── sticky-session-service.yaml
│   ├── test-load-balancing.sh
│   └── README.md
│
├── 05-case-study/                # Complete application
│   ├── 00-namespace.yaml
│   ├── 01-configmap.yaml
│   ├── 02-secret.yaml
│   ├── 03-database-pvc.yaml
│   ├── 04-database-deployment.yaml
│   ├── 05-database-service.yaml
│   ├── 06-backend-deployment.yaml
│   ├── 07-backend-service.yaml
│   ├── 08-backend-hpa.yaml
│   ├── 09-frontend-configmap.yaml
│   ├── 10-frontend-deployment.yaml
│   ├── 11-frontend-service.yaml
│   ├── deploy-all.sh
│   ├── test-application.sh
│   └── README.md
│
├── README.md                     # This file
├── SETUP.md                      # Setup instructions
└── COMMANDS.md                   # Useful kubectl commands
```

## 🚀 Quick Start

### Prerequisites

1. **Kubernetes cluster** - One of:
   - Minikube (local development)
   - Docker Desktop with Kubernetes
   - Kind (Kubernetes in Docker)
   - Cloud provider (GKE, EKS, AKS)

2. **kubectl** - Kubernetes command-line tool

3. **Basic understanding** of containers and Docker

### Verify Your Setup

```bash
# Check kubectl is installed
kubectl version --client

# Check cluster connection
kubectl cluster-info

# Check nodes
kubectl get nodes
```

## 📖 Learning Path

### Step 1: Pods (15 minutes)

Pods are the smallest deployable units in Kubernetes.

```bash
cd 01-pods

# Create a simple pod
kubectl apply -f simple-pod.yaml

# View pod details
kubectl get pods
kubectl describe pod nginx-pod

# Access pod logs
kubectl logs nginx-pod

# Execute commands in pod
kubectl exec -it nginx-pod -- /bin/bash

# Port forward to access pod
kubectl port-forward nginx-pod 8080:80
# Visit http://localhost:8080

# Clean up
kubectl delete -f simple-pod.yaml
```

**Try these:**
- Multi-container pod with shared volume
- Pod with environment variables

### Step 2: Deployments (20 minutes)

Deployments manage ReplicaSets and provide declarative updates.

```bash
cd 02-deployments

# Create deployment
kubectl apply -f simple-deployment.yaml

# View deployment
kubectl get deployments
kubectl get pods

# Scale deployment
kubectl scale deployment nginx-deployment --replicas=5

# Update deployment (change image version)
kubectl set image deployment/nginx-deployment nginx=nginx:1.22

# View rollout status
kubectl rollout status deployment/nginx-deployment

# View rollout history
kubectl rollout history deployment/nginx-deployment

# Rollback if needed
kubectl rollout undo deployment/nginx-deployment

# Clean up
kubectl delete -f simple-deployment.yaml
```

**Key Concepts:**
- ReplicaSets ensure desired number of pods
- Rolling updates provide zero-downtime deployments
- Rollback capability for failed updates

### Step 3: Services (20 minutes)

Services provide stable networking for pods.

```bash
cd 03-services

# Create deployment first
kubectl apply -f ../02-deployments/simple-deployment.yaml

# Create ClusterIP service (internal only)
kubectl apply -f clusterip-service.yaml
kubectl get service nginx-clusterip-service

# Test from within cluster
kubectl run -it --rm debug --image=busybox --restart=Never -- wget -qO- nginx-clusterip-service

# Create NodePort service (accessible via node IP)
kubectl apply -f nodeport-service.yaml
kubectl get service nginx-nodeport-service

# Access via node port (if using minikube)
minikube service nginx-nodeport-service

# Create LoadBalancer service (cloud providers)
kubectl apply -f loadbalancer-service.yaml
kubectl get service nginx-loadbalancer-service

# Clean up
kubectl delete -f clusterip-service.yaml
kubectl delete -f nodeport-service.yaml
kubectl delete -f loadbalancer-service.yaml
```

**Service Types:**
- **ClusterIP**: Internal cluster communication (default)
- **NodePort**: Exposes service on each node's IP
- **LoadBalancer**: Cloud provider load balancer
- **Headless**: Direct pod IP access (ClusterIP: None)

### Step 4: Load Balancing (30 minutes)

Kubernetes automatically load balances traffic across healthy pods.

```bash
cd 04-load-balancing

# Deploy the demo application
chmod +x test-load-balancing.sh
./test-load-balancing.sh

# Manual testing
kubectl apply -f lb-deployment.yaml
kubectl apply -f lb-service.yaml

# Watch pods being distributed
kubectl get pods -l app=lb-demo -o wide

# Port forward and test
kubectl port-forward service/loadbalancer-demo-service 8080:80

# In another terminal, send multiple requests
for i in {1..20}; do curl localhost:8080; echo ""; done

# You'll see different pod hostnames showing load distribution

# Test sticky sessions
kubectl apply -f sticky-session-service.yaml
# Requests from same IP will go to same pod

# Clean up
kubectl delete -f lb-deployment.yaml
kubectl delete -f lb-service.yaml
```

**Load Balancing Features:**
- Round-robin distribution (default)
- Session affinity (sticky sessions)
- Health checks (readiness/liveness probes)
- Automatic pod discovery

### Step 5: Complete Case Study (45 minutes)

Deploy a full 3-tier e-commerce application.

```bash
cd 05-case-study

# Deploy everything
chmod +x deploy-all.sh test-application.sh
./deploy-all.sh

# Wait for all pods to be ready
kubectl get pods -n ecommerce --watch

# Test the application
./test-application.sh

# Access the application
# If LoadBalancer IP is available:
kubectl get service frontend-service -n ecommerce

# If using minikube/local:
kubectl port-forward -n ecommerce service/frontend-service 8080:80
# Visit http://localhost:8080

# Monitor the application
kubectl get all -n ecommerce
kubectl top pods -n ecommerce

# Check HPA scaling
kubectl get hpa -n ecommerce --watch

# View logs
kubectl logs -n ecommerce -l app=backend --tail=50

# Scale backend manually
kubectl scale deployment backend-deployment -n ecommerce --replicas=6

# Clean up when done
kubectl delete namespace ecommerce
```

**What You'll Learn:**
- Multi-tier application architecture
- ConfigMaps and Secrets management
- Persistent storage with PVCs
- Horizontal Pod Autoscaling
- Health checks and probes
- Service discovery and networking
- Load balancing across tiers

## 🎯 Key Concepts Summary

### Pods
- Smallest deployable unit
- Can contain multiple containers
- Share network namespace and storage volumes
- Ephemeral by nature

### Deployments
- Manage ReplicaSets
- Declarative updates
- Rolling updates and rollbacks
- Self-healing capabilities

### Services
- Stable network endpoint
- Load balancing across pods
- Service discovery via DNS
- Multiple service types for different use cases

### Load Balancing
- Automatic traffic distribution
- Health-based routing
- Session affinity options
- Scales with pod replicas

## 🛠️ Useful Commands

See [COMMANDS.md](COMMANDS.md) for a comprehensive list of kubectl commands.

```bash
# Quick reference
kubectl get pods                    # List pods
kubectl get deployments            # List deployments
kubectl get services               # List services
kubectl describe pod <name>        # Detailed pod info
kubectl logs <pod-name>            # View logs
kubectl exec -it <pod> -- /bin/bash  # Shell into pod
kubectl port-forward <pod> 8080:80 # Port forwarding
kubectl delete pod <name>          # Delete pod
```

## 📊 Architecture Diagrams

### Basic Pod
```
┌─────────────────────┐
│       Pod           │
│  ┌──────────────┐   │
│  │  Container   │   │
│  │  (nginx)     │   │
│  └──────────────┘   │
└─────────────────────┘
```

### Deployment with Service
```
┌──────────────────────────────────┐
│         Deployment               │
│  ┌─────────┐  ┌─────────┐       │
│  │  Pod 1  │  │  Pod 2  │ ...   │
│  └─────────┘  └─────────┘       │
└──────────────────────────────────┘
           ↓
    ┌──────────────┐
    │   Service    │  ← Load Balances
    └──────────────┘
```

### 3-Tier Application
```
┌─────────────────────────────────────────┐
│          Frontend (LoadBalancer)        │
│     ┌──────┐  ┌──────┐  ┌──────┐       │
│     │ Pod1 │  │ Pod2 │  │ Pod3 │       │
│     └──────┘  └──────┘  └──────┘       │
└─────────────────────────────────────────┘
                  ↓
┌─────────────────────────────────────────┐
│          Backend (ClusterIP)            │
│  ┌──────┐  ┌──────┐  ┌──────┐  ┌──────┐│
│  │ Pod1 │  │ Pod2 │  │ Pod3 │  │ Pod4 ││
│  └──────┘  └──────┘  └──────┘  └──────┘│
└─────────────────────────────────────────┘
                  ↓
┌─────────────────────────────────────────┐
│          Database (ClusterIP)           │
│          ┌──────────────┐               │
│          │  MongoDB Pod │               │
│          └──────────────┘               │
│          ┌──────────────┐               │
│          │ Persistent   │               │
│          │   Volume     │               │
│          └──────────────┘               │
└─────────────────────────────────────────┘
```

## 🔍 Troubleshooting

### Pod Issues
```bash
# Pod not starting
kubectl describe pod <pod-name>
kubectl logs <pod-name>

# Pod in CrashLoopBackOff
kubectl logs <pod-name> --previous

# Check events
kubectl get events --sort-by=.metadata.creationTimestamp
```

### Service Issues
```bash
# Service not accessible
kubectl describe service <service-name>
kubectl get endpoints <service-name>

# Test connectivity
kubectl run -it --rm debug --image=busybox --restart=Never -- wget -qO- <service-name>
```

### Deployment Issues
```bash
# Deployment stuck
kubectl rollout status deployment/<name>
kubectl describe deployment <name>

# Rollback
kubectl rollout undo deployment/<name>
```

## 📚 Additional Resources

- [Kubernetes Documentation](https://kubernetes.io/docs/)
- [Kubectl Cheat Sheet](https://kubernetes.io/docs/reference/kubectl/cheatsheet/)
- [Kubernetes Patterns](https://kubernetes.io/docs/concepts/cluster-administration/manage-deployment/)

## ✅ Lab Completion Checklist

- [ ] Successfully created and managed pods
- [ ] Deployed applications using Deployments
- [ ] Configured different service types
- [ ] Demonstrated load balancing across replicas
- [ ] Deployed complete 3-tier application
- [ ] Performed rolling updates
- [ ] Scaled applications manually and automatically
- [ ] Used ConfigMaps and Secrets
- [ ] Implemented health checks
- [ ] Monitored application metrics

## 🎓 Next Steps

After completing this lab, you should explore:

1. **StatefulSets** - For stateful applications
2. **DaemonSets** - Run pods on all nodes
3. **Jobs & CronJobs** - Batch processing
4. **Ingress** - Advanced routing
5. **Network Policies** - Security
6. **Resource Quotas** - Resource management
7. **Helm** - Package manager
8. **Operators** - Custom controllers

## 📝 Notes

- All examples use public container images
- Adjust resource limits based on your cluster
- LoadBalancer services require cloud provider support
- For local testing, use Minikube or port-forwarding

## 🤝 Contributing

Feel free to improve these examples and add new scenarios!

---

**Happy Learning! 🚀**

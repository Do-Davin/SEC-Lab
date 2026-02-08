# Complete Case Study: E-Commerce Application

## Architecture Overview

This case study implements a complete 3-tier e-commerce application with:

1. **Frontend**: React application served by Nginx
2. **Backend**: Node.js/Express API with multiple replicas for load balancing
3. **Database**: MongoDB with persistent storage
4. **Services**: Various service types to connect components

## Application Flow

```
Internet → LoadBalancer → Frontend (3 replicas)
                            ↓
                         Backend Service → Backend API (4 replicas)
                                          ↓
                                      Database Service → MongoDB
```

## Components

### Database Layer
- MongoDB deployment with persistent volume
- ClusterIP service (internal only)
- ConfigMap for database configuration
- Secret for database credentials

### Backend Layer
- Node.js API deployment with 4 replicas
- ClusterIP service for internal communication
- Environment variables from ConfigMap and Secret
- Horizontal Pod Autoscaler for automatic scaling
- Readiness and liveness probes

### Frontend Layer
- Nginx deployment serving React app with 3 replicas
- LoadBalancer service for external access
- ConfigMap for Nginx configuration
- Load balanced across multiple replicas

## Features Demonstrated

1. **Pods**: Basic building blocks with containers
2. **Deployments**: Managing replica sets with rolling updates
3. **Services**: ClusterIP for internal, LoadBalancer for external access
4. **Load Balancing**: Traffic distribution across multiple backend replicas
5. **ConfigMaps**: Configuration management
6. **Secrets**: Sensitive data management
7. **Persistent Volumes**: Data persistence for database
8. **Health Checks**: Readiness and liveness probes
9. **Resource Management**: CPU and memory limits
10. **Auto-scaling**: HPA based on CPU usage

## Deployment Order

1. Create namespace and ConfigMaps/Secrets
2. Deploy database (MongoDB)
3. Deploy backend API
4. Deploy frontend
5. Verify all services are running

## Testing

- Access frontend via LoadBalancer external IP
- Backend API automatically load balances across 4 replicas
- Database maintains state across pod restarts
- Scale backend replicas to handle increased load

## Files Structure

```
05-case-study/
├── 00-namespace.yaml
├── 01-configmap.yaml
├── 02-secret.yaml
├── 03-database-pvc.yaml
├── 04-database-deployment.yaml
├── 05-database-service.yaml
├── 06-backend-deployment.yaml
├── 07-backend-service.yaml
├── 08-backend-hpa.yaml
├── 09-frontend-configmap.yaml
├── 10-frontend-deployment.yaml
├── 11-frontend-service.yaml
├── deploy-all.sh
├── test-application.sh
└── README.md
```

## Quick Start

```bash
cd 05-case-study
chmod +x deploy-all.sh test-application.sh
./deploy-all.sh
./test-application.sh
```

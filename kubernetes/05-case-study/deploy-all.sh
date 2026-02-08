#!/bin/bash

# Deploy All Components Script
# This script deploys the entire e-commerce application

set -e

echo "========================================="
echo "Deploying E-Commerce Application"
echo "========================================="
echo ""

# Colors for output
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Function to print colored output
print_status() {
    echo -e "${GREEN}✓${NC} $1"
}

print_waiting() {
    echo -e "${YELLOW}⏳${NC} $1"
}

# Step 1: Create namespace
echo "Step 1: Creating namespace..."
kubectl apply -f 00-namespace.yaml
print_status "Namespace created"
echo ""

# Step 2: Create ConfigMap and Secrets
echo "Step 2: Creating ConfigMaps and Secrets..."
kubectl apply -f 01-configmap.yaml
kubectl apply -f 02-secret.yaml
print_status "Configuration created"
echo ""

# Step 3: Deploy Database
echo "Step 3: Deploying MongoDB database..."
kubectl apply -f 03-database-pvc.yaml
kubectl apply -f 04-database-deployment.yaml
kubectl apply -f 05-database-service.yaml
print_waiting "Waiting for database to be ready..."
kubectl wait --for=condition=ready pod -l app=mongodb -n ecommerce --timeout=120s
print_status "Database is ready"
echo ""

# Step 4: Deploy Backend
echo "Step 4: Deploying backend API..."
kubectl apply -f 06-backend-deployment.yaml
kubectl apply -f 07-backend-service.yaml
kubectl apply -f 08-backend-hpa.yaml
print_waiting "Waiting for backend pods to be ready..."
kubectl wait --for=condition=ready pod -l app=backend -n ecommerce --timeout=120s
print_status "Backend is ready"
echo ""

# Step 5: Deploy Frontend
echo "Step 5: Deploying frontend..."
kubectl apply -f 09-frontend-configmap.yaml
kubectl apply -f 10-frontend-deployment.yaml
kubectl apply -f 11-frontend-service.yaml
print_waiting "Waiting for frontend pods to be ready..."
kubectl wait --for=condition=ready pod -l app=frontend -n ecommerce --timeout=120s
print_status "Frontend is ready"
echo ""

# Display deployment status
echo "========================================="
echo "Deployment Summary"
echo "========================================="
echo ""

echo "Pods:"
kubectl get pods -n ecommerce -o wide
echo ""

echo "Services:"
kubectl get services -n ecommerce
echo ""

echo "HPA Status:"
kubectl get hpa -n ecommerce
echo ""

# Get frontend service details
echo "========================================="
echo "Access Information"
echo "========================================="
EXTERNAL_IP=$(kubectl get service frontend-service -n ecommerce -o jsonpath='{.status.loadBalancer.ingress[0].ip}')

if [ -z "$EXTERNAL_IP" ]; then
    echo "LoadBalancer IP not yet assigned. You can access the application using:"
    echo ""
    echo "  kubectl port-forward -n ecommerce service/frontend-service 8080:80"
    echo ""
    echo "Then visit: http://localhost:8080"
else
    echo "Application is available at: http://$EXTERNAL_IP"
fi

echo ""
echo "========================================="
echo "Useful Commands"
echo "========================================="
echo "View logs:"
echo "  kubectl logs -n ecommerce -l app=backend --tail=50"
echo ""
echo "Scale backend:"
echo "  kubectl scale deployment backend-deployment -n ecommerce --replicas=6"
echo ""
echo "Check HPA status:"
echo "  kubectl get hpa -n ecommerce --watch"
echo ""
echo "Port forward (if LoadBalancer not available):"
echo "  kubectl port-forward -n ecommerce service/frontend-service 8080:80"
echo ""
echo "========================================="
print_status "Deployment Complete!"
echo "========================================="

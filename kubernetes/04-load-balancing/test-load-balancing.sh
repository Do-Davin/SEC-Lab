#!/bin/bash

# Load Balancing Test Script
# This script demonstrates load balancing across multiple pod replicas

echo "==================================="
echo "Kubernetes Load Balancing Demo"
echo "==================================="
echo ""

# Apply the deployment and service
echo "1. Creating deployment with 5 replicas..."
kubectl apply -f lb-deployment.yaml

echo ""
echo "2. Creating LoadBalancer service..."
kubectl apply -f lb-service.yaml

echo ""
echo "3. Waiting for pods to be ready..."
kubectl wait --for=condition=ready pod -l app=lb-demo --timeout=60s

echo ""
echo "4. Current pod status:"
kubectl get pods -l app=lb-demo -o wide

echo ""
echo "5. Service details:"
kubectl get service loadbalancer-demo-service

echo ""
echo "==================================="
echo "Testing Load Balancing"
echo "==================================="
echo ""
echo "Sending 20 requests to see load distribution..."
echo ""

# Get the service endpoint
SERVICE_IP=$(kubectl get service loadbalancer-demo-service -o jsonpath='{.status.loadBalancer.ingress[0].ip}')

if [ -z "$SERVICE_IP" ]; then
    echo "LoadBalancer IP not yet assigned. Using port-forward instead..."
    kubectl port-forward service/loadbalancer-demo-service 8080:80 &
    PORT_FORWARD_PID=$!
    sleep 3
    SERVICE_URL="localhost:8080"
else
    SERVICE_URL="$SERVICE_IP"
fi

# Send multiple requests and see which pod handles each request
for i in {1..20}; do
    echo "Request $i: $(curl -s $SERVICE_URL)"
done

# Clean up port-forward if used
if [ ! -z "$PORT_FORWARD_PID" ]; then
    kill $PORT_FORWARD_PID 2>/dev/null
fi

echo ""
echo "==================================="
echo "You should see different pod names"
echo "demonstrating round-robin load balancing"
echo "==================================="

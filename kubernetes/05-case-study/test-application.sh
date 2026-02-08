#!/bin/bash

# Test Application Script
# This script tests the deployed e-commerce application

echo "========================================="
echo "Testing E-Commerce Application"
echo "========================================="
echo ""

# Check if application is deployed
if ! kubectl get namespace ecommerce &> /dev/null; then
    echo "Error: ecommerce namespace not found. Please deploy the application first."
    exit 1
fi

# Get service endpoint
EXTERNAL_IP=$(kubectl get service frontend-service -n ecommerce -o jsonpath='{.status.loadBalancer.ingress[0].ip}')

if [ -z "$EXTERNAL_IP" ]; then
    echo "LoadBalancer IP not assigned. Starting port-forward..."
    kubectl port-forward -n ecommerce service/frontend-service 8080:80 &
    PORT_FORWARD_PID=$!
    sleep 3
    ENDPOINT="localhost:8080"
else
    ENDPOINT="$EXTERNAL_IP"
fi

echo "Testing endpoint: http://$ENDPOINT"
echo ""

# Test 1: Frontend availability
echo "Test 1: Frontend health check"
if curl -s -o /dev/null -w "%{http_code}" "http://$ENDPOINT/nginx-health" | grep -q "200"; then
    echo "✓ Frontend is healthy"
else
    echo "✗ Frontend health check failed"
fi
echo ""

# Test 2: Backend API
echo "Test 2: Backend API connectivity"
RESPONSE=$(curl -s "http://$ENDPOINT/api/products")
if echo "$RESPONSE" | grep -q "products"; then
    echo "✓ Backend API is responding"
    echo "Response: $RESPONSE"
else
    echo "✗ Backend API not responding"
fi
echo ""

# Test 3: Load Balancing
echo "Test 3: Load balancing across backend pods"
echo "Sending 10 requests to see distribution..."
declare -A servers
for i in {1..10}; do
    SERVER=$(curl -s "http://$ENDPOINT/api/products" | grep -o '"server":"[^"]*"' | cut -d'"' -f4)
    servers[$SERVER]=$((${servers[$SERVER]:-0} + 1))
    echo "Request $i: $SERVER"
    sleep 0.2
done

echo ""
echo "Distribution:"
for server in "${!servers[@]}"; do
    echo "  $server: ${servers[$server]} requests"
done
echo ""

# Test 4: Pod status
echo "Test 4: Pod health status"
kubectl get pods -n ecommerce
echo ""

# Test 5: Service endpoints
echo "Test 5: Service endpoints"
echo "Frontend endpoints:"
kubectl get endpoints frontend-service -n ecommerce
echo ""
echo "Backend endpoints:"
kubectl get endpoints backend-service -n ecommerce
echo ""

# Test 6: HPA status
echo "Test 6: Horizontal Pod Autoscaler"
kubectl get hpa -n ecommerce
echo ""

# Test 7: Resource usage
echo "Test 7: Resource usage"
kubectl top pods -n ecommerce 2>/dev/null || echo "Metrics server not available"
echo ""

# Cleanup port-forward if used
if [ ! -z "$PORT_FORWARD_PID" ]; then
    kill $PORT_FORWARD_PID 2>/dev/null
    echo "Port-forward cleaned up"
fi

echo "========================================="
echo "Testing Complete!"
echo "========================================="
echo ""
echo "To access the application:"
if [ -z "$EXTERNAL_IP" ]; then
    echo "  kubectl port-forward -n ecommerce service/frontend-service 8080:80"
    echo "  Then visit: http://localhost:8080"
else
    echo "  http://$EXTERNAL_IP"
fi

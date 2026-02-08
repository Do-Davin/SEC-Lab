#!/bin/bash

# Cleanup Script - Remove all lab resources
# Use this to clean up your Kubernetes cluster after completing the lab

echo "========================================="
echo "Kubernetes Lab Cleanup Script"
echo "========================================="
echo ""

# Colors
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

print_warning() {
    echo -e "${YELLOW}⚠️  $1${NC}"
}

print_success() {
    echo -e "${GREEN}✓ $1${NC}"
}

print_error() {
    echo -e "${RED}✗ $1${NC}"
}

# Confirmation
print_warning "This will delete all lab resources from your Kubernetes cluster!"
read -p "Are you sure you want to continue? (yes/no): " confirm

if [ "$confirm" != "yes" ]; then
    echo "Cleanup cancelled."
    exit 0
fi

echo ""
echo "Starting cleanup..."
echo ""

# Delete case study namespace (includes all case study resources)
echo "1. Cleaning up case study application..."
if kubectl get namespace ecommerce &> /dev/null; then
    kubectl delete namespace ecommerce
    print_success "Case study namespace deleted"
else
    print_warning "Case study namespace not found"
fi
echo ""

# Delete pods from section 01
echo "2. Cleaning up pods..."
kubectl delete -f 01-pods/ --ignore-not-found=true &> /dev/null && print_success "Pods cleaned up" || print_warning "No pods found"
echo ""

# Delete deployments from section 02
echo "3. Cleaning up deployments..."
kubectl delete -f 02-deployments/ --ignore-not-found=true &> /dev/null && print_success "Deployments cleaned up" || print_warning "No deployments found"
echo ""

# Delete services from section 03
echo "4. Cleaning up services..."
kubectl delete -f 03-services/ --ignore-not-found=true &> /dev/null && print_success "Services cleaned up" || print_warning "No services found"
echo ""

# Delete load balancing resources from section 04
echo "5. Cleaning up load balancing demo..."
kubectl delete -f 04-load-balancing/ --ignore-not-found=true &> /dev/null && print_success "Load balancing demo cleaned up" || print_warning "No load balancing resources found"
echo ""

# Delete any remaining resources in default namespace
echo "6. Cleaning up any remaining test resources..."
kubectl delete pods,deployments,services,configmaps,secrets -l lab=kubernetes --ignore-not-found=true &> /dev/null

# Check for any remaining lab-related resources
echo ""
echo "7. Checking for remaining resources..."
remaining=$(kubectl get all --all-namespaces | grep -E "nginx|backend|frontend|mongodb|lb-demo" || echo "")
if [ -z "$remaining" ]; then
    print_success "All lab resources removed"
else
    print_warning "Some resources may still exist:"
    echo "$remaining"
fi

echo ""
echo "========================================="
echo "Cleanup Summary"
echo "========================================="
kubectl get all --all-namespaces | grep -c "nginx\|backend\|frontend" || echo "0 resources remaining"

echo ""
echo "========================================="
print_success "Cleanup Complete!"
echo "========================================="
echo ""
echo "Cluster is now clean. You can:"
echo "  - Re-run the lab exercises"
echo "  - Stop your cluster (minikube stop)"
echo "  - Delete your cluster (minikube delete)"
echo ""

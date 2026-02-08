# Kubernetes Setup Guide

## 🎯 Prerequisites

Before starting this lab, ensure you have:

1. A computer with at least 4GB RAM
2. Administrator/sudo access
3. Stable internet connection
4. Basic command line knowledge

## 🐳 Option 1: Minikube (Recommended for Beginners)

Minikube runs a single-node Kubernetes cluster on your local machine.

### macOS

```bash
# Install Minikube
brew install minikube

# Or download directly
curl -LO https://storage.googleapis.com/minikube/releases/latest/minikube-darwin-amd64
sudo install minikube-darwin-amd64 /usr/local/bin/minikube

# Start Minikube
minikube start --driver=docker --cpus=4 --memory=4096

# Verify installation
kubectl get nodes
minikube status

# Enable useful addons
minikube addons enable metrics-server
minikube addons enable ingress
```

### Linux

```bash
# Install Minikube
curl -LO https://storage.googleapis.com/minikube/releases/latest/minikube-linux-amd64
sudo install minikube-linux-amd64 /usr/local/bin/minikube

# Start Minikube
minikube start --driver=docker --cpus=4 --memory=4096

# Verify
kubectl get nodes
```

### Windows

```powershell
# Using Chocolatey
choco install minikube

# Or download installer from
# https://minikube.sigs.k8s.io/docs/start/

# Start Minikube
minikube start --driver=docker --cpus=4 --memory=4096

# Verify
kubectl get nodes
```

## 🐋 Option 2: Docker Desktop

Docker Desktop includes Kubernetes support.

### macOS / Windows

1. Download Docker Desktop from [docker.com](https://www.docker.com/products/docker-desktop)
2. Install and launch Docker Desktop
3. Go to Settings/Preferences → Kubernetes
4. Check "Enable Kubernetes"
5. Click "Apply & Restart"
6. Wait for Kubernetes to start (green icon in bottom left)

```bash
# Verify installation
kubectl cluster-info
kubectl get nodes
```

### Configure Resources

- Go to Settings/Preferences → Resources
- Set CPUs: 4
- Set Memory: 4GB
- Apply & Restart

## 🎪 Option 3: Kind (Kubernetes in Docker)

Kind runs Kubernetes clusters using Docker containers.

### Installation

```bash
# macOS
brew install kind

# Linux
curl -Lo ./kind https://kind.sigs.k8s.io/dl/v0.20.0/kind-linux-amd64
chmod +x ./kind
sudo mv ./kind /usr/local/bin/kind

# Windows (using Chocolatey)
choco install kind

# Create cluster
kind create cluster --name lab-cluster

# Verify
kubectl cluster-info --context kind-lab-cluster
```

### Multi-node cluster (optional)

```bash
# Create config file
cat <<EOF > kind-config.yaml
kind: Cluster
apiVersion: kind.x-k8s.io/v1alpha4
nodes:
- role: control-plane
- role: worker
- role: worker
EOF

# Create cluster
kind create cluster --config kind-config.yaml --name multi-node
```

## ☁️ Option 4: Cloud Providers

For production-like experience, use managed Kubernetes:

### Google Kubernetes Engine (GKE)

```bash
# Install gcloud CLI
# Visit: https://cloud.google.com/sdk/docs/install

# Create cluster
gcloud container clusters create lab-cluster \
  --num-nodes=3 \
  --machine-type=e2-medium \
  --zone=us-central1-a

# Get credentials
gcloud container clusters get-credentials lab-cluster --zone=us-central1-a
```

### Amazon EKS

```bash
# Install eksctl
# Visit: https://eksctl.io/introduction/#installation

# Create cluster
eksctl create cluster \
  --name lab-cluster \
  --region us-west-2 \
  --nodegroup-name standard-workers \
  --node-type t3.medium \
  --nodes 3

# Verify
kubectl get nodes
```

### Azure AKS

```bash
# Install Azure CLI
# Visit: https://docs.microsoft.com/en-us/cli/azure/install-azure-cli

# Login
az login

# Create resource group
az group create --name lab-rg --location eastus

# Create cluster
az aks create \
  --resource-group lab-rg \
  --name lab-cluster \
  --node-count 3 \
  --node-vm-size Standard_B2s \
  --enable-addons monitoring \
  --generate-ssh-keys

# Get credentials
az aks get-credentials --resource-group lab-rg --name lab-cluster
```

## 🔧 Install kubectl

kubectl is the Kubernetes command-line tool.

### macOS

```bash
# Using Homebrew
brew install kubectl

# Or download binary
curl -LO "https://dl.k8s.io/release/$(curl -L -s https://dl.k8s.io/release/stable.txt)/bin/darwin/amd64/kubectl"
chmod +x kubectl
sudo mv kubectl /usr/local/bin/
```

### Linux

```bash
# Download latest
curl -LO "https://dl.k8s.io/release/$(curl -L -s https://dl.k8s.io/release/stable.txt)/bin/linux/amd64/kubectl"

# Install
sudo install -o root -g root -m 0755 kubectl /usr/local/bin/kubectl

# Verify
kubectl version --client
```

### Windows

```powershell
# Using Chocolatey
choco install kubernetes-cli

# Or download from
# https://kubernetes.io/docs/tasks/tools/install-kubectl-windows/
```

## ✅ Verify Your Setup

Run these commands to ensure everything is working:

```bash
# Check kubectl version
kubectl version --client

# Check cluster connection
kubectl cluster-info

# List nodes
kubectl get nodes

# List all resources in all namespaces
kubectl get all --all-namespaces

# Check you can create resources
kubectl create namespace test
kubectl delete namespace test
```

Expected output:
- kubectl version shows client version
- cluster-info shows cluster details
- At least one node in "Ready" state

## 🔍 Troubleshooting

### Minikube won't start

```bash
# Delete and recreate
minikube delete
minikube start --driver=docker

# Check logs
minikube logs

# Try different driver
minikube start --driver=virtualbox
```

### kubectl not connecting

```bash
# Check config
kubectl config view

# Check current context
kubectl config current-context

# Switch context (if multiple clusters)
kubectl config use-context <context-name>

# For Minikube
minikube update-context
```

### Docker Desktop Kubernetes not starting

1. Quit Docker Desktop
2. Delete: `~/Library/Group Containers/group.com.docker/pki` (macOS)
3. Restart Docker Desktop
4. Re-enable Kubernetes

### "The connection to the server was refused"

```bash
# For Minikube
minikube status  # Check if running
minikube start   # Start if stopped

# For Docker Desktop
# Restart Docker Desktop

# For Kind
kind get clusters
kubectl cluster-info --context kind-<cluster-name>
```

## 🎨 Optional: Useful Tools

### kubectx & kubens

Switch between clusters and namespaces easily.

```bash
# macOS
brew install kubectx

# Usage
kubectx                  # List contexts
kubectx <context-name>   # Switch context
kubens                   # List namespaces
kubens <namespace>       # Switch namespace
```

### k9s

Terminal UI for Kubernetes.

```bash
# macOS
brew install k9s

# Linux
curl -sS https://webinstall.dev/k9s | bash

# Run
k9s
```

### Helm

Kubernetes package manager.

```bash
# macOS
brew install helm

# Linux
curl https://raw.githubusercontent.com/helm/helm/main/scripts/get-helm-3 | bash

# Verify
helm version
```

## 📊 Resource Requirements

### Minimum
- 2 CPU cores
- 2GB RAM
- 20GB disk space

### Recommended
- 4 CPU cores
- 4GB RAM
- 50GB disk space

### For Case Study
- 4 CPU cores
- 8GB RAM (to run all pods comfortably)

## 🚀 Quick Start Verification

Once setup is complete, run this quick test:

```bash
# Create a test deployment
kubectl create deployment nginx --image=nginx

# Expose it
kubectl expose deployment nginx --port=80 --type=NodePort

# Check status
kubectl get deployments
kubectl get pods
kubectl get services

# Access it (Minikube)
minikube service nginx

# Or port-forward
kubectl port-forward deployment/nginx 8080:80
# Visit http://localhost:8080

# Clean up
kubectl delete deployment nginx
kubectl delete service nginx
```

If this works, you're ready for the lab! 🎉

## 📚 Next Steps

Now that your environment is ready:

1. Read the main [README.md](README.md)
2. Start with [01-pods](01-pods/)
3. Work through each section sequentially
4. Complete the [case study](05-case-study/)

## 💡 Tips

- Start with Minikube if you're new to Kubernetes
- Use `kubectl explain <resource>` to learn about resources
- Enable bash/zsh completion for kubectl:
  ```bash
  # Bash
  echo 'source <(kubectl completion bash)' >> ~/.bashrc
  
  # Zsh
  echo 'source <(kubectl completion zsh)' >> ~/.zshrc
  ```

---

**Need help?** Check the troubleshooting section or consult [Kubernetes documentation](https://kubernetes.io/docs/).

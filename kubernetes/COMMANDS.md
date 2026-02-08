# Kubernetes kubectl Commands Cheat Sheet

## 📑 Table of Contents
- [Cluster Info](#cluster-info)
- [Contexts and Configuration](#contexts-and-configuration)
- [Pods](#pods)
- [Deployments](#deployments)
- [Services](#services)
- [Namespaces](#namespaces)
- [ConfigMaps and Secrets](#configmaps-and-secrets)
- [Logs and Debugging](#logs-and-debugging)
- [Scaling and Updates](#scaling-and-updates)
- [Resource Management](#resource-management)
- [Advanced Operations](#advanced-operations)

---

## Cluster Info

```bash
# Display cluster info
kubectl cluster-info

# View cluster nodes
kubectl get nodes

# Detailed node info
kubectl describe node <node-name>

# Check node resource usage
kubectl top nodes

# View cluster version
kubectl version

# View API resources
kubectl api-resources

# View API versions
kubectl api-versions
```

---

## Contexts and Configuration

```bash
# View current context
kubectl config current-context

# List all contexts
kubectl config get-contexts

# Switch context
kubectl config use-context <context-name>

# View kubectl config
kubectl config view

# Set namespace for current context
kubectl config set-context --current --namespace=<namespace>

# Create new context
kubectl config set-context <name> --cluster=<cluster> --user=<user> --namespace=<namespace>
```

---

## Pods

### Viewing Pods

```bash
# List pods in current namespace
kubectl get pods

# List pods in all namespaces
kubectl get pods --all-namespaces
kubectl get pods -A

# List pods with more details
kubectl get pods -o wide

# List pods with labels
kubectl get pods --show-labels

# Filter pods by label
kubectl get pods -l app=nginx
kubectl get pods --selector=app=nginx

# Watch pods in real-time
kubectl get pods --watch
kubectl get pods -w

# Get pod details in YAML
kubectl get pod <pod-name> -o yaml

# Get pod details in JSON
kubectl get pod <pod-name> -o json
```

### Creating Pods

```bash
# Create pod from file
kubectl apply -f pod.yaml

# Create pod from stdin
cat <<EOF | kubectl apply -f -
apiVersion: v1
kind: Pod
metadata:
  name: nginx
spec:
  containers:
  - name: nginx
    image: nginx
EOF

# Create pod imperatively
kubectl run nginx --image=nginx

# Create pod with labels
kubectl run nginx --image=nginx --labels="app=nginx,env=prod"

# Create pod with env variables
kubectl run nginx --image=nginx --env="KEY=value"

# Create pod with port
kubectl run nginx --image=nginx --port=80
```

### Pod Operations

```bash
# Describe pod (detailed info)
kubectl describe pod <pod-name>

# Get pod logs
kubectl logs <pod-name>

# Get logs from specific container
kubectl logs <pod-name> -c <container-name>

# Follow logs in real-time
kubectl logs -f <pod-name>

# Get previous container logs
kubectl logs <pod-name> --previous

# Execute command in pod
kubectl exec <pod-name> -- <command>

# Interactive shell
kubectl exec -it <pod-name> -- /bin/bash
kubectl exec -it <pod-name> -- /bin/sh

# Execute in specific container
kubectl exec -it <pod-name> -c <container-name> -- /bin/bash

# Copy files to/from pod
kubectl cp <local-file> <pod-name>:<remote-path>
kubectl cp <pod-name>:<remote-path> <local-file>

# Port forward to pod
kubectl port-forward <pod-name> 8080:80

# Delete pod
kubectl delete pod <pod-name>

# Force delete pod
kubectl delete pod <pod-name> --force --grace-period=0

# Delete all pods
kubectl delete pods --all
```

---

## Deployments

### Viewing Deployments

```bash
# List deployments
kubectl get deployments
kubectl get deploy

# List with details
kubectl get deployments -o wide

# Describe deployment
kubectl describe deployment <deployment-name>

# View deployment YAML
kubectl get deployment <deployment-name> -o yaml

# Watch deployment
kubectl get deployments --watch
```

### Creating Deployments

```bash
# Create deployment from file
kubectl apply -f deployment.yaml

# Create deployment imperatively
kubectl create deployment nginx --image=nginx

# Create with replicas
kubectl create deployment nginx --image=nginx --replicas=3

# Create with port
kubectl create deployment nginx --image=nginx --port=80

# Generate YAML without creating
kubectl create deployment nginx --image=nginx --dry-run=client -o yaml

# Generate and save to file
kubectl create deployment nginx --image=nginx --dry-run=client -o yaml > deployment.yaml
```

### Deployment Operations

```bash
# Scale deployment
kubectl scale deployment <deployment-name> --replicas=5

# Autoscale deployment
kubectl autoscale deployment <deployment-name> --min=2 --max=10 --cpu-percent=80

# Update image
kubectl set image deployment/<deployment-name> <container-name>=<new-image>

# Edit deployment
kubectl edit deployment <deployment-name>

# Rollout status
kubectl rollout status deployment/<deployment-name>

# Rollout history
kubectl rollout history deployment/<deployment-name>

# Rollback to previous version
kubectl rollout undo deployment/<deployment-name>

# Rollback to specific revision
kubectl rollout undo deployment/<deployment-name> --to-revision=2

# Pause rollout
kubectl rollout pause deployment/<deployment-name>

# Resume rollout
kubectl rollout resume deployment/<deployment-name>

# Restart deployment
kubectl rollout restart deployment/<deployment-name>

# Delete deployment
kubectl delete deployment <deployment-name>
```

---

## Services

### Viewing Services

```bash
# List services
kubectl get services
kubectl get svc

# List with details
kubectl get services -o wide

# Describe service
kubectl describe service <service-name>

# Get service endpoints
kubectl get endpoints <service-name>

# View service YAML
kubectl get service <service-name> -o yaml
```

### Creating Services

```bash
# Create service from file
kubectl apply -f service.yaml

# Expose deployment as service
kubectl expose deployment <deployment-name> --port=80 --target-port=8080

# Create ClusterIP service
kubectl expose deployment <deployment-name> --port=80 --type=ClusterIP

# Create NodePort service
kubectl expose deployment <deployment-name> --port=80 --type=NodePort

# Create LoadBalancer service
kubectl expose deployment <deployment-name> --port=80 --type=LoadBalancer

# Expose pod
kubectl expose pod <pod-name> --port=80

# Create service imperatively
kubectl create service clusterip my-service --tcp=80:8080

# Generate YAML
kubectl create service clusterip my-service --tcp=80:8080 --dry-run=client -o yaml
```

### Service Operations

```bash
# Port forward to service
kubectl port-forward service/<service-name> 8080:80

# Delete service
kubectl delete service <service-name>

# Test service (using temporary pod)
kubectl run -it --rm debug --image=busybox --restart=Never -- wget -qO- <service-name>
```

---

## Namespaces

```bash
# List namespaces
kubectl get namespaces
kubectl get ns

# Create namespace
kubectl create namespace <namespace-name>

# Create from file
kubectl apply -f namespace.yaml

# Delete namespace
kubectl delete namespace <namespace-name>

# Get resources in namespace
kubectl get all -n <namespace-name>

# Set default namespace
kubectl config set-context --current --namespace=<namespace-name>

# Run command in specific namespace
kubectl get pods -n <namespace-name>
```

---

## ConfigMaps and Secrets

### ConfigMaps

```bash
# List configmaps
kubectl get configmaps
kubectl get cm

# Create from literal
kubectl create configmap my-config --from-literal=key1=value1 --from-literal=key2=value2

# Create from file
kubectl create configmap my-config --from-file=config.properties

# Create from directory
kubectl create configmap my-config --from-file=config-dir/

# Create from env file
kubectl create configmap my-config --from-env-file=config.env

# View configmap
kubectl describe configmap <configmap-name>
kubectl get configmap <configmap-name> -o yaml

# Edit configmap
kubectl edit configmap <configmap-name>

# Delete configmap
kubectl delete configmap <configmap-name>
```

### Secrets

```bash
# List secrets
kubectl get secrets

# Create generic secret
kubectl create secret generic my-secret --from-literal=password=secretpass

# Create from file
kubectl create secret generic my-secret --from-file=ssh-privatekey=~/.ssh/id_rsa

# Create TLS secret
kubectl create secret tls tls-secret --cert=path/to/tls.cert --key=path/to/tls.key

# Create docker registry secret
kubectl create secret docker-registry regcred \
  --docker-server=<server> \
  --docker-username=<username> \
  --docker-password=<password> \
  --docker-email=<email>

# View secret
kubectl describe secret <secret-name>

# Get secret value (base64 encoded)
kubectl get secret <secret-name> -o yaml

# Decode secret
kubectl get secret <secret-name> -o jsonpath='{.data.password}' | base64 --decode

# Delete secret
kubectl delete secret <secret-name>
```

---

## Logs and Debugging

```bash
# Get pod logs
kubectl logs <pod-name>

# Follow logs
kubectl logs -f <pod-name>

# Logs from previous container
kubectl logs <pod-name> --previous

# Logs from specific container
kubectl logs <pod-name> -c <container-name>

# Logs from all containers
kubectl logs <pod-name> --all-containers=true

# Tail last N lines
kubectl logs <pod-name> --tail=100

# Logs since duration
kubectl logs <pod-name> --since=1h

# Logs with timestamps
kubectl logs <pod-name> --timestamps

# Logs from multiple pods
kubectl logs -l app=nginx

# Get events
kubectl get events

# Sorted events
kubectl get events --sort-by=.metadata.creationTimestamp

# Events for specific object
kubectl get events --field-selector involvedObject.name=<pod-name>

# Describe resource (shows events too)
kubectl describe pod <pod-name>

# Check pod status
kubectl get pod <pod-name> -o jsonpath='{.status.phase}'

# Check container status
kubectl get pod <pod-name> -o jsonpath='{.status.containerStatuses[*].state}'

# Debug with temporary pod
kubectl run -it --rm debug --image=busybox --restart=Never -- /bin/sh

# Debug network
kubectl run -it --rm debug --image=nicolaka/netshoot --restart=Never -- /bin/bash
```

---

## Scaling and Updates

```bash
# Scale deployment
kubectl scale deployment <deployment-name> --replicas=5

# Scale replicaset
kubectl scale rs <replicaset-name> --replicas=3

# Autoscale
kubectl autoscale deployment <deployment-name> --min=2 --max=10 --cpu-percent=80

# View HPA
kubectl get hpa

# Update image
kubectl set image deployment/<deployment-name> nginx=nginx:1.22

# Update multiple containers
kubectl set image deployment/<deployment-name> nginx=nginx:1.22 sidecar=sidecar:v2

# Update with record
kubectl set image deployment/<deployment-name> nginx=nginx:1.22 --record

# Edit resource
kubectl edit deployment <deployment-name>

# Patch resource
kubectl patch deployment <deployment-name> -p '{"spec":{"replicas":5}}'

# Replace resource
kubectl replace -f deployment.yaml

# Apply changes
kubectl apply -f deployment.yaml

# Rollout status
kubectl rollout status deployment/<deployment-name>

# Rollback
kubectl rollout undo deployment/<deployment-name>

# Restart
kubectl rollout restart deployment/<deployment-name>
```

---

## Resource Management

```bash
# View resource usage
kubectl top nodes
kubectl top pods

# View pod resource usage
kubectl top pod <pod-name>

# View pod resource usage with containers
kubectl top pod <pod-name> --containers

# Resource quota
kubectl get resourcequota

# Limit ranges
kubectl get limitrange

# Describe quota
kubectl describe resourcequota

# Create quota
kubectl create quota my-quota --hard=cpu=1,memory=1G,pods=2

# Set resources
kubectl set resources deployment <deployment-name> -c=<container-name> --limits=cpu=200m,memory=512Mi

# Label resource
kubectl label pod <pod-name> env=prod

# Annotate resource
kubectl annotate pod <pod-name> description="My pod"

# Remove label
kubectl label pod <pod-name> env-

# Remove annotation
kubectl annotate pod <pod-name> description-
```

---

## Advanced Operations

### Apply and Dry-run

```bash
# Apply configuration
kubectl apply -f config.yaml

# Apply directory
kubectl apply -f ./configs/

# Apply with server-side dry-run
kubectl apply -f config.yaml --dry-run=server

# Apply with client-side dry-run
kubectl apply -f config.yaml --dry-run=client

# View diff before applying
kubectl diff -f config.yaml

# Delete resources
kubectl delete -f config.yaml

# Delete by label
kubectl delete pods -l app=nginx

# Delete all in namespace
kubectl delete all --all -n <namespace>
```

### Kubectl Output Options

```bash
# YAML output
kubectl get pod <pod-name> -o yaml

# JSON output
kubectl get pod <pod-name> -o json

# Wide output
kubectl get pods -o wide

# Custom columns
kubectl get pods -o custom-columns=NAME:.metadata.name,STATUS:.status.phase

# JSONPath
kubectl get pods -o jsonpath='{.items[*].metadata.name}'

# Template
kubectl get pods -o go-template='{{range .items}}{{.metadata.name}}{{"\n"}}{{end}}'

# Name only
kubectl get pods -o name

# Show labels
kubectl get pods --show-labels
```

### Kubectl Plugins

```bash
# List plugins
kubectl plugin list

# Install krew (plugin manager)
# Visit: https://krew.sigs.k8s.io/docs/user-guide/setup/install/

# Popular plugins
kubectl krew install ctx    # kubectx
kubectl krew install ns     # kubens
kubectl krew install tree   # kubectl-tree
kubectl krew install view-secret  # view-secret
```

### Useful Aliases

Add to your `.bashrc` or `.zshrc`:

```bash
alias k='kubectl'
alias kg='kubectl get'
alias kd='kubectl describe'
alias kdel='kubectl delete'
alias kl='kubectl logs'
alias kex='kubectl exec -it'
alias kgp='kubectl get pods'
alias kgs='kubectl get services'
alias kgd='kubectl get deployments'
alias kgn='kubectl get nodes'
alias kc='kubectl config'
alias kcc='kubectl config current-context'
alias kuc='kubectl config use-context'
```

---

## Quick Reference

### Most Common Commands

```bash
# View resources
kubectl get pods
kubectl get deployments
kubectl get services
kubectl get all

# Describe resource
kubectl describe pod <name>

# Create from file
kubectl apply -f <file>

# Delete resource
kubectl delete pod <name>

# Logs
kubectl logs <pod-name>

# Execute command
kubectl exec -it <pod> -- /bin/bash

# Port forward
kubectl port-forward <pod> 8080:80

# Scale
kubectl scale deployment <name> --replicas=3
```

---

## Tips and Tricks

```bash
# Watch resources
watch kubectl get pods

# Get all resources in namespace
kubectl get all

# Explain resource
kubectl explain pod
kubectl explain pod.spec
kubectl explain pod.spec.containers

# Generate YAML template
kubectl create deployment nginx --image=nginx --dry-run=client -o yaml

# Multiple resources
kubectl get pods,services

# Sort by name
kubectl get pods --sort-by=.metadata.name

# Sort by creation time
kubectl get pods --sort-by=.metadata.creationTimestamp

# Filter by field
kubectl get pods --field-selector=status.phase=Running

# Multiple filters
kubectl get pods --field-selector=status.phase=Running,metadata.namespace=default

# Completion
source <(kubectl completion bash)  # Bash
source <(kubectl completion zsh)   # Zsh
```

---

For more information, visit the [official kubectl documentation](https://kubernetes.io/docs/reference/kubectl/).

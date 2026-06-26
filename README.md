# End-to-End GitOps Pipeline — Java Spring Boot

A production-grade CI/CD pipeline that automates the complete software delivery lifecycle of a **Java Spring Boot** application — from source code to a live Kubernetes deployment on **AWS EKS Fargate** — with security scanning, quality gates, and GitOps-based continuous delivery.

---

# Project Overview

This project demonstrates a real-world DevOps workflow where every code push triggers an automated pipeline that:

1. **Builds** the Java application using Maven
2. **Tests** code quality using SonarQube
3. **Scans** Docker images for vulnerabilities using Trivy
4. **Stores** artifacts in JFrog Artifactory
5. **Pushes** Docker images to DockerHub
6. **Deploys** automatically to AWS EKS Fargate via Argo CD (GitOps)

---

# 🏗️ Architecture

```
Developer pushes code to GitHub
        │
        ▼
  Jenkins CI Pipeline
  ┌─────────────────────────────────────────┐
  │  Maven Build → SonarQube → Trivy Scan  │
  │  → JFrog Artifactory → DockerHub Push  │
  └─────────────────────────────────────────┘
        │
        ▼
  GitHub Manifest Repo Updated
        │
        ▼
  Argo CD (GitOps) on EKS Fargate
  ┌──────────────────────────────┐
  │  Auto-Sync → Self-Healing   │
  │  Kubernetes Deployment Live │
  └──────────────────────────────┘
```

---

# Tech Stack

| Category | Tools |
|---|---|
| **Application** | Java Spring Boot, Maven |
| **CI/CD** | Jenkins (Declarative Pipeline, Shared Libraries) |
| **Containerization** | Docker, DockerHub |
| **Container Orchestration** | Kubernetes, AWS EKS Fargate |
| **Infrastructure as Code** | Terraform (EKS Module) |
| **GitOps / CD** | Argo CD |
| **Code Quality** | SonarQube + Webhooks |
| **Security Scanning** | Trivy |
| **Artifact Storage** | JFrog Artifactory |
| **Cloud** | AWS (EC2, EKS, Fargate, IAM, VPC) |

---

# 📁 Repository Structure

```
├── src/                    # Java Spring Boot application source code
├── eks_module/             # Terraform code to provision AWS EKS Fargate cluster
├── kubernetes/             # Kubernetes manifest files (Deployment, Service)
├── Dockerfile              # Docker image definition (eclipse-temurin base)
├── Jenkinsfile             # Main CI pipeline (Build + Test + Quality Gate)
├── Jenkinsfile_jfrog       # Pipeline with JFrog Artifactory integration
├── Jenkinsfile_k8s         # Pipeline with Kubernetes deployment stage
├── pom.xml                 # Maven project config
└── mvnw / mvnw.cmd         # Maven wrapper scripts
```

---

## ⚙️ CI Pipeline Stages (Jenkinsfile)

```
Checkout → Maven Build → SonarQube Analysis → Quality Gate
→ Trivy Image Scan → Push to DockerHub / JFrog → Update K8s Manifest
```

### Key Highlights:
- Built using **Jenkins Declarative Pipeline** with shared libraries for reusability
- **SonarQube quality gate** enforced via webhook — pipeline fails if code quality drops
- **Trivy** scans Docker image for CVEs before pushing to registry
- **Multi-version Java conflict** (Java 8 vs Java 21) resolved by isolating `JAVA_HOME` per stage
- Three Jenkinsfiles for modular, stage-by-stage understanding

---

## 🌐 Infrastructure (Terraform — `eks_module/`)

- Provisions a **serverless AWS EKS Fargate** cluster using `eksctl` and Terraform
- CI/CD tools (Jenkins, SonarQube, JFrog) hosted on **AWS EC2**
- VPC, Subnets, IAM roles configured for secure cluster access

---

## 🔄 GitOps with Argo CD (`kubernetes/`)

- **Argo CD** deployed on EKS monitors the GitHub manifest repo
- Any change to Kubernetes YAML files triggers **automatic sync to the cluster**
- **Self-healing** enabled — if someone manually changes the cluster, Argo CD reverts it to match the GitHub state
- Zero-downtime deployments via rolling update strategy

---

## 🔒 Security & Quality Gates

| Tool | Purpose |
|---|---|
| SonarQube | Static code analysis, bug & vulnerability detection |
| Trivy | Container image vulnerability scanning |
| JFrog Artifactory | Secure, versioned artifact and image storage |

---

## 🚀 How to Run

### Prerequisites
- AWS account with IAM permissions
- Jenkins server running on EC2
- SonarQube, JFrog Artifactory running on EC2
- Terraform installed
- `kubectl` and `eksctl` configured

### Step 1 — Provision EKS Cluster
```bash
cd eks_module/
terraform init
terraform apply
```

### Step 2 — Configure Jenkins
- Add credentials: DockerHub, SonarQube token, JFrog, AWS
- Create a Pipeline job pointing to this repo's `Jenkinsfile`

### Step 3 — Install Argo CD on EKS
```bash
kubectl create namespace argocd
kubectl apply -n argocd -f https://raw.githubusercontent.com/argoproj/argo-cd/stable/manifests/install.yaml
```

### Step 4 — Connect Argo CD to GitHub Manifest Repo
- Point Argo CD to the `kubernetes/` folder
- Enable auto-sync and self-healing

### Step 5 — Trigger the Pipeline
- Push any code change to `main` branch
- Jenkins pipeline runs automatically end-to-end

---

## 📊 Pipeline Flow Diagram

```
git push
   │
   ▼
Jenkins Pipeline
   ├── 1. Checkout Code
   ├── 2. Maven Build (mvn clean package)
   ├── 3. SonarQube Analysis
   ├── 4. Quality Gate Check ──── FAIL → Pipeline stops ❌
   ├── 5. Trivy Image Scan
   ├── 6. Docker Build & Push to DockerHub
   ├── 7. Store JAR in JFrog Artifactory
   └── 8. Update K8s Manifest (image tag)
              │
              ▼
         Argo CD detects change
              │
              ▼
         Auto-sync to EKS Fargate ✅
              │
              ▼
         Application Live on Kubernetes 🚀
```

---

## 🎯 Key Learnings & Challenges Solved

- ✅ Resolved **Java 8 / Java 21 version conflict** between Maven and SonarQube by setting `JAVA_HOME` per pipeline stage
- ✅ Used **Jenkins shared libraries** to keep pipeline DRY and modular
- ✅ Configured **SonarQube webhooks** to block deployment on quality failures
- ✅ Implemented **GitOps pattern** — cluster state always driven by Git, never manual kubectl

---

## 👨‍💻 Author

Udit Raj Dubey
MCA Student | Aspiring DevOps Engineer

  uditrajdubey48@gmail.com
  [LinkedIn]_(https://www.linkedin.com/feed/)
  [GitHub](https://github.com/uditrajdubey)


## 📄 License

This project is open source and available under the [MIT License](LICENSE).

# BEM13 ECS CICD — Application Repository

**Author:** Timothy Nlenjibi | **Lab:** BEM13 Running Containers on AWS

This repository contains the Spring Boot application, Dockerfile, and GitHub Actions workflows for the BEM13 ECS CICD lab.

---

## Repository Structure

```
ecs-cicd-app/
├── src/
│   ├── main/java/com/example/bem13/
│   │   ├── Bem13Application.java
│   │   ├── controller/
│   │   │   ├── InfoController.java    # GET /api/info
│   │   │   └── QuoteController.java   # GET /api/quotes, /random, /{id}, /health
│   │   └── model/
│   │       ├── AppInfo.java
│   │       └── Quote.java
│   ├── main/resources/
│   │   ├── application.yaml
│   │   └── static/index.html          # Dashboard UI
│   └── test/java/com/example/bem13/
│       ├── InfoControllerTest.java
│       └── QuoteControllerTest.java
├── codedeploy/
│   ├── appspec.yaml                   # CodeDeploy ECS spec
│   └── taskdef.json                   # Task definition template (<IMAGE1_NAME>)
├── pom.xml                            # Spring Boot 3.4.5, Java 21
├── Dockerfile                         # eclipse-temurin:21-jre-alpine
└── .github/workflows/
    ├── ci.yml                         # Build + test on push to dev/test/main
    └── deploy.yml                     # Build image, push to ECR (OIDC auth)
```

---

## Local Development

```bash
# Build and run
mvn clean package -DskipTests -B
java -jar target/bem13app-0.0.1-SNAPSHOT.jar

# Run tests (6 tests, 0 failures expected)
mvn test -B

# Docker build and run
docker build -t bem13app:local .
docker run -p 8080:8080 -e SPRING_PROFILES_ACTIVE=dev bem13app:local
```

Open `http://localhost:8080` for the dashboard.

---

## CI/CD

| Branch | Workflow | Action |
|---|---|---|
| `dev` | ci.yml + deploy.yml | Build, test, push image tagged `dev-{sha}` |
| `test` | ci.yml + deploy.yml | Build, test, push image tagged `test-{sha}` |
| `main` | ci.yml + deploy.yml | Build, test, push image tagged `prod-{sha}` |

### Required GitHub Secret

```
AWS_ROLE_ARN = arn:aws:iam::<AccountId>:role/prod-bem13-github-actions-role
```

No other AWS secrets — OIDC handles authentication.

---

## API Endpoints

| Method | Path | Description |
|---|---|---|
| GET | `/` | Dashboard UI |
| GET | `/health` | Returns `OK` (ALB health check) |
| GET | `/api/info` | Hostname, env, Java version, author, lab name |
| GET | `/api/quotes` | All 10 quotes |
| GET | `/api/quotes/random` | Random quote |
| GET | `/api/quotes/{id}` | Quote by ID or 404 |

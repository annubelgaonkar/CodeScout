# GitHub Repository Searcher

A Spring Boot service that fetches repositories from the GitHub API, stores them in PostgreSQL, and provides endpoints to query them with filters and sorting.


---
⚡ Features

🔎 Search GitHub repositories by keyword and language

💾 Store repositories in a relational database

📂 Retrieve stored repositories with filters:

- Language

- Minimum Stars

- Sort by: stars, forks, updated

🚨 Error handling with clear JSON responses (rate limits, invalid requests)

✅ Tests included for controllers and services



---

## 🚀 Tech Stack
- Java 21
- Spring Boot 3.x
- Spring Web (REST APIs)
- Spring Data JPA + PostgreSQL
- WebClient (for GitHub API calls)
- Global Exception Handling
- JUnit 5 & Mockito for testing

---

## ▶️ Getting Started
1. Clone and Build
```
git clone https://github.com/annubelgaonkar/GithubRepoSearcher.git
cd GithubRepoSearcher
mvn clean package
```
2. Run
```
mvn spring-boot:run
```
Or run the built JAR:
```
java -jar target/github-repo-searcher-0.0.1-SNAPSHOT.jar

```

The service starts on:
👉 http://localhost:8080
---
## ⚡ Endpoints

### 1. Search GitHub Repositories
**POST** `/api/github/search`

**Request body:**
```json
{
  "query": "spring boot",
  "language": "java",
  "sort": "stars"
}
```

**Response:**
```json
{
  "message": "Repositories fetched and saved successfully",
  "repositories": [
    {
      "id": 123,
      "name": "spring-boot",
      "description": "Spring Boot makes it easy...",
      "owner": "spring-projects",
      "language": "Java",
      "stars": 67000,
      "forks": 30000,
      "lastUpdated": "2024-08-18T12:00:00Z"
    }
  ]
}
```

### 2. Get stored repositories
**GET** `/api/github/repositories?language=Java&minStars=100&sort=stars`

**Response:**
```json
{
  "repositories": [
    {
      "id": 123,
      "name": "spring-boot",
      "description": "This is an example repository",
      "owner": "spring-projects",
      "language": "Java",
      "stars": 67000,
      "forks": 30000,
      "lastUpdated": "2024-08-18T12:00:00Z"
    }
  ]
}
```
### 3. Example Error Response (Rate Limit)
```
{
  "success": false,
  "message": "GitHub rate limit exceeded. Retry after 60 seconds",
  "status": 429,
  "timestamp": "2024-08-19T11:00:00"
}

```
### 🛠️ Setup & Run
### Prerequisites

- JDK 17/21
- Maven 3.9+

### Run PostgreSQL
```bash
docker run --name pg-grs -e POSTGRES_USER=grs -e POSTGRES_PASSWORD=grs -e POSTGRES_DB=grs -p 5432:5432 -d postgres:16
```
### Build & Run
```
./mvnw clean package
java -jar target/github-repo-searcher-0.0.1-SNAPSHOT.jar
```
### Swagger UI
http://localhost:8080/swagger-ui/index.html

👤 Author
Anuradha Belgaonkar — Backend Developer (Java, Spring Boot, Microservices)
# GitHub Repository Searcher

A Spring Boot service that fetches repositories from the GitHub API, stores them in PostgreSQL, and provides endpoints to query them with filters and sorting.

---

## 🚀 Tech Stack
- Java 21
- Spring Boot 3.x
- Spring Web (REST APIs)
- Spring Data JPA + PostgreSQL
- WebClient (for GitHub API calls)
- Global Exception Handling
- JUnit 5 & Mockito for testing
- Swagger / OpenAPI for documentation

---

## 📂 Project Structure
```
src/main/java/dev/anuradha/githubreposearcher/
├── controller/ # REST Controllers
├── service/ # Business logic
├── repository/ # JPA Repositories
├── model/ # Entities
├── dto/ # DTOs for request/response
├── client/ # GitHub API client
├── config/ # WebClient config
└── exception/ # Exception handling
```
---

## ⚡ Endpoints

### 1. Fetch and store repositories
**POST** `/github/search`

**Request body:**
```json
{
  "query": "spring boot",
  "language": "Java",
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
**GET** `/github/repositories?language=Java&minStars=100&sort=stars`

**Response:**
```json
{
  "repositories": [
    {
      "id": 123,
      "name": "spring-boot",
      "owner": "spring-projects",
      "language": "Java",
      "stars": 67000,
      "forks": 30000,
      "lastUpdated": "2024-08-18T12:00:00Z"
    }
  ]
}
```
### 🛠️ Setup & Run
### Prerequisites

- JDK 21+
- Maven 3.9+
- Docker (for PostgreSQL)

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
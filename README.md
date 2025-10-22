# 📚 CodeScout

A Spring Boot backend service that integrates with the GitHub API to search repositories, persist them locally, and expose APIs for querying stored repositories with filters and sorting.

---

## ⚡ Features
- 🔎 **Search GitHub repositories** by keyword and language  
- 💾 **Store repositories** in a relational database  
- 📂 **Retrieve stored repositories** with filters:  
  - Language  
  - Minimum Stars  
  - Sort by: stars, forks, updated  
- 🚨 **Error handling** with clear JSON responses (rate limits, invalid requests)  
- ✅ **Tests included** for controllers and services  

---

## 🛠️ Tech Stack
- Java 21
- Spring Boot 3.x
- Spring Web (REST APIs)
- Spring Data JPA + PostgreSQL
- WebClient (for GitHub API calls)
- Global Exception Handling
- JUnit 5 & Mockito for testing

---

## ▶️ Getting Started  

### 1. Clone and Build
```bash
git clone https://github.com/annubelgaonkar/GithubRepoSearcher.git
cd GithubRepoSearcher
mvn clean package
```

### 2. Run
```bash
mvn spring-boot:run
```
Or run the built JAR:
```bash
java -jar target/github-repo-searcher-0.0.1-SNAPSHOT.jar
```

The service starts on:  
👉 `http://localhost:8081`

---

## 📌 API Endpoints  

### 1. Search GitHub Repositories  
`POST /api/github/search`  

**Request Body**
```json
{
  "query": "spring boot",
  "language": "java",
  "sort": "stars"
}
```

**Response**
```json
{
  "repositories": [
    {
      "id": 123456,
      "name": "spring-boot-example",
      "description": "Demo repo",
      "owner": "user123",
      "language": "Java",
      "stars": 150,
      "forks": 30,
      "lastUpdated": "2024-08-19T10:12:45Z"
    }
  ]
}
```

---

### 2. Get Stored Repositories  
`GET /api/github/repositories?language=Java&minStars=100&sort=stars`  

**Response**
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

---

### 3. Example Error Response (Rate Limit)  
```json
{
  "success": false,
  "message": "GitHub rate limit exceeded. Retry after 60 seconds",
  "status": 429,
  "timestamp": "2024-08-19T11:00:00"
}
```

---

## 🧪 Running Tests
```bash
mvn test
```

- **Controller tests** → validate API endpoints using `MockMvc`.  
- **Service tests** → cover persistence, API calls, and exception handling.  

---

## 📈 Improvements (Future Scope)
- Add authentication for endpoints.  
- Implement retry/backoff when GitHub rate limit is hit.  
- Add Swagger/OpenAPI documentation.  

👤 Author

Anuradha Belgaonkar — Backend Developer (Java, Spring Boot, Microservices)
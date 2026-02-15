# GameLog Backend

A backend Ktor service inspired by Letterboxd for tracking and reviewing video games.

This project focuses on clean architecture, structured error handling, validation, and production-oriented backend design.

## Tech Stack
- Kotlin
- Ktor (Netty)
- PostgreSQL
- Exposed ORM
- HikariCP
- Koin (DI)
- Konform (validation)
- Bcrypt (password hashing)
- OpenAPI + Swagger UI
- Kotest
- Testcontainers
---
## Setup
1. Clone the repository 
```bash 
git clone https://github.com/carlawarde/kotlin-backend-demo.git
```
2. Create a local `.env` file with the following variables:
```env
SERVICE_NAME=kotlin_backend_demo
ENVIRONMENT=local
REGION=local
SERVICE_INSTANCE=localhost

APP_PORT=8080

DB_TYPE=postgresql
DB_HOST=db
DB_PORT=5432
DB_NAME=demo_db

DB_USER=[USERNAME]
DB_PASSWORD=[PASSWORD]
```
3. Run the application:
   1. **Gradle**
   
      ```bash
      ./gradlew run
      ```
      Runs the application locally without provisioning a database.<br>
      Core routes will return `503 Service Unavailable` until a database becomes available.
   2. **Docker Compose**

      ```bash
      ./scripts/buildAndRunApp.sh
      ```

      This will start:
      
      - The application container
       - A PostgreSQL container
      
      The application will automatically connect and run migrations.

---
## Features
### User Management
- [x] User registration 
- [x] DTO validation using Konform 
- [x] Password hashing with Bcrypt 
- [x] Unique username/email enforcement (DB constraint mapping)
- [x] Structured JSON error responses (StatusPages)
- [ ] Login 
- [ ] Session-based authentication 
- [ ] Session expiration 
- [ ] Session-based authorisation
- [ ] Logout

### Game Logging
- [ ] Create game entity
- [ ] Log played games
- [ ] Ratings
- [ ] Reviews
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

## Setup
1. Clone the repository: ``git clone https://github.com/carlawarde/kotlin-backend-demo.git``
2. Create a local `.env` file with the following variables:
```
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
3. You now have two ways to run the application:
   1. Gradle: ``gradlew build && gradlew run``
   2. DockerCompose via some helper scripts: ``./scripts/buildAndRunApp.sh``. 
      Please note this will create two Docker containers: One for the application and one for the Postgresql database.

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
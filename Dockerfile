FROM ubuntu:latest
LABEL authors="carla.warde"

# =========================
# Build stage
# =========================
FROM gradle:9.3.0-jdk17 AS build

WORKDIR /app
COPY . .

RUN gradle :server:installDist --no-daemon

# =========================
# Runtime stage
# =========================
FROM eclipse-temurin:17-jre
WORKDIR /app

COPY --from=build /app/server/build/install/kotlin-backend-demo /app/

RUN chmod +x bin/kotlin-backend-demo

EXPOSE 8080

ENV JAVA_OPTS=""

ENTRYPOINT ["sh", "-c", "./bin/kotlin-backend-demo $JAVA_OPTS"]
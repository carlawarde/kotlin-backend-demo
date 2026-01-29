FROM ubuntu:latest
LABEL authors="carla.warde"

# =========================
# Build stage (Shadow JAR)
# =========================
FROM gradle:9.3.0-jdk17 AS build

WORKDIR /app

COPY . .

# Build the fat JAR using shadowJar
ARG APP_VERSION=1.0.0-SNAPSHOT
RUN gradle clean shadowJar -Pversion=$APP_VERSION --no-daemon --console=plain

# =========================
# Runtime stage
# =========================
FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

# Copy the fat JAR from build stage
ARG APP_VERSION=1.0.0-SNAPSHOT
COPY --from=build /app/server/build/libs/server-$APP_VERSION.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
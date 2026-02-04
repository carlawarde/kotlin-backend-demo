#!/usr/bin/env bash
set -euo pipefail

APP_VERSION="${APP_VERSION:-1.0.0-SNAPSHOT}"
COMPOSE_FILE="docker-compose.yml"

if [[ "${1:-}" == "down" ]]; then
    echo "Stopping and removing containers..."
    docker-compose -f "$COMPOSE_FILE" down
    exit 0
fi

echo "Building Kotlin app Docker image with version: $APP_VERSION"
docker build \
    --build-arg APP_VERSION="$APP_VERSION" \
    -t kotlin-backend-demo:"$APP_VERSION" .

echo "Starting app and database with Docker Compose..."
docker-compose -f "$COMPOSE_FILE" up -d --build --remove-orphans

echo "App should be available at: http://localhost:${APP_PORT:-8080}"
echo "Tailing logs (press Ctrl+C to stop)"

trap "echo 'Stopping log tail'; exit" SIGINT
docker-compose -f "$COMPOSE_FILE" logs -f



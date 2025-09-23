#!/bin/bash
set -e

echo "Starting Tasktrix Application..."
echo "Active Profile: ${SPRING_PROFILES_ACTIVE:-docker}"
echo "Database URL: ${DATABASE_URL:-jdbc:postgresql://postgres:5432/trix}"

# Wait for PostgreSQL to be ready
echo "Waiting for PostgreSQL to be ready..."
while ! nc -z postgres 5432; do
    echo "Waiting for PostgreSQL..."
    sleep 2
done
echo "PostgreSQL is ready!"

# Start the Spring Boot application
exec java ${JAVA_OPTS:--Xmx512m -Xms256m} org.springframework.boot.loader.launch.JarLauncher
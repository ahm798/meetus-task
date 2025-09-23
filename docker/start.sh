#!/bin/bash
set -e

echo "Starting Spring Boot Application..."
echo "Active Profile: ${SPRING_PROFILES_ACTIVE:-docker}"
echo "Java Options: ${JAVA_OPTS}"
echo "Database URL: ${DATABASE_URL:-jdbc:postgresql://postgres:5432/springapp}"

# Start the Spring Boot application
exec java ${JAVA_OPTS} -jar app.jar
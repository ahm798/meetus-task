
FROM eclipse-temurin:24-jdk-alpine AS builder

WORKDIR /app

COPY .mvn/ .mvn/
COPY mvnw pom.xml ./

RUN chmod +x ./mvnw

RUN ./mvnw dependency:go-offline -B

COPY src ./src

RUN ./mvnw clean package -DskipTests -B

FROM eclipse-temurin:24-jdk-alpine AS runtime

WORKDIR /app

RUN apk add --no-cache \
    curl \
    netcat-openbsd \
    && rm -rf /var/cache/apk/*

RUN addgroup -g 1001 -S appgroup && \
    adduser -u 1001 -S appuser -G appgroup && \
    mkdir -p /app/logs && \
    chown -R appuser:appgroup /app

COPY --from=builder --chown=appuser:appgroup /app/target/*.jar /app/app.jar

USER appuser

EXPOSE 8080

CMD ["sh", "-c", "echo 'Waiting for PostgreSQL...' && while ! nc -z postgres 5432; do sleep 2; done && echo 'PostgreSQL is ready!' && java ${JAVA_OPTS:--Xmx512m -Xms256m} -jar /app/app.jar"]
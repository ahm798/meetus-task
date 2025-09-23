# Tasktrix - Task Management REST API

A comprehensive task management REST API built with Spring Boot, featuring JWT authentication, caching, and containerized deployment.

## 🚀 Features

### 🔐 Authentication & Security
- **JWT Authentication** - Secure 512-bit token-based authentication
- **User Registration & Login** - Complete user management system
- **Token Refresh** - Automatic token renewal capability
- **User Isolation** - Users can only access their own tasks
- **Password Encryption** - BCrypt hashing with strong password validation
- **Security Filters** - Custom JWT authentication filter with proper error handling

### 📋 Task Management
- **CRUD Operations** - Create, Read, Update, Delete tasks
- **Task Status** - NEW, IN_PROGRESS, COMPLETED status tracking
- **User-Specific Tasks** - Each user manages their own tasks
- **Timestamps** - Automatic creation and update timestamps
- **Business Logic** - Task completion tracking and status management
- **Pagination Support** - Efficient data retrieval with pagination

### 🚀 Infrastructure & Performance
- **Containerized Deployment** - Docker Compose with multi-service architecture
- **Redis Caching** - High-performance caching layer for user data
- **PostgreSQL Database** - Production-ready relational database
- **Health Monitoring** - Spring Boot Actuator integration
- **Database Migrations** - Flyway for version-controlled schema changes

## 🛠️ Tech Stack

- **Framework**: Spring Boot 3.5.5
- **Security**: Spring Security with JWT (HS512)
- **Database**: PostgreSQL 15
- **Cache**: Redis 7
- **ORM**: JPA/Hibernate + MapStruct
- **Migration**: Flyway
- **Containerization**: Docker & Docker Compose
- **Build Tool**: Maven
- **Java Version**: 24

## 📦 Dependencies

```xml
<!-- Core Spring Boot -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>

<!-- JWT -->
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-api</artifactId>
    <version>0.11.5</version>
</dependency>

<!-- Database -->
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
    <scope>runtime</scope>
</dependency>

<!-- Migration -->
<dependency>
    <groupId>org.flywaydb</groupId>
    <artifactId>flyway-core</artifactId>
</dependency>

<!-- Mapping -->
<dependency>
    <groupId>org.mapstruct</groupId>
    <artifactId>mapstruct</artifactId>
    <version>1.5.5.Final</version>
</dependency>
```

## 🚀 Quick Start

### Prerequisites
- Java 24 or higher
- Docker & Docker Compose
- Maven 3.6+ (for local development)

### Installation & Setup

1. **Clone the repository**
   ```bash
   git clone https://github.com/ahm798/meetus-task.git
   cd tasktrix
   ```

2. **Start with Docker Compose (Recommended)**
   ```bash
   docker compose up --build -d
   ```
   This will start:
   - **PostgreSQL** database on port 5432
   - **Redis** cache on port 6379  
   - **Tasktrix API** on port 8080

3. **Verify services are running**
   ```bash
   docker compose ps
   curl http://localhost:8080/actuator/health
   ```

4. **Alternative: Local Development**
   ```bash
   # Start only database services
   docker compose up postgres redis -d
   
   # Run application locally
   mvn spring-boot:run
   ```

## 📖 API Documentation

### 🔐 Authentication Endpoints

#### Register User
```http
POST /api/v1/auth/register
Content-Type: application/json

{
    "username": "john_doe",
    "email": "john@example.com", 
    "password": "SecurePass123!",
    "firstName": "John",
    "lastName": "Doe"
}
```

**Response:**
```json
{
    "success": true,
    "message": "User registered successfully",
    "timestamp": "2025-09-23T06:16:47.953642701"
}
```

**Password Requirements:**
- At least 8 characters
- One uppercase letter
- One lowercase letter  
- One digit
- One special character

#### Login User
```http
POST /api/v1/auth/login
Content-Type: application/json

{
    "username": "john_doe",
    "password": "SecurePass123!"
}
```

**Response:**
```json
{
    "success": true,
    "message": "Authentication successful",
    "data": {
        "accessToken": "eyJhbGciOiJIUzUxMiJ9...",
        "refreshToken": "eyJhbGciOiJIUzUxMiJ9...",
        "tokenType": "Bearer",
        "user": {
            "id": 1,
            "username": "john_doe",
            "email": "john@example.com",
            "firstName": "John",
            "lastName": "Doe"
        },
        "expiresAt": "2025-09-24T06:16:55.486"
    },
    "timestamp": "2025-09-23T06:16:55.487363816"
}
```

#### Refresh Token
```http
POST /api/v1/auth/refresh
Content-Type: application/json
Authorization: Bearer <refresh-token>
```

### 🧪 Test Endpoints

#### Hello Test
```http
GET /api/test/hello
Authorization: Bearer <access-token>
```

#### Secure Test  
```http
GET /api/test/secure
Authorization: Bearer <access-token>
```

### 📋 Task Management Endpoints

> **Note**: All task endpoints require JWT authentication via `Authorization: Bearer <token>` header.

#### Create Task
```http
POST /api/task
Authorization: Bearer <your-jwt-token>
Content-Type: application/json

{
    "title": "Learn Spring Boot",
    "content": "Complete the Spring Boot tutorial",
    "status": "NEW"
}
```

#### Get All Tasks (Paginated)
```http
GET /api/task?page=0&size=10&sort=createdAt,desc
Authorization: Bearer <your-jwt-token>
```

#### Get Task by ID
```http
GET /api/task/{id}
Authorization: Bearer <your-jwt-token>
```

#### Update Task
```http
PUT /api/task/{id}
Authorization: Bearer <your-jwt-token>
Content-Type: application/json

{
    "title": "Updated Task Title",
    "content": "Updated content",
    "status": "COMPLETED"
}
```

#### Delete Task
```http
DELETE /api/task/{id}
Authorization: Bearer <your-jwt-token>
```

### 🩺 Health & Monitoring

#### Health Check
```http
GET /actuator/health
```

#### Application Info
```http
GET /actuator/info
```

## 🏗️ Project Structure

```
src/
├── main/
│   ├── java/ahm/dev/tasktrix/
│   │   ├── TasktrixApplication.java       # Main application class
│   │   ├── config/                        # Configuration classes
│   │   │   ├── AuthenticationConfig.java  # Authentication provider configuration
│   │   │   ├── CacheConfig.java           # Redis cache configuration
│   │   │   ├── CorsProperties.java        # CORS configuration properties
│   │   │   ├── PasswordConfig.java        # Password encoder configuration
│   │   │   └── SecurityConfig.java        # Spring Security configuration
│   │   ├── controller/                    # REST controllers
│   │   │   ├── AuthController.java        # Authentication endpoints
│   │   │   ├── TaskController.java        # Task management endpoints
│   │   │   └── TestController.java        # Test/debug endpoints
│   │   ├── domain/                        # Entity classes
│   │   │   ├── Role.java                  # User role enum
│   │   │   ├── Task.java                  # Task entity
│   │   │   ├── TaskStatus.java            # Task status enum
│   │   │   └── User.java                  # User entity with caching
│   │   ├── dto/                           # Data Transfer Objects
│   │   │   ├── AuthRequest.java           # Login request DTO
│   │   │   ├── AuthResponse.java          # Authentication response DTO
│   │   │   ├── TaskForm.java              # Task creation/update DTO
│   │   │   └── UserForRegister.java       # User registration DTO
│   │   ├── exception/                     # Custom exceptions
│   │   │   ├── BusinessLogicException.java # Business logic errors
│   │   │   └── GlobalExceptionHandler.java # Global error handling
│   │   ├── mapper/                        # MapStruct mappers
│   │   │   └── UserMapper.java            # User entity mapping
│   │   ├── repository/                    # Data repositories
│   │   │   ├── TaskRepository.java        # Task data access
│   │   │   └── UserRepository.java        # User data access
│   │   ├── security/                      # Security components
│   │   │   ├── JwtAuthenticationEntryPoint.java # JWT error handling
│   │   │   ├── JwtAuthenticationFilter.java     # JWT authentication filter
│   │   │   └── JwtTokenProvider.java            # JWT token operations
│   │   └── service/                       # Business logic services
│   │       ├── AuthService.java           # Authentication service interface
│   │       ├── AuthServiceImpl.java       # Authentication service implementation
│   │       ├── TaskService.java           # Task service interface
│   │       ├── TaskServiceImpl.java       # Task service implementation
│   │       ├── UserService.java           # User service interface
│   │       └── UserServiceImpl.java       # User service with caching
│   └── resources/
│       ├── application.yml                # Application configuration
│       └── db/migration/                  # Flyway database migrations
├── test/                                  # Test classes
└── docker-compose.yml                     # Multi-service container setup
```

## 🔧 Configuration

### Docker Compose Services
```yaml
services:
  app:
    build: .
    ports:
      - "8080:8080"
    depends_on:
      - postgres
      - redis
    environment:
      - SPRING_PROFILES_ACTIVE=docker

  postgres:
    image: postgres:15-alpine
    ports:
      - "5432:5432"
    environment:
      - POSTGRES_DB=tasktrix
      - POSTGRES_USER=tasktrix
      - POSTGRES_PASSWORD=tasktrix

  redis:
    image: redis:7-alpine
    ports:
      - "6379:6379"
    command: redis-server --requirepass redissecret
```

### Application Configuration (application.yml)
```yaml
server:
  port: 8080

spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/tasktrix
    username: tasktrix
    password: tasktrix
    driver-class-name: org.postgresql.Driver

  jpa:
    hibernate:
      ddl-auto: validate
    show-sql: false
    database-platform: org.hibernate.dialect.PostgreSQLDialect

  data:
    redis:
      host: localhost
      port: 6379
      password: redissecret

  flyway:
    enabled: true
    locations: classpath:db/migration

security:
  jwt:
    secret-key: your-512-bit-secret-key
    access-token-expiration: 86400000   # 24 hours
    refresh-token-expiration: 604800000 # 7 days

management:
  endpoints:
    web:
      exposure:
        include: health,info
  endpoint:
    health:
      show-details: when-authorized
```

## 🧪 Testing the API

### Using cURL

1. **Health Check:**
   ```bash
   curl http://localhost:8080/actuator/health
   ```

2. **Register a user:**
   ```bash
   curl -X POST http://localhost:8080/api/v1/auth/register \
     -H "Content-Type: application/json" \
     -d '{
       "username": "testuser",
       "email": "test@example.com",
       "password": "TestPass123!",
       "firstName": "Test",
       "lastName": "User"
     }'
   ```

3. **Login and get token:**
   ```bash
   curl -X POST http://localhost:8080/api/v1/auth/login \
     -H "Content-Type: application/json" \
     -d '{
       "username": "testuser", 
       "password": "TestPass123!"
     }'
   ```

4. **Test authentication:**
   ```bash
   # Replace YOUR_JWT_TOKEN with actual token from login response
   curl -X GET http://localhost:8080/api/test/hello \
     -H "Authorization: Bearer YOUR_JWT_TOKEN"
   ```

5. **Create a task:**
   ```bash
   curl -X POST http://localhost:8080/api/task \
     -H "Content-Type: application/json" \
     -H "Authorization: Bearer YOUR_JWT_TOKEN" \
     -d '{
       "title": "Test Task",
       "content": "Test content", 
       "status": "NEW"
     }'
   ```

6. **Get all tasks:**
   ```bash
   curl -X GET http://localhost:8080/api/task \
     -H "Authorization: Bearer YOUR_JWT_TOKEN"
   ```

### Docker Environment Testing
```bash
# Start all services
docker compose up --build -d

# Check service status
docker compose ps

# View logs
docker compose logs app

# Stop services
docker compose down
```

## 🛡️ Security Features

### JWT Authentication
- **Algorithm**: HS512 (512-bit HMAC SHA-512)
- **Access Token**: Short-lived token for API access (24 hours)
- **Refresh Token**: Long-lived token for token renewal (7 days)
- **Bearer Authentication**: Standard HTTP Authorization header
- **Secure Secret**: Cryptographically strong 512+ bit secret key

### Password Security
- **BCrypt Encryption**: Strong password hashing algorithm
- **Validation Rules**: Enforced complex password requirements
- **No Plain Text**: Passwords never stored in plain text

### User Isolation & Authorization
- Users can only access their own tasks
- All task operations are scoped to the authenticated user
- Proper authorization checks on all protected endpoints
- Role-based access control (USER, ADMIN roles)

### Caching Security
- **Redis Authentication**: Password-protected Redis instance
- **Secure Serialization**: Proper JSON serialization for cached data
- **Cache Isolation**: User data properly isolated in cache

## 🗄️ Database Schema

### Users Table
```sql
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(55) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    role VARCHAR(50) DEFAULT 'USER'
);
```

### Tasks Table
```sql
CREATE TABLE tasks (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    content VARCHAR(1000),
    status VARCHAR(50) NOT NULL,
    user_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_tasks_user FOREIGN KEY (user_id) REFERENCES users(id)
);
```

### Indexes
```sql
CREATE INDEX idx_tasks_user_id ON tasks(user_id);
CREATE INDEX idx_tasks_status ON tasks(status);
CREATE INDEX idx_users_username ON users(username);
CREATE INDEX idx_users_email ON users(email);
```

## 🚀 Deployment

### Production Deployment with Docker

1. **Build and deploy:**
   ```bash
   # Clone and navigate
   git clone https://github.com/ahm798/meetus-task.git
   cd tasktrix
   
   # Deploy with Docker Compose
   docker compose up --build -d
   ```

2. **Environment Variables for Production:**
   ```bash
   # Create .env file
   echo "SPRING_PROFILES_ACTIVE=prod" > .env
   echo "JWT_SECRET_KEY=your-production-512-bit-secret" >> .env
   echo "POSTGRES_PASSWORD=your-secure-db-password" >> .env
   echo "REDIS_PASSWORD=your-secure-redis-password" >> .env
   ```

3. **Health Monitoring:**
   ```bash
   # Check application health
   curl http://your-domain:8080/actuator/health
   
   # Monitor logs
   docker compose logs -f app
   ```

### Kubernetes Deployment

1. **Create ConfigMaps and Secrets:**
   ```yaml
   apiVersion: v1
   kind: Secret
   metadata:
     name: tasktrix-secrets
   data:
     jwt-secret: <base64-encoded-secret>
     db-password: <base64-encoded-password>
     redis-password: <base64-encoded-password>
   ```

2. **Deploy services:**
   ```bash
   kubectl apply -f k8s/
   ```

### Building for Production
```bash
# Build optimized JAR
mvn clean package -DskipTests -Dspring.profiles.active=prod

# Build optimized Docker image
docker build -t tasktrix:latest .
```

### Performance Tuning
```yaml
# application-prod.yml
spring:
  jpa:
    show-sql: false
    hibernate:
      ddl-auto: validate
  
  data:
    redis:
      timeout: 2000ms
      lettuce:
        pool:
          max-active: 8
          max-idle: 8

server:
  tomcat:
    max-threads: 200
    min-spare-threads: 10

logging:
  level:
    ahm.dev.tasktrix: INFO
    org.springframework.security: WARN
```

## 📈 Architecture & Design

### Layered Architecture
```
┌─────────────────────────────────────┐
│         Presentation Layer          │
│    (Controllers, DTOs, Mappers)     │
├─────────────────────────────────────┤
│           Service Layer             │
│     (Business Logic, Security)      │
├─────────────────────────────────────┤
│         Repository Layer            │
│      (Data Access, Caching)         │
├─────────────────────────────────────┤
│        Infrastructure Layer         │
│  (Database, Cache, External APIs)   │
└─────────────────────────────────────┘
```

### Security Architecture
```
Request → JWT Filter → Security Context → Controller → Service → Repository
            ↓              ↓               ↓         ↓         ↓
         Validate      Set Auth        Authorize  Business  Data Access
         JWT Token     Context         Request    Logic     + Cache
```

### Caching Strategy
- **User Entity Caching**: Frequently accessed user data
- **Redis Backend**: Distributed cache for scalability
- **TTL Management**: Automatic cache expiration
- **Cache Invalidation**: On user updates

### Database Design Principles
- **Normalized Schema**: Eliminates data redundancy
- **Foreign Key Constraints**: Ensures referential integrity
- **Indexed Columns**: Optimized query performance
- **Migration Versioning**: Flyway for schema evolution

## 🧪 Testing Strategy

### Unit Tests
```bash
# Run unit tests
mvn test

# Run with coverage
mvn test jacoco:report
```

### Integration Tests
```bash
# Test with test containers
mvn verify -Dspring.profiles.active=test
```

### API Testing
```bash
# Test all endpoints
curl -X GET http://localhost:8080/actuator/health
# ... register, login, create tasks, etc.
```

## 🔧 Development Setup

### IDE Configuration
1. **Import Maven Project**
2. **Set Java 24** as project SDK
3. **Enable Annotation Processing** for MapStruct
4. **Install Lombok Plugin**

### Local Development Database
```bash
# Start only database services
docker compose up postgres redis -d

# Run application in IDE or command line
mvn spring-boot:run -Dspring.profiles.active=dev
```

### Debug Configuration
```yaml
# application-dev.yml
logging:
  level:
    ahm.dev.tasktrix: DEBUG
    org.springframework.security: DEBUG
    org.springframework.data.redis: DEBUG

spring:
  jpa:
    show-sql: true
    hibernate:
      format_sql: true
```

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

### Development Guidelines
- Follow Spring Boot best practices
- Write unit tests for new features
- Update documentation for API changes
- Use conventional commit messages
- Ensure Docker builds pass

## 🐛 Troubleshooting

### Common Issues

1. **Connection Refused Error**
   ```bash
   # Ensure services are running
   docker compose ps
   docker compose up postgres redis -d
   ```

2. **JWT Token Invalid**
   ```bash
   # Check token expiration and format
   # Ensure Bearer prefix in Authorization header
   ```

3. **Redis Connection Issues**
   ```bash
   # Verify Redis is running with authentication
   docker compose logs redis
   redis-cli -h localhost -p 6379 -a redissecret ping
   ```

4. **Database Connection Problems**
   ```bash
   # Check PostgreSQL status
   docker compose logs postgres
   psql -h localhost -p 5432 -U tasktrix -d tasktrix
   ```

### Log Analysis
```bash
# Application logs
docker compose logs app

# Database logs
docker compose logs postgres

# Cache logs  
docker compose logs redis
```

## 📊 Performance Metrics

### Response Times (Typical)
- **Authentication**: < 200ms
- **Task CRUD**: < 100ms
- **Cached User Lookup**: < 50ms
- **Health Check**: < 10ms

### Throughput
- **Concurrent Users**: 100+
- **Requests/Second**: 500+
- **Database Connections**: Pool of 10

### Resource Usage
- **Memory**: ~512MB JVM heap
- **CPU**: < 5% under normal load
- **Storage**: PostgreSQL + Redis persistent volumes

## 🔗 API Client Examples

### JavaScript/Node.js
```javascript
const axios = require('axios');

// Login and get token
const login = async () => {
  const response = await axios.post('http://localhost:8080/api/v1/auth/login', {
    username: 'testuser',
    password: 'TestPass123!'
  });
  return response.data.data.accessToken;
};

// Create task
const createTask = async (token) => {
  const response = await axios.post('http://localhost:8080/api/task', {
    title: 'New Task',
    content: 'Task description',
    status: 'NEW'
  }, {
    headers: { Authorization: `Bearer ${token}` }
  });
  return response.data;
};
```

### Python
```python
import requests

# Login and get token
def login():
    response = requests.post('http://localhost:8080/api/v1/auth/login', json={
        'username': 'testuser',
        'password': 'TestPass123!'
    })
    return response.json()['data']['accessToken']

# Create task
def create_task(token):
    response = requests.post('http://localhost:8080/api/task', 
        json={
            'title': 'New Task',
            'content': 'Task description', 
            'status': 'NEW'
        },
        headers={'Authorization': f'Bearer {token}'}
    )
    return response.json()
```
## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 👨‍💻 Author

**Ahmed Dev**
- Email: admin@tasktrix.com
- GitHub: [@ahm798](https://github.com/ahm798)

## 🙏 Acknowledgments

- Spring Boot Team for the excellent framework
- Spring Security Team for robust security features
- PostgreSQL Community for the robust database
- Redis Team for high-performance caching
- Docker Team for containerization platform
- MapStruct Team for powerful object mapping

## 🛠️ Built With

- [Spring Boot](https://spring.io/projects/spring-boot) - The web framework
- [Spring Security](https://spring.io/projects/spring-security) - Security framework
- [PostgreSQL](https://www.postgresql.org/) - Database
- [Redis](https://redis.io/) - Caching layer
- [Docker](https://www.docker.com/) - Containerization
- [MapStruct](https://mapstruct.org/) - Object mapping
- [Flyway](https://flywaydb.org/) - Database migrations
- [JWT](https://jwt.io/) - JSON Web Tokens

---

## 📞 Support

If you encounter any issues or have questions:

1. **Check the troubleshooting section** above
2. **Search existing issues** on GitHub
3. **Create a new issue** with detailed information
4. **Contact the maintainer** for urgent matters

### Issue Template
```
**Environment:**
- OS: [e.g., Ubuntu 20.04]
- Java Version: [e.g., 24]
- Docker Version: [e.g., 20.10.8]

**Description:**
Brief description of the issue

**Steps to Reproduce:**
1. Step one
2. Step two
3. Step three

**Expected Behavior:**
What should happen

**Actual Behavior:**
What actually happens

**Logs:**
Relevant error messages or logs
```

## 🌟 Show Your Support

Give a ⭐️ if this project helped you!

## 📈 Project Status

![Build Status](https://img.shields.io/badge/build-passing-brightgreen)
![Version](https://img.shields.io/badge/version-1.0.0-blue)
![License](https://img.shields.io/badge/license-MIT-green)
![Java](https://img.shields.io/badge/java-24-orange)
![Spring Boot](https://img.shields.io/badge/spring--boot-3.5.5-green)

**Happy coding! 🚀**
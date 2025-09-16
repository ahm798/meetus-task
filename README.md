# Tasktrix - Task Management REST API

A comprehensive task management REST API built with Spring Boot, featuring JWT authentication, user isolation, and interactive Swagger documentation.

## 🚀 Features

### 🔐 Authentication & Security
- **JWT Authentication** - Secure token-based authentication
- **User Registration & Login** - Complete user management system
- **Token Refresh** - Automatic token renewal capability
- **User Isolation** - Users can only access their own tasks
- **Password Encryption** - BCrypt hashing for secure password storage

### 📋 Task Management
- **CRUD Operations** - Create, Read, Update, Delete tasks
- **Task Status** - NEW, IN_PROGRESS, COMPLETED status tracking
- **User-Specific Tasks** - Each user manages their own tasks
- **Timestamps** - Automatic creation and update timestamps
- **Business Logic** - Task completion tracking and status management

### 📚 API Documentation
- **Swagger UI** - Interactive API documentation and testing
- **OpenAPI 3.0** - Complete API specification
- **Request/Response Examples** - Comprehensive examples for all endpoints
- **Authentication Flow** - Step-by-step JWT usage guide

## 🛠️ Tech Stack

- **Framework**: Spring Boot 3.5.5
- **Security**: Spring Security with JWT
- **Database**: H2 (in-memory for development)
- **ORM**: JPA/Hibernate
- **Documentation**: SpringDoc OpenAPI (Swagger)
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

<!-- JWT -->
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-api</artifactId>
    <version>0.11.5</version>
</dependency>

<!-- Swagger Documentation -->
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.2.0</version>
</dependency>

<!-- Database -->
<dependency>
    <groupId>com.h2database</groupId>
    <artifactId>h2</artifactId>
    <scope>runtime</scope>
</dependency>
```

## 🚀 Quick Start

### Prerequisites
- Java 24 or higher
- Maven 3.6+

### Installation & Setup

1. **Clone the repository**
   ```bash
   git clone https://github.com/ahm798/meetus-task.git
   cd tasktrix
   ```

2. **Build the project**
   ```bash
   mvn clean package
   ```

3. **Run the application**
   ```bash
   java -jar target/tasktrix-0.0.1-SNAPSHOT.jar
   ```

4. **Access the application**
   - **API Base URL**: `http://localhost:8080`
   - **Swagger UI**: `http://localhost:8080/swagger-ui/index.html`
   - **API Docs**: `http://localhost:8080/v3/api-docs`

## 📖 API Documentation

### 🔐 Authentication Endpoints

#### Register User
```http
POST /api/auth/register
Content-Type: application/json

{
    "username": "john_doe",
    "email": "john@example.com", 
    "password": "password123",
    "firstName": "John",
    "lastName": "Doe"
}
```

#### Login User
```http
POST /api/auth/login
Content-Type: application/json

{
    "username": "john_doe",
    "password": "password123"
}
```

**Response:**
```json
{
    "accessToken": "eyJhbGciOiJIUzI1NiJ9...",
    "refreshToken": "eyJhbGciOiJIUzI1NiJ9...",
    "expiresIn": 86400000
}
```

#### Refresh Token
```http
POST /api/auth/refresh
Content-Type: text/plain

eyJhbGciOiJIUzI1NiJ9...
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

#### Get All Tasks
```http
GET /api/task
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

## 🏗️ Project Structure

```
src/
├── main/
│   ├── java/ahm/dev/tasktrix/
│   │   ├── TasktrixApplication.java       # Main application class
│   │   ├── config/                        # Configuration classes
│   │   │   ├── AuthenticationConfig.java  # Authentication configuration
│   │   │   ├── PasswordConfig.java        # Password encoder configuration
│   │   │   ├── SecurityConfig.java        # Spring Security configuration
│   │   │   └── SwaggerConfig.java         # Swagger/OpenAPI configuration
│   │   ├── controller/                    # REST controllers
│   │   │   ├── AuthController.java        # Authentication endpoints
│   │   │   └── TaskController.java        # Task management endpoints
│   │   ├── domain/                        # Entity classes
│   │   │   ├── Role.java                  # User role enum
│   │   │   ├── Task.java                  # Task entity
│   │   │   ├── TaskStatus.java            # Task status enum
│   │   │   └── User.java                  # User entity
│   │   ├── dto/                           # Data Transfer Objects
│   │   │   ├── AuthRequest.java           # Login request DTO
│   │   │   ├── AuthResponse.java          # Authentication response DTO
│   │   │   ├── TaskForm.java              # Task creation/update DTO
│   │   │   └── UserForRegister.java       # User registration DTO
│   │   ├── filter/                        # Security filters
│   │   │   └── JwtAuthenticationFilter.java # JWT authentication filter
│   │   ├── repository/                    # Data repositories
│   │   │   ├── TaskRepository.java        # Task data access
│   │   │   └── UserRepository.java        # User data access
│   │   └── service/                       # Business logic services
│   │       ├── AuthService.java           # Authentication service interface
│   │       ├── AuthServiceImpl.java       # Authentication service implementation
│   │       ├── JwtService.java            # JWT service interface
│   │       ├── JwtServiceImpl.java        # JWT service implementation
│   │       ├── TaskService.java           # Task service interface
│   │       ├── TaskServiceImpl.java       # Task service implementation
│   │       ├── UserService.java           # User service interface
│   │       └── UserServiceImpl.java       # User service implementation
│   └── resources/
│       └── application.properties         # Application configuration
└── test/                                  # Test classes
```

## 🔧 Configuration

### Application Properties
```properties
# Server Configuration
server.port=8080

# Database Configuration (H2)
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

# JPA Configuration
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=true

# H2 Console (Development only)
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console

# JWT Configuration
jwt.secret=mySecretKey
jwt.access-token-expiration=86400000
jwt.refresh-token-expiration=604800000
```

## 🧪 Testing the API

### Using cURL

1. **Register a user:**
   ```bash
   curl -X POST http://localhost:8080/api/auth/register \
     -H "Content-Type: application/json" \
     -d '{"username": "testuser", "password": "password123", "email": "test@example.com"}'
   ```

2. **Login and get token:**
   ```bash
   curl -X POST http://localhost:8080/api/auth/login \
     -H "Content-Type: application/json" \
     -d '{"username": "testuser", "password": "password123"}'
   ```

3. **Create a task:**
   ```bash
   curl -X POST http://localhost:8080/api/task \
     -H "Content-Type: application/json" \
     -H "Authorization: Bearer YOUR_JWT_TOKEN" \
     -d '{"title": "Test Task", "content": "Test content", "status": "NEW"}'
   ```

### Using Swagger UI

1. Open `http://localhost:8080/swagger-ui/index.html`
2. Use the "Authorize" button to add your JWT token
3. Test all endpoints interactively

## 🛡️ Security Features

### JWT Authentication
- **Access Token**: Short-lived token for API access (24 hours)
- **Refresh Token**: Long-lived token for token renewal (7 days)
- **Bearer Authentication**: Standard HTTP Authorization header

### User Isolation
- Users can only access their own tasks
- All task operations are scoped to the authenticated user
- Proper authorization checks on all endpoints

### Password Security
- BCrypt encryption for password storage
- No plain text passwords in database

## 🗄️ Database Schema

### Users Table
```sql
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(55) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    role VARCHAR(50)
);
```

### Tasks Table
```sql
CREATE TABLE tasks (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    content VARCHAR(1000),
    status VARCHAR(50) NOT NULL,
    user_id BIGINT NOT NULL,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
);
```

## 🚀 Deployment

### Building for Production
```bash
mvn clean package -DskipTests
```

### Running in Production
```bash
java -jar target/tasktrix-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod
```

### Environment Variables
```bash
export JWT_SECRET=your-production-secret-key
export DATABASE_URL=your-production-database-url
export DATABASE_USERNAME=your-db-username
export DATABASE_PASSWORD=your-db-password
```

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 👨‍💻 Author

**Ahmed Dev**
- Email: admin@tasktrix.com
- GitHub: [@ahm798](https://github.com/ahm798)

## 🙏 Acknowledgments

- Spring Boot Team for the excellent framework
- Spring Security Team for robust security features
- SpringDoc team for seamless OpenAPI integration
- H2 Database for easy development setup

---

## 📞 Support

If you encounter any issues or have questions, please open an issue on GitHub or contact the maintainer.

**Happy coding! 🚀**

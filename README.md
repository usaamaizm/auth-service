# 🔐 Auth Service - JWT Authentication & Role-Based Access Control (RBAC)

This is a production-style authentication service built using **Spring Boot 3**, **JWT**, and **Role-Based Access Control (RBAC)** principles. It supports secure login/signup, microservices communication using tokens, and fine-grained access control via roles and permissions.

---

## ✅ Features

- User registration (signup)
- Secure login with JWT token issuance
- JWT validation via custom filter
- Spring Security context population
- Role and permission-based authorization
- Secure REST APIs with `@PreAuthorize`
- UUID-based user identity
- Liquibase-based schema migration

---

## 🧱 Tech Stack

| Layer         | Technology           |
|---------------|----------------------|
| Language      | Java 17              |
| Framework     | Spring Boot 3        |
| Auth Protocol | JWT (Manual)         |
| Database      | MySQL 8 (Dockerized) |
| Schema Tool   | Liquibase            |
| Build Tool    | Maven                |

---

## 📁 Project Structure (Important Parts)

```
authserver/
├── config/
│   └── SecurityConfig.java
├── controller/
│   ├── AuthController.java
│   └── AdminController.java
├── dto/
│   ├── AuthRequest.java
│   ├── AuthResponse.java
│   ├── SignupRequest.java
│   └── UserPrincipal.java
├── entity/
│   ├── AppUser.java
│   ├── Role.java
│   ├── Permission.java
│   ├── UserRole.java
│   └── RolePermission.java
├── repository/
│   ├── AppUserRepository.java
│   ├── RoleRepository.java
│   └── [other repositories]
├── security/
│   ├── JwtAuthFilter.java
│   └── JwtEntryPoint.java
├── service/
│   ├── AuthService.java
│   └── UserAuthorityService.java
├── util/
│   └── JwtTokenProvider.java
└── resources/
    └── db/changelog/
        └── [Liquibase XMLs]
```

---

## ⚙️ Setup Instructions

### 1. 🚀 Run MySQL with Docker

```bash
docker run --name mysql-auth \
  -e MYSQL_ROOT_PASSWORD=root \
  -e MYSQL_DATABASE=auth_db \
  -p 3306:3306 -d mysql:8.0
```

### 2. 📝 Configure Application Properties

```properties
# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/auth_db
spring.datasource.username=root
spring.datasource.password=root
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# JPA Configuration
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect

# Liquibase Configuration
spring.liquibase.change-log=classpath:db/changelog/db.changelog-master.xml
spring.liquibase.enabled=true

# JWT Configuration
jwt.secret=your-secret-key-here
jwt.expiration=86400000

# Server Configuration
server.port=8080
```

### 3. 🏗️ Build and Run

```bash
# Clone the repository
git clone <repository-url>
cd authserver

# Build the project
mvn clean install

# Run the application
mvn spring-boot:run
```

---

## 🔌 API Endpoints

### Authentication Endpoints

| Method | Endpoint | Description | Authentication |
|--------|----------|-------------|----------------|
| POST   | `/api/auth/signup` | User registration | None |
| POST   | `/api/auth/login` | User login | None |

---

## 📊 Database Schema

### Core Tables

- **app_user**: User information (UUID, username, email, password)
- **role**: System roles (ADMIN, USER, MODERATOR)
- **permission**: System permissions (READ, WRITE, DELETE)
- **user_role**: Many-to-many relationship between users and roles
- **role_permission**: Many-to-many relationship between roles and permissions

---

## 🔐 Security Features

### JWT Token Structure

```json
{
  "sub": "user-uuid",
  "username": "john.doe",
  "roles": ["USER", "ADMIN"],
  "permissions": ["READ", "WRITE"],
  "iat": 1234567890,
  "exp": 1234567890
}
```

### Authorization Examples

```java
// Controller method with role-based authorization
@GetMapping("/admin/users")
@PreAuthorize("hasRole('ADMIN')")
public ResponseEntity<List<UserDto>> getAllUsers() {
    // Implementation
}

// Controller method with permission-based authorization
@DeleteMapping("/admin/users/{id}")
@PreAuthorize("hasPermission('DELETE')")
public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
    // Implementation
}
```

---

## 🧪 Testing

### Sample API Requests

#### User Registration
```bash
curl -X POST http://localhost:8080/api/auth/signup \
  -H "Content-Type: application/json" \
  -d '{
    "username": "john.doe",
    "email": "john@example.com",
    "password": "password123"
  }'
```

#### User Login
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "john.doe",
    "password": "password123"
  }'
```

#### Accessing Protected Endpoint
```bash
curl -X GET http://localhost:8080/api/admin/users \
  -H "Authorization: Bearer <jwt-token>"
```

---

## 📈 Performance Considerations

- JWT tokens are stateless, reducing database load
- Role and permission caching for frequently accessed data
- Proper indexing on user lookup fields

---

## 🔧 Configuration Options

### JWT Configuration

```yaml
jwt:
  secret: ${JWT_SECRET:default-secret-key}
  expiration: ${JWT_EXPIRATION:86400000} # 24 hours
```

### Security Configuration

```yaml
security:
  cors:
    allowed-origins: "*"
    allowed-methods: "GET,POST,PUT,DELETE,OPTIONS"
    allowed-headers: "*"
  csrf:
    enabled: false
```

---

## 🐛 Troubleshooting

### Common Issues

1. **Database Connection Issues**
   - Ensure MySQL container is running
   - Check database credentials in application.properties

2. **JWT Token Validation Errors**
   - Verify JWT secret configuration
   - Check token expiration settings

3. **Permission Denied Errors**
   - Verify user roles and permissions in database
   - Check @PreAuthorize annotations

---

## 📚 Future Enhancements

- OAuth2 integration (Google, GitHub)
- Password reset functionality
- Account email verification
- Multi-factor authentication (MFA)
- Rate limiting and security monitoring
- Audit logging for security events

---

## 👨‍💻 Author

**Muhammad Usama**  
Backend Developer learning and building modern security systems using Spring Boot & OAuth2.  
Mentored by a senior engineer with 15+ years experience in Java & security architecture.

> "Security is not a feature; it's a continuous design discipline."

---

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

**authShorts** (cm_yt) - A Spring Boot 3.5.7 application for a short-form video platform with authentication capabilities. This is an early-stage project with foundational infrastructure in place but minimal domain implementation.

- **Language**: Java 21
- **Framework**: Spring Boot 3.5.7
- **Build Tool**: Gradle with wrapper
- **Database**: MySQL
- **ORM**: Spring Data JPA with Hibernate

## Development Commands

### Build & Run
```bash
# Build the project
./gradlew build

# Run the application (development mode with auto-reload)
./gradlew bootRun

# Clean build
./gradlew clean build
```

### Testing
```bash
# Run all tests
./gradlew test

# Run tests with coverage
./gradlew test jacocoTestReport

# Run a single test class
./gradlew test --tests com.example.cm_yt.ClassName

# Run a single test method
./gradlew test --tests com.example.cm_yt.ClassName.methodName
```

### Documentation
```bash
# Generate REST API documentation (Spring REST Docs)
./gradlew asciidoctor

# Access Swagger UI (when application is running)
# http://localhost:8080/swagger-ui.html
```

### Database
```bash
# The application uses profiles for environment-specific configuration
# Development: MySQL at localhost:3306/cm_yt
# DDL mode in dev: create (auto-generates schema on startup)
# Ensure MySQL is running before starting the application
```

## Architecture & Code Organization

### Layer Architecture
The project follows standard Spring Boot layered architecture:

```
com.example.cm_yt/
├── global/                    # Cross-cutting concerns
│   ├── ApiResponse.java       # Standardized response wrapper
│   ├── OpenApiConfig.java     # Swagger/OpenAPI configuration
│   └── exception/
│       └── GlobalExceptionHandler.java  # Centralized exception handling
├── [domain]/                  # Domain packages (to be implemented)
│   ├── controller/            # REST endpoints
│   ├── service/               # Business logic
│   ├── repository/            # Data access layer
│   ├── entity/                # JPA entities
│   └── dto/                   # Data transfer objects
└── CmYtApplication.java       # Spring Boot main class
```

### Global Infrastructure

**ApiResponse Pattern**: All API responses must use the `ApiResponse<T>` wrapper:
```java
// Success response
return ApiResponse.success(data);

// Error response
return ApiResponse.fail("Error message");
```

Response format:
```json
{
  "success": boolean,
  "message": string,
  "data": T
}
```

**Exception Handling**: The `GlobalExceptionHandler` automatically catches:
- Validation errors (`@Valid`) → 400 with field-specific messages
- All other exceptions → 500 with generic error message

**Swagger Documentation**: Auto-configured at `/swagger-ui.html`. All REST endpoints are automatically documented.

### Database Configuration

**Profiles**:
- `dev`: Local development (active by default with `secret`)
- `prod`: Production environment
- `secret`: For sensitive configuration (not in version control)

**Development Database**:
- URL: `jdbc:mysql://localhost:3306/cm_yt`
- Credentials: root with no password (configure via application-secret.yml for security)
- DDL Auto: `create` (schema auto-generated, data cleared on restart)

**Important**: When implementing entities, be aware that `ddl-auto: create` will drop and recreate tables on application restart in dev mode.

### Logging

Logback configuration at `src/main/resources/logback-spring.xml`:
- Console output with timestamp, thread, log level, logger name
- Default level: INFO
- SQL queries logged via `show-sql: true` in dev profile

## Key Dependencies

- **Spring Boot Starter Web**: REST API development
- **Spring Boot Starter Data JPA**: Database access
- **Spring Boot Starter Validation**: Bean validation (`@Valid`, `@NotNull`, etc.)
- **springdoc-openapi-ui 1.7.0**: Swagger/OpenAPI documentation
- **Lombok**: Reduces boilerplate (`@Getter`, `@Setter`, `@Builder`, `@Slf4j`)
- **MySQL Connector/J**: MySQL database driver
- **Spring REST Docs**: API documentation generation from tests

## Development Notes

### When Adding New Features

1. Create domain package under `com.example.cm_yt` (e.g., `user`, `video`, `auth`)
2. Follow the layer structure: entity → repository → service → controller → dto
3. Use `@Valid` with DTOs for automatic validation
4. Controllers should return `ApiResponse<T>` for consistency
5. Use Lombok annotations to reduce boilerplate
6. Write tests for REST endpoints using MockMvc and Spring REST Docs

### Database Schema Changes

In development, schema changes are auto-applied on restart due to `ddl-auto: create`. For production-ready schema management, consider:
- Changing `ddl-auto` to `validate` or `none`
- Using Flyway or Liquibase for versioned migrations

### Testing Strategy

Tests use:
- **JUnit 5**: Core testing framework
- **Spring Boot Test**: Integration testing with application context
- **MockMvc**: REST API testing
- **Spring REST Docs**: Generate documentation snippets from tests

Generated documentation snippets are placed in `build/generated-snippets/` and processed by Asciidoctor.

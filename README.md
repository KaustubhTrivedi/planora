# Planora - AI-Powered Travel Planning Platform

Planora is a comprehensive travel planning platform that combines AI-powered recommendations with collaborative trip planning features.

## Architecture

- **Backend**: Java 21 + Spring Boot 3.x
- **Frontend**: React 18 + TypeScript
- **Database**: PostgreSQL 15
- **Cache**: Redis 7
- **File Storage**: AWS S3
- **Containerization**: Docker & Docker Compose

## Prerequisites

- Docker and Docker Compose
- Java 21 (for local development)
- Node.js 18+ (for frontend development)
- Make (optional, for convenience commands)

## Quick Start

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd planora
   ```

2. **Set up environment variables**
   ```bash
   cp .env.example .env
   # Edit .env with your API keys and configuration
   ```

3. **Start the development environment**
   ```bash
   make up
   # or
   docker-compose -f docker-compose.yml -f docker-compose.dev.yml up -d
   ```

4. **Access the applications**
   - Backend API: http://localhost:8080/api
   - Frontend: http://localhost:3000
   - PgAdmin: http://localhost:5050 (admin@planora.com / admin123)
   - Redis Commander: http://localhost:8081

## Development Commands

### Using Make (Recommended)
```bash
make help          # Show all available commands
make build         # Build all Docker images
make up            # Start development environment
make down          # Stop all services
make logs          # Show logs from all services
make test          # Run all tests
make clean         # Clean up Docker resources
```

### Using Docker Compose Directly
```bash
# Development environment
docker-compose -f docker-compose.yml -f docker-compose.dev.yml up -d

# Production environment
docker-compose up -d

# View logs
docker-compose logs -f backend
```

### Backend Development
```bash
cd backend
./gradlew bootRun          # Run Spring Boot application
./gradlew test             # Run tests
./gradlew build            # Build application
```

## Configuration

### Environment Variables

Key environment variables (see `.env.example` for complete list):

- `OPENAI_API_KEY`: OpenAI API key for AI features
- `GOOGLE_MAPS_API_KEY`: Google Maps API key for location services
- `AWS_ACCESS_KEY` / `AWS_SECRET_KEY`: AWS credentials for S3 storage
- `DATABASE_URL`: PostgreSQL connection string
- `REDIS_HOST`: Redis server host

### Application Profiles

- `dev`: Development profile with debug logging and auto-DDL
- `test`: Test profile with H2 in-memory database
- `prod`: Production profile with optimized settings

## API Documentation

Once the backend is running, API documentation is available at:
- Swagger UI: http://localhost:8080/api/swagger-ui.html
- OpenAPI JSON: http://localhost:8080/api/v3/api-docs

## Database Management

### Accessing the Database
- **PgAdmin**: http://localhost:5050
  - Email: admin@planora.com
  - Password: admin123
  - Server: postgres (host), port 5432

### Database Operations
```bash
make db-reset      # Reset database (destroys all data)
make db-migrate    # Run database migrations
```

## Testing

### Backend Tests
```bash
cd backend
./gradlew test                    # Run all tests
./gradlew test --tests UserTest   # Run specific test class
```

### Frontend Tests
```bash
cd frontend
npm test                          # Run tests in watch mode
npm run test:coverage             # Run tests with coverage
```

## Monitoring and Health Checks

- **Health Check**: http://localhost:8080/api/actuator/health
- **Metrics**: http://localhost:8080/api/actuator/metrics
- **Application Info**: http://localhost:8080/api/actuator/info

## Troubleshooting

### Common Issues

1. **Port conflicts**: Ensure ports 3000, 5432, 6379, and 8080 are available
2. **Docker build failures**: Run `make clean` to clean up Docker resources
3. **Database connection issues**: Check if PostgreSQL container is healthy
4. **API key errors**: Verify all required API keys are set in `.env`

### Logs
```bash
make logs                    # All services
make logs-backend           # Backend only
make logs-frontend          # Frontend only
```

### Reset Environment
```bash
make clean                  # Clean up all Docker resources
make build                  # Rebuild images
make up                     # Start fresh environment
```

## Contributing

1. Create a feature branch from `main`
2. Make your changes
3. Run tests: `make test`
4. Submit a pull request

## License

[License information here]# planora

# Planora - AI-Powered Travel Planning Platform

Planora is a comprehensive travel planning platform that combines AI-powered recommendations with collaborative trip planning features.

## Architecture

- **Backend**: Node.js 18+ + NestJS (TypeScript)
- **Frontend**: React 18 + TypeScript *(not yet implemented)*
- **Database**: PostgreSQL 15
- **Cache**: Redis 7
- **File Storage**: AWS S3 *(planned)*
- **Containerization**: Docker & Docker Compose

## Project Status

### ✅ Completed
- Project structure and module scaffolding
- NestJS backend with 13 core modules:
  - Authentication, Users, Trips, AI, Location
  - Collaboration, Files, Notifications, Checklists
  - Cache, Database, Storage, Integrations
- Docker Compose setup for PostgreSQL and Redis
- Development tooling (Makefile, scripts)

### 🚧 In Progress / Next Steps
- Database connection and ORM setup (TypeORM/Prisma)
- Entity models with proper relationships
- Service implementations (currently placeholder stubs)
- Authentication and JWT security
- Trip management core functionality

### 📋 Planned
- Frontend application
- AI integration (OpenAI)
- External API integrations (Google Maps, Weather, Currency)
- Real-time collaboration features
- File storage (AWS S3)
- Comprehensive testing suite

## Prerequisites

- Docker and Docker Compose
- Node.js 18+ (for backend and frontend development)
- npm or yarn
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
   - Backend API: http://localhost:3000 (default NestJS port)
   - Frontend: http://localhost:3000 *(not yet implemented)*
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
npm install                # Install dependencies
npm run start:dev         # Run NestJS application in watch mode
npm run start:prod        # Run in production mode
npm run test              # Run unit tests
npm run test:e2e          # Run end-to-end tests
npm run build             # Build application
```

## Configuration

### Environment Variables

Key environment variables (see `.env.example` for complete list):

- `OPENAI_API_KEY`: OpenAI API key for AI features
- `GOOGLE_MAPS_API_KEY`: Google Maps API key for location services
- `AWS_ACCESS_KEY` / `AWS_SECRET_KEY`: AWS credentials for S3 storage
- `DATABASE_URL`: PostgreSQL connection string
- `REDIS_HOST`: Redis server host

### Environment Configuration

Create a `.env` file in the backend directory:
```env
# Database
DATABASE_URL=postgresql://planora:planora123@localhost:5432/planora
DATABASE_HOST=localhost
DATABASE_PORT=5432
DATABASE_USER=planora
DATABASE_PASSWORD=planora123
DATABASE_NAME=planora

# Redis
REDIS_HOST=localhost
REDIS_PORT=6379

# JWT
JWT_SECRET=your-secret-key-here
JWT_EXPIRATION=7d

# External APIs
OPENAI_API_KEY=your-openai-key
GOOGLE_MAPS_API_KEY=your-google-maps-key
WEATHER_API_KEY=your-weather-api-key
CURRENCY_API_KEY=your-currency-api-key

# AWS S3
AWS_ACCESS_KEY_ID=your-access-key
AWS_SECRET_ACCESS_KEY=your-secret-key
AWS_REGION=us-east-1
AWS_S3_BUCKET=planora-documents-dev
```

## API Documentation

Once the backend is running, API documentation will be available via Swagger:
- Swagger UI: http://localhost:3000/api *(to be configured)*
- OpenAPI JSON: http://localhost:3000/api-json *(to be configured)*

*Note: Swagger/OpenAPI integration needs to be set up in the NestJS application.*

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
npm test                          # Run all unit tests
npm run test:watch                # Run tests in watch mode
npm run test:cov                  # Run tests with coverage
npm run test:e2e                  # Run end-to-end tests
```

### Frontend Tests
```bash
cd frontend
npm test                          # Run tests in watch mode
npm run test:coverage             # Run tests with coverage
```

## Monitoring and Health Checks

Health check endpoints will be available once implemented:
- **Health Check**: http://localhost:3000/health *(to be implemented)*
- **Metrics**: http://localhost:3000/metrics *(to be implemented)*

*Note: NestJS health check module needs to be configured.*

## Troubleshooting

### Common Issues

1. **Port conflicts**: Ensure ports 3000 (backend), 5432 (PostgreSQL), 6379 (Redis), 5050 (PgAdmin), and 8081 (Redis Commander) are available
2. **Docker build failures**: Run `make clean` to clean up Docker resources
3. **Database connection issues**: 
   - Check if PostgreSQL container is healthy: `docker ps`
   - Verify database credentials in `.env` file
   - Ensure database connection is configured in NestJS app
4. **Module dependencies**: Run `npm install` in the backend directory if you encounter module errors
5. **API key errors**: Verify all required API keys are set in `.env` file

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

## Backend Module Structure

The backend is organized into the following modules (currently scaffolded, implementation pending):

- **auth**: Authentication and authorization
- **users**: User management
- **trips**: Trip planning and management
- **ai**: AI-powered recommendations
- **location**: Location services and maps integration
- **collaboration**: Real-time collaboration features
- **files**: File upload and management
- **notifications**: Notification system
- **checklists**: Trip checklists and packing lists
- **cache**: Caching layer
- **database**: Database utilities
- **storage**: Storage abstraction layer
- **integrations**: External service integrations

## Development Notes

- The backend uses NestJS framework with TypeScript
- All modules have been scaffolded but contain placeholder implementations
- Database connection (TypeORM/Prisma) needs to be configured
- Entity models need to be defined with proper relationships
- Services need business logic implementation
- Authentication middleware needs to be set up

<!-- ## License

[License information here] -->

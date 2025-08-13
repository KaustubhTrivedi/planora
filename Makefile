# Planora Development Makefile

.PHONY: help build up down logs clean test backend-test frontend-test

# Default target
help:
	@echo "Available commands:"
	@echo "  build          - Build all Docker images"
	@echo "  up             - Start all services in development mode"
	@echo "  down           - Stop all services"
	@echo "  logs           - Show logs from all services"
	@echo "  logs-backend   - Show backend logs"
	@echo "  logs-frontend  - Show frontend logs"
	@echo "  clean          - Clean up Docker resources"
	@echo "  test           - Run all tests"
	@echo "  backend-test   - Run backend tests"
	@echo "  frontend-test  - Run frontend tests"
	@echo "  db-reset       - Reset database"
	@echo "  db-migrate     - Run database migrations"

# Build all images
build:
	docker-compose -f docker-compose.yml -f docker-compose.dev.yml build

# Start development environment
up:
	docker-compose -f docker-compose.yml -f docker-compose.dev.yml up -d
	@echo "Services starting..."
	@echo "Backend: http://localhost:8080/api"
	@echo "Frontend: http://localhost:3000"
	@echo "PgAdmin: http://localhost:5050 (admin@planora.com / admin123)"
	@echo "Redis Commander: http://localhost:8081"

# Stop all services
down:
	docker-compose -f docker-compose.yml -f docker-compose.dev.yml down

# Show logs
logs:
	docker-compose -f docker-compose.yml -f docker-compose.dev.yml logs -f

logs-backend:
	docker-compose -f docker-compose.yml -f docker-compose.dev.yml logs -f backend

logs-frontend:
	docker-compose -f docker-compose.yml -f docker-compose.dev.yml logs -f frontend

# Clean up Docker resources
clean:
	docker-compose -f docker-compose.yml -f docker-compose.dev.yml down -v
	docker system prune -f
	docker volume prune -f

# Run tests
test: backend-test frontend-test

backend-test:
	cd backend && ./gradlew test

frontend-test:
	cd frontend && npm test

# Database operations
db-reset:
	docker-compose -f docker-compose.yml -f docker-compose.dev.yml stop postgres
	docker-compose -f docker-compose.yml -f docker-compose.dev.yml rm -f postgres
	docker volume rm planora_postgres_data || true
	docker-compose -f docker-compose.yml -f docker-compose.dev.yml up -d postgres

db-migrate:
	cd backend && ./gradlew flywayMigrate

# Production build
build-prod:
	docker-compose build

# Production deployment
up-prod:
	docker-compose up -d
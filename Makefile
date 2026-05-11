.PHONY: build up dev down restart logs app-logs frontend-logs db-logs kafka-logs ps clean rebuild

build:
	docker compose build

up:
	docker compose up -d

dev:
	docker compose -f docker-compose.yml -f docker-compose.dev.yml up

down:
	docker compose down

restart:
	docker compose restart

logs:
	docker compose logs -f app frontend postgres

app-logs:
	docker compose logs -f app

frontend-logs:
	docker compose logs -f frontend

db-logs:
	docker compose logs -f postgres

kafka-logs:
	docker compose logs -f kafka

ps:
	docker compose ps

clean:
	docker compose down -v --remove-orphans

rebuild:
	docker compose down -v --remove-orphans
	docker compose build --no-cache
	docker compose up -d

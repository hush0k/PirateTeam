.PHONY: build up dev down restart logs app-logs db-logs ps clean rebuild

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
	docker compose logs -f

app-logs:
	docker compose logs -f app

db-logs:
	docker compose logs -f postgres

ps:
	docker compose ps

clean:
	docker compose down -v --remove-orphans

rebuild:
	docker compose down -v --remove-orphans
	docker compose build --no-cache
	docker compose up -d
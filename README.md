# Spring2026

Repository for Spring 2026 practices and TSIS

## Practice 1

Run:

```bash
./mvnw spring-boot:run
```

Swagger:

```text
http://localhost:8080/swagger-ui/index.html
```

## Practice 2

Dev profile:

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

Test profile:

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=test
```

The `dev` and `test` profiles use different configuration values.

## Practice 3

Conditional bean enabled:

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

Conditional bean disabled:

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=test
```

The conditional bean is controlled by `practice3.enabled`.

## Practice 4

**Part 1:** the application is connected to PostgreSQL. The repository layer uses Spring Data JPA, with paged queries (`Pageable` / `Page`) for listing movies, filtering by genre and filtering by director.

**Part 2:** the schema is under Flyway control (migrations in `src/main/resources/db/migration`) and seed data is added by a migration. Hibernate no longer creates tables.

This is the merged task of weeks 5-6.

## Practice 5

- **Logging aspect:** `ServiceLoggingAspect` wraps every public `MovieService` method and logs the call, its arguments, the execution time in ms and any failure.
- **Caching:** `MovieService.get` is cached in the `movies` cache (Caffeine, max 1000 entries, 10 minutes). `update` refreshes the entry (`@CachePut`) and `delete` evicts it (`@CacheEvict`).

## Practice 6

- **Swagger UI:** the API is published at `http://localhost:8080/swagger-ui/index.html`.
- **External service:** `GET /api/movie/{id}/rating` calls the OMDb API with a connect timeout of 2 s and a read timeout of 3 s. If the service fails, the endpoint returns a fallback result (`"source":"FALLBACK"`) instead of an error.

## SIS 1

REST API of the course project (movies):

- CRUD endpoints with DTOs (`MovieRequest`, `MovieUpdateRequest`, `MovieResponse`) and Bean Validation.
- Unified error format (`ErrorResponse`: `status`, `message`, `timestamp`, `errors`) produced by `GlobalExceptionHandler`.
- Correct HTTP status codes: 200, 201, 204, 400, 404, 409, 422, 500.
- Maven build with profiles and an HTTP-client collection covering all endpoints.

Consolidates weeks 1-4.

## SIS 2

Layered architecture and tests:

- Layers per feature: `web` (controllers, DTOs, API mapper), `app` (services, query interfaces), `domain` (records, repository interface, exceptions), `infra` (JPA entities, Spring Data repositories, entity mapper).
- Unit tests for the service layer, MockMvc slice tests for the web layer, and a repository integration test against a real PostgreSQL (Testcontainers).

Run all tests (Docker must be running):

```bash
./mvnw clean verify
```

Consolidates weeks 5-8.

## SIS 3

Documented API and event-driven communication. Consolidates weeks 9-11.

### Start Kafka locally

Requires Docker. Kafka runs as a single-node KRaft broker from `docker-compose.yml`:

```bash
docker compose up -d      # start
docker compose ps         # check it is running
docker compose down       # stop
```

Then run the app (PostgreSQL must be running):

```bash
./mvnw spring-boot:run
```

### OpenAPI

Swagger UI: `http://localhost:8080/swagger-ui/index.html` (raw spec: `/v3/api-docs`).
Every endpoint documents its success and error responses (400, 404, 409, 422, 500) using the shared `ErrorResponse` schema.

### Resilient outbound client

The rating client uses timeouts (connect 2 s, read 3 s), retry (3 attempts), a circuit breaker and a fallback (`"source":"FALLBACK"`).

Demo with a dead service:

```bash
RATING_BASE_URL=http://localhost:9999 ./mvnw spring-boot:run
for i in 1 2 3 4 5 6 7 8; do curl http://localhost:8080/api/movie/1/rating; echo; done
```

All responses are `FALLBACK`, and the log shows `Rating circuit is OPEN` after the first few calls.

### Logging aspect and caching

`ServiceLoggingAspect` logs every `MovieService` call with its duration. `MovieService.get` is cached (Caffeine), and `MovieServiceCachingTest` proves it.

### Kafka

| | |
|---|---|
| Event | `MovieCreatedEvent` (`eventId`, `movieId`, `title`, `occurredAt`) |
| Topic | `movie-created` (3 partitions), key = movie id |
| Producer | sends `AFTER_COMMIT`, so a rolled-back request publishes nothing |
| Consumer | group `movie-audit`, idempotent on `eventId` |
| Dead-letter topic | `movie-created.DLT` |

Poison-message demo. A movie titled `POISON` always fails in the consumer:

```bash
curl -X POST http://localhost:8080/api/movie -H "Content-Type: application/json" \
  -d '{"title":"POISON","genre":"Drama","year":2020,"description":"demo"}'
```

The record is retried 3 times and then moved to the DLT. A message that is not valid JSON ends up there too (`ErrorHandlingDeserializer`). Inspect it with:

```bash
docker compose exec kafka /opt/kafka/bin/kafka-console-consumer.sh \
  --bootstrap-server localhost:9092 --topic movie-created.DLT --from-beginning \
  --property print.key=true --property print.headers=true
```

Notes: delivery is at-least-once, so the consumer ignores repeated `eventId`s. If Kafka is down at publish time the event is lost; a transactional outbox would close that gap.


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


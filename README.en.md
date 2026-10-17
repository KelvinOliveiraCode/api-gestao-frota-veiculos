# Vehicle Fleet Management API

REST API for vehicle fleet management with Java 21, Spring Boot 3 and PostgreSQL. MVP scope: vehicles, maintenance and costs, with full CRUD and cost report per vehicle and period.

## Stack

- Java 21 (JDK Temurin/Eclipse Adoptium)
- Spring Boot 3.3.5, Spring Data JPA, Spring Web, Jakarta Validation
- PostgreSQL (driver org.postgresql:postgresql, runtime scope)
- H2 in-memory for tests (test scope — no PostgreSQL installation required to run tests)
- JUnit 5, AssertJ, Mockito

## Project structure

```
api-gestao-frota-veiculos/
├── pom.xml
├── README.md
├── README.en.md
├── BRIEF.md
├── LICENSE
├── src/main/resources/application.properties   # PostgreSQL connection
├── src/test/resources/application.properties   # H2 in-memory (tests)
└── src/main/java/com/kelvinoliveira/gestaofrota/
    ├── GestaoFrotaApplication.java
    ├── controller/        # Veiculo, Manutencao, Custo, RelatorioCusto
    ├── dto/               # request/response records
    ├── exception/         # GlobalExceptionHandler + business exceptions
    ├── model/             # JPA entities and enums
    ├── repository/        # Spring Data JPA
    └── service/           # use cases
└── src/test/java/com/kelvinoliveira/gestaofrota/
    ├── service/           # 4 tests against in-memory H2 (@DataJpaTest)
    └── controller/        # 4 REST layer tests (@WebMvcTest + Mockito)
```

## Prerequisites

- Java 21 (`java -version` → 21.x)
- Apache Maven 3.9+ (`mvn -version`)
- PostgreSQL 14+ only to run the API with a real database (tests use in-memory H2)

## How to use

### 1. Configure the database

Create the database in PostgreSQL:

```sql
CREATE DATABASE gestao_frota;
```

Adjust `src/main/resources/application.properties` if your user/password differ:

```
spring.datasource.url=jdbc:postgresql://localhost:5432/gestao_frota
spring.datasource.username=postgres
spring.datasource.password=postgres
```

### 2. Run the application

```bash
mvn spring-boot:run
```

The API starts at `http://localhost:8080`. The schema is created/updated automatically (`ddl-auto=update`).

## Endpoints

### Vehicles

| Method | Path | Description |
|--------|------|-------------|
| GET | `/veiculos` | List all (optional `?status=ATIVO|EM_MANUTENCAO|INATIVO|VENDIDO`) |
| GET | `/veiculos/{id}` | Find by ID |
| POST | `/veiculos` | Create (unique plate, case insensitive) |
| PUT | `/veiculos/{id}` | Update |
| DELETE | `/veiculos/{id}` | Delete |

### Maintenance

| Method | Path | Description |
|--------|------|-------------|
| GET | `/manutencoes` | List all |
| GET | `/manutencoes/{id}` | Find by ID |
| GET | `/manutencoes/veiculo/{veiculoId}` | List by vehicle |
| POST | `/manutencoes` | Create maintenance order |
| PATCH | `/manutencoes/{id}/concluir` | Mark as completed |
| PATCH | `/manutencoes/{id}/cancelar` | Cancel |
| DELETE | `/manutencoes/{id}` | Delete |

### Costs

| Method | Path | Description |
|--------|------|-------------|
| GET | `/custos` | List all |
| GET | `/custos/{id}` | Find by ID |
| GET | `/custos/veiculo/{veiculoId}` | List by vehicle |
| POST | `/custos` | Create cost (fuel, insurance, IPVA, tire, maintenance, other) |
| DELETE | `/custos/{id}` | Delete |

### Cost report

| Method | Path | Description |
|--------|------|-------------|
| GET | `/relatorios/custos` | Costs of a vehicle in a period, grouped by type |

Parameters are `veiculoId` (Long), `dataInicio` (YYYY-MM-DD), `dataFim` (YYYY-MM-DD).

Example response:

```json
{
  "veiculoId": 1,
  "veiculoPlaca": "ABC-1234",
  "dataInicio": "2024-01-01",
  "dataFim": "2024-01-31",
  "valorTotal": 800.00,
  "itens": [
    { "tipo": "COMBUSTIVEL", "total": 300.00, "quantidade": 2 },
    { "tipo": "SEGURO", "total": 500.00, "quantidade": 1 }
  ]
}
```

Errors return a standardized body (`timestamp`, `status`, `erro`, `mensagem`, `caminho`) with the appropriate HTTP status (400 validation, 404 not found, 409 duplicate plate).

## Tests

Tests run on in-memory H2 — no need to install or start PostgreSQL:

```bash
mvn test
```

Expected result: BUILD SUCCESS, 40 tests, 0 failures, 0 errors.

To build the JAR:

```bash
mvn package
# generates target/api-gestao-frota-veiculos-1.0.0.jar
```

## License

Distributed under the MIT License. See the LICENSE file.

## Author

Kelvin Oliveira

api-gestao-frota-veiculos — vehicle fleet management with Java Spring Boot 3 + PostgreSQL.

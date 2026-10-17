# API Gestao de Frota de Veiculos

API REST para gestao de frota de veiculos com Java 21, Spring Boot 3 e PostgreSQL. Escopo MVP: veiculos, manutencoes e custos, com CRUD completo e relatorio de custos por veiculo e periodo.

## Stack

- Java 21 (JDK Temurin/Eclipse Adoptium)
- Spring Boot 3.3.5, Spring Data JPA, Spring Web, Jakarta Validation
- PostgreSQL (driver org.postgresql:postgresql, escopo runtime)
- H2 em memoria para os testes (escopo test — nao precisa de PostgreSQL instalado para testar)
- JUnit 5, AssertJ, Mockito

## Estrutura do projeto

```
api-gestao-frota-veiculos/
├── pom.xml
├── README.md
├── README.en.md
├── BRIEF.md
├── LICENSE
├── src/main/resources/application.properties   # conexao PostgreSQL
├── src/test/resources/application.properties   # H2 em memoria (testes)
└── src/main/java/com/kelvinoliveira/gestaofrota/
    ├── GestaoFrotaApplication.java
    ├── controller/        # Veiculo, Manutencao, Custo, RelatorioCusto
    ├── dto/               # records de request/response
    ├── exception/         # GlobalExceptionHandler + excecoes de negocio
    ├── model/             # entidades JPA e enums
    ├── repository/        # Spring Data JPA
    └── service/           # casos de uso
└── src/test/java/com/kelvinoliveira/gestaofrota/
    ├── service/           # 4 testes contra H2 em memoria (@DataJpaTest)
    └── controller/        # 4 testes de camada REST (@WebMvcTest + Mockito)
```

## Pré-requisitos

- Java 21 (`java -version` → 21.x)
- Apache Maven 3.9+ (`mvn -version`)
- PostgreSQL 14+ apenas para rodar a API com banco real (os testes usam H2 em memoria)

## Como usar

### 1. Configure o banco de dados

Crie o banco no PostgreSQL:

```sql
CREATE DATABASE gestao_frota;
```

Ajuste `src/main/resources/application.properties` se o usuario/senha diferirem:

```
spring.datasource.url=jdbc:postgresql://localhost:5432/gestao_frota
spring.datasource.username=postgres
spring.datasource.password=postgres
```

### 2. Rode a aplicação

```bash
mvn spring-boot:run
```

A API sobe em `http://localhost:8080`. O schema e criado/atualizado automaticamente (`ddl-auto=update`).

## Endpoints

### Veículos

| Metodo | Caminho | Descricao |
|--------|---------|-----------|
| GET | `/veiculos` | Lista todos (opcional `?status=ATIVO|EM_MANUTENCAO|INATIVO|VENDIDO`) |
| GET | `/veiculos/{id}` | Busca por ID |
| POST | `/veiculos` | Cadastra (placa unica, ignorando caixa) |
| PUT | `/veiculos/{id}` | Atualiza |
| DELETE | `/veiculos/{id}` | Remove |

### Manutencoes

| Metodo | Caminho | Descricao |
|--------|---------|-----------|
| GET | `/manutencoes` | Lista todas |
| GET | `/manutencoes/{id}` | Busca por ID |
| GET | `/manutencoes/veiculo/{veiculoId}` | Lista por veiculo |
| POST | `/manutencoes` | Cadastra ordem de manutencao |
| PATCH | `/manutencoes/{id}/concluir` | Marca como concluida |
| PATCH | `/manutencoes/{id}/cancelar` | Cancela |
| DELETE | `/manutencoes/{id}` | Remove |

### Custos

| Metodo | Caminho | Descricao |
|--------|---------|-----------|
| GET | `/custos` | Lista todos |
| GET | `/custos/{id}` | Busca por ID |
| GET | `/custos/veiculo/{veiculoId}` | Lista por veiculo |
| POST | `/custos` | Cadastra custo (combustivel, seguro, IPVA, pneu, manutencao, outro) |
| DELETE | `/custos/{id}` | Remove |

### Relatorio de custos

| Metodo | Caminho | Descricao |
|--------|---------|-----------|
| GET | `/relatorios/custos` | Custos de um veiculo em um periodo, agrupados por tipo |

Parametros sao `veiculoId` (Long), `dataInicio` (YYYY-MM-DD), `dataFim` (YYYY-MM-DD).

Exemplo de resposta:

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

Erros retornam corpo padronizado (`timestamp`, `status`, `erro`, `mensagem`, `caminho`) com status HTTP adequado (400 validacao, 404 nao encontrado, 409 placa duplicada).

## Testes

Os testes rodam em H2 em memoria — nao e necessario instalar ou iniciar o PostgreSQL:

```bash
mvn test
```

Resultado esperado: BUILD SUCCESS, 40 testes, 0 falhas, 0 erros.

Para gerar o JAR:

```bash
mvn package
# gera target/api-gestao-frota-veiculos-1.0.0.jar
```

## Licenca

Distribuido sob a licenca MIT. Consulte o arquivo LICENSE.

## Autor

Kelvin Oliveira

api-gestao-frota-veiculos — gestao de frota com Java Spring Boot 3 + PostgreSQL.

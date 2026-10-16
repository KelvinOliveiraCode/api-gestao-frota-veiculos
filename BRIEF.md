# BRIEF — api-gestao-frota-veiculos

## Projeto / Project

Gestão de frota de veículos com Java Spring Boot 3 + PostgreSQL.
Vehicle fleet management with Java Spring Boot 3 + PostgreSQL.

Pasta do projeto / Project folder:
`C:\Users\Kelvin\AppData\Local\Temp\opencode\github-staging\projects\api-gestao-frota-veiculos\`

## Entregas / Deliveries

- `pom.xml`: Java 21, Spring Boot 3.3.5 (web, data-jpa, validation), driver PostgreSQL (runtime), H2 (test)
- `src/main/java/com/kelvinoliveira/gestaofrota/`:
  - `model/`: entidades JPA `Veiculo`, `Manutencao`, `Custo` e enums (`StatusVeiculo`, `TipoManutencao`, `StatusManutencao`, `TipoCusto`)
  - `dto/`: records de request/response para cada domínio + `RelatorioCustoResponse` / `RelatorioCustoItem`
  - `repository/`: `VeiculoRepository`, `ManutencaoRepository`, `CustoRepository` (Spring Data JPA)
  - `service/`: `VeiculoService`, `ManutencaoService`, `CustoService`, `RelatorioCustoService` (casos de uso + transações)
  - `controller/`: 4 controllers REST (`/veiculos`, `/manutencoes`, `/custos`, `/relatorios/custos`)
  - `exception/`: `GlobalExceptionHandler` + exceções de negócio (`VeiculoNaoEncontradoException`, `PlacaDuplicadaException`, etc.)
  - `GestaoFrotaApplication.java`
- `src/test/java/com/kelvinoliveira/gestaofrota/`: 8 classes de teste (4 serviço com `@DataJpaTest` em H2, 4 controlador com `@WebMvcTest` + Mockito) — **40 testes**
- `README.md`: bilíngue PT-BR + EN (título, descrição, stack, estrutura, pré-requisitos, como usar, endpoints, testes, licença MIT, autor Kelvin Oliveira)
- `LICENSE`: MIT, Kelvin Oliveira, 2026
- Este arquivo `BRIEF.md`

Critérios de aceitação atendidos: CRUD de veículos/manutenções/custos, relatório de custos por veículo e período, testes passando, README completo PT-BR/EN.

## Como verificar / How to verify

1. Abra o terminal na pasta do projeto:

   ```powershell
   cd C:\Users\Kelvin\AppData\Local\Temp\opencode\github-staging\projects\api-gestao-frota-veiculos
   ```

2. Rode os testes (comando de teste usado):

   ```powershell
   mvn test
   ```

   Resultado esperado: **BUILD SUCCESS — 40 testes, 0 falhas, 0 erros**.
   Os testes rodam em H2 em memória; não é necessário ter PostgreSQL instalado ou rodando.

3. (Opcional) Rode a API:

   ```powershell
   mvn spring-boot:run
   ```

   Sobe em `http://localhost:8080`. Requer PostgreSQL em `localhost:5432`, banco `gestao_frota` (conforme `src/main/resources/application.properties`).

4. Endpoints para conferir:

   - `GET/POST/PUT/DELETE /veiculos`
   - `GET/POST/PATCH/DELETE /manutencoes` (inclui `/manutencoes/veiculo/{id}`, `/concluir`, `/cancelar`)
   - `GET/POST/DELETE /custos` (inclui `/custos/veiculo/{id}`)
   - `GET /relatorios/custos?veiculoId=1&dataInicio=2024-01-01&dataFim=2024-01-31`

## Caminhos dos artefatos / Artifact paths

- README: `C:\Users\Kelvin\AppData\Local\Temp\opencode\github-staging\projects\api-gestao-frota-veiculos\README.md`
- BRIEF: `C:\Users\Kelvin\AppData\Local\Temp\opencode\github-staging\projects\api-gestao-frota-veiculos\BRIEF.md`
- LICENSE: `C:\Users\Kelvin\AppData\Local\Temp\opencode\github-staging\projects\api-gestao-frota-veiculos\LICENSE`

## Resultado dos testes / Test results

**PASS** — `mvn test`: BUILD SUCCESS, 40/40 testes aprovados (25 de serviço contra H2, 15 de controlador com MockMvc), 0 falhas, 0 erros. Autor: Kelvin Oliveira.

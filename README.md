# 🎫 Helpdesk API

API REST para gerenciamento de chamados de suporte técnico, desenvolvida com **Spring Boot 4**, autenticação via **JWT** e documentação interativa com **Swagger/OpenAPI 3**.

---

## 🚀 Tecnologias

| Tecnologia | Versão |
|---|---|
| Java | 17 |
| Spring Boot | 4.1.0 |
| Spring Security | — |
| Spring Data JPA | — |
| JJWT (JWT) | 0.12.6 |
| PostgreSQL | 16 |
| springdoc-openapi (Swagger) | 2.5.0 |
| Docker / Docker Compose | — |

---

## 📐 Arquitetura

```
src/main/java/com/alex/helpdesk/
├── config/          # Configurações (OpenAPI/Swagger)
├── controller/      # Endpoints REST
├── dto/             # Records de entrada e saída
├── exception/       # Exceções customizadas e GlobalExceptionHandler
├── model/           # Entidades JPA e Enums
├── repository/      # Interfaces Spring Data JPA
├── security/        # JWT Filter, JwtService, SecurityConfig, UserDetailsService
└── service/         # Regras de negócio
```

---

## ⚙️ Configuração do Ambiente

### Variáveis de Ambiente

Um arquivo `.env.example` está disponível na raiz do projeto como template.

| Variável | Descrição | Exemplo |
|---|---|---|
| `DB_HOST` | Host e porta do PostgreSQL | `localhost:5432` |
| `DB_NAME` | Nome do banco de dados | `helpdesk` |
| `DB_USER` | Usuário do banco | `helpdesk_user` |
| `DB_PASSWORD` | Senha do banco | `helpdesk_pass` |
| `JWT_SECRET` | Chave secreta para assinar o JWT (Base64) | *(ver abaixo)* |

> O `JWT_SECRET` possui um valor padrão de desenvolvimento. **Em produção, defina uma chave forte e única.**

---

## 🐳 Subindo com Docker Compose

A forma mais rápida de rodar a aplicação completa (API + Banco):

```bash
docker-compose up --build
```

A API estará disponível em: `http://localhost:8080`

---

## 🛠️ Rodando Localmente (sem Docker)

### Pré-requisitos
- Java 17+
- Maven 3.8+
- PostgreSQL 16 em execução

### Passos

1. Clone o repositório:
   ```bash
   git clone https://github.com/alex01sander/HelpDesk-api.git
   cd helpdesk-api
   ```

2. Configure as variáveis de ambiente (ou exporte no terminal):
   ```bash
   export DB_HOST=localhost:5432
   export DB_NAME=helpdesk
   export DB_USER=helpdesk_user
   export DB_PASSWORD=helpdesk_pass
   ```

3. Execute a aplicação:
   ```bash
   mvn spring-boot:run
   ```

---

## 📖 Documentação Interativa (Swagger UI)

Com a aplicação rodando, acesse:

- **Swagger UI:** [`http://localhost:8080/swagger-ui.html`](http://localhost:8080/swagger-ui.html)
- **OpenAPI JSON:** [`http://localhost:8080/v3/api-docs`](http://localhost:8080/v3/api-docs)

### Como autenticar no Swagger

1. Faça login em `POST /auth/login` para obter o token JWT.
2. Clique no botão **Authorize 🔒** no topo da página.
3. Cole o token no campo e confirme.
4. Todas as requisições seguintes serão enviadas com o header `Authorization: Bearer <token>`.

---

## 🔐 Autenticação

A API usa **JWT Bearer Token**. Inclua o token no header de todas as requisições autenticadas:

```
Authorization: Bearer <seu_token_jwt>
```

O token expira em **24 horas** (86400000 ms).

---

## 📡 Endpoints

### 🔑 Autenticação

| Método | Rota | Descrição | Permissão |
|---|---|---|---|
| `POST` | `/auth/login` | Realiza login e retorna o JWT | Público |

**Body (login):**
```json
{
  "email": "usuario@email.com",
  "senha": "123456"
}
```

---

### 👤 Usuários

| Método | Rota | Descrição | Permissão |
|---|---|---|---|
| `POST` | `/usuarios` | Cadastra um novo usuário | Público |
| `GET` | `/usuarios` | Lista todos os usuários | `TECNICO` |
| `GET` | `/usuarios/{id}` | Busca usuário por ID | `TECNICO` |
| `PUT` | `/usuarios/{id}` | Atualiza dados do usuário | `TECNICO` |
| `DELETE` | `/usuarios/{id}` | Remove usuário | `TECNICO` |

**Body:**
```json
{
  "nome": "Alex Sander",
  "email": "alex@email.com",
  "senha": "123456"
}
```

---

### 🔧 Técnicos

| Método | Rota | Descrição | Permissão |
|---|---|---|---|
| `POST` | `/tecnicos` | Cadastra um novo técnico | Público |
| `GET` | `/tecnicos` | Lista todos os técnicos | `TECNICO` |
| `GET` | `/tecnicos/{id}` | Busca técnico por ID | `TECNICO` |
| `PUT` | `/tecnicos/{id}` | Atualiza dados do técnico | `TECNICO` |
| `DELETE` | `/tecnicos/{id}` | Remove técnico | `TECNICO` |

**Body:**
```json
{
  "nome": "João Silva",
  "email": "joao@helpdesk.com",
  "senha": "123456",
  "especialidade": "SOFTWARE"
}
```

> **Especialidades disponíveis:** `HARDWARE`, `REDE`, `SOFTWARE`, `OUTRO`

---

### 🎫 Chamados

| Método | Rota | Descrição | Permissão |
|---|---|---|---|
| `POST` | `/chamados` | Abre um novo chamado | `USUARIO` |
| `GET` | `/chamados` | Lista todos os chamados (com paginação) | Autenticado |
| `GET` | `/chamados/{id}` | Busca chamado por ID | Autenticado |
| `PATCH` | `/chamados/{id}/atribuir-tecnico` | Atribui técnico ao chamado | `TECNICO` |
| `PATCH` | `/chamados/{id}/status` | Atualiza status do chamado | `TECNICO` |

**Paginação:**
```bash
GET /chamados?page=0&size=10&sort=prioridade,desc
```

**Body (abrir chamado):**
```json
{
  "titulo": "Impressora não funciona",
  "descricao": "A impressora do setor não está ligando",
  "prioridade": "ALTA"
}
```

> **Prioridades:** `BAIXA`, `MEDIA`, `ALTA`
> **Status:** `ABERTO`, `EM_ANDAMENTO`, `AGUARDANDO_CLIENTE`, `RESOLVIDO`, `FECHADO`

---

### 💬 Comentários

| Método | Rota | Descrição | Permissão |
|---|---|---|---|
| `POST` | `/chamados/{id}/comentarios` | Adiciona comentário ao chamado | Autenticado |
| `GET` | `/chamados/{id}/comentarios` | Lista comentários do chamado | Autenticado |

**Body:**
```json
{
  "texto": "Verificamos o problema, peça pedida."
}
```

> **Nota:** O autor do comentário é determinado automaticamente pelo token JWT do usuário autenticado (usuário ou técnico).

---

## 🗃️ Modelo de Dados

```
Usuario ──┐
          ├──< Chamado >──< Comentario
Tecnico ──┘
```

- Um **Usuário** pode abrir vários **Chamados**.
- Um **Técnico** pode ser atribuído a vários **Chamados**.
- Um **Chamado** pode ter vários **Comentários** (de usuários ou técnicos).

---

## 🧪 Testes

```bash
mvn test
```

**Cobertura atual:** 26 testes unitários — 0 falhas.

| Suite | Testes |
|---|---|
| `ChamadoServiceTest` | 9 |
| `ComentarioServiceTest` | 5 |
| `TecnicoServiceTest` | 7 |
| `UsuarioServiceTest` | 5 |

---

## 🔄 CI/CD

O projeto possui um workflow de CI/CD configurado com GitHub Actions que é executado automaticamente em cada push e pull request para as branches `main` e `develop`.

**O workflow inclui:**
- Build com Maven
- Execução de testes unitários
- Testes de integração com PostgreSQL
- Upload de resultados dos testes

**Para verificar o status dos builds, acesse a aba "Actions" no repositório GitHub.**

---

## ✨ Recursos de Produção

A API foi desenvolvida com padrões de produção-ready para 2026:

### 🔒 Segurança
- **Autenticação JWT Bearer** com validação de claims
- **Autorização baseada em roles** (USUARIO, TECNICO)
- **Autenticação automática** em comentários (via SecurityContext)
- **Segurança de comentários:** autor determinado pelo token, não pelo body

### 📊 Auditoria
- **Timestamps automáticos** em todas as entidades (createdAt, updatedAt)
- **Tracing de operações** com auditoria de data

### ⚡ Performance
- **Paginação nativa** em endpoints de listagem
- **Transações otimizadas** com @Transactional
- **Validações eficientes** com Bean Validation

### 🛡️ Tratamento de Erros
- **Validação detalhada** com erros por campo
- **Respostas padronizadas** via GlobalExceptionHandler
- **Exceções customizadas** para casos de negócio

### 🚀 DevOps
- **CI/CD automatizado** com GitHub Actions
- **Configuração ambiente** com .env.example
- **Docker Compose** para desenvolvimento local
- **Health checks** no banco de dados

---

## 📄 Licença

Este projeto está sob a licença MIT.

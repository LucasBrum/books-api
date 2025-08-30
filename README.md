# 📚 Books API - Sistema de Gerenciamento de Biblioteca

API REST completa desenvolvida em **Spring Boot** para gerenciamento de livros, autores e gêneros com autenticação JWT e controle de acesso baseado em roles.

## 🚀 Tecnologias Utilizadas

- **Java 21** + **Spring Boot 3.3.3**
- **PostgreSQL 16** (banco de dados)
- **Spring Data JPA** (persistência)
- **Spring Security** + **JWT** (autenticação/autorização)
- **Liquibase** (versionamento do banco)
- **SpringDoc OpenAPI 3** (documentação/Swagger)
- **Docker** + **Docker Compose** (containerização)
- **Maven** (gerenciamento de dependências)

## 🏗️ Arquitetura

```
br.com.lucasbrum.booksapi/
├── controller/           # Controllers REST (v1)
├── domain/
│   ├── entity/          # Entidades JPA (Author, Book, Genre, User)
│   ├── repository/      # Repositories Spring Data
│   └── specifications/ # JPA Specifications para consultas complexas
├── service/             # Camada de serviços
├── dto/v1/             # Data Transfer Objects (versionados)
├── mapper/             # Conversores Entity ↔ DTO
├── exception/          # Tratamento global de exceções
└── config/             # Configurações (Security, OpenAPI, etc.)
```

## ⚡ Como Rodar a Aplicação

### Pré-requisitos
- **Docker** e **Docker Compose** instalados
- **Git** para clonar o repositório

### 1. Clone o Repositório
```bash
git clone <URL_DO_REPOSITORIO>
cd books-api
```

### 2. Configure as Variáveis de Ambiente
```bash
# Copie o arquivo de exemplo e ajuste se necessário
cp .env.example .env
```

### 3. Execute com Docker Compose
```bash
# Inicia todos os serviços (PostgreSQL + API)
docker-compose up -d

# Acompanhe os logs (opcional)
docker-compose logs -f api
```

### 4. Verifique se está funcionando
```bash
# Health check
curl http://localhost:8080/actuator/health

# Deve retornar: {"status":"UP"}
```

## 🔐 Credenciais de Teste

A aplicação possui **3 usuários** pré-cadastrados para testes:

| Usuário | Senha   | Roles           | Permissões                    |
|---------|---------|-----------------|-------------------------------|
| `admin` | `123456`| READER, WRITER  | 🟢 Todas as operações CRUD   |
| `writer`| `123456`| READER, WRITER  | 🟢 Todas as operações CRUD   |
| `reader`| `123456`| READER          | 🟡 Apenas operações de leitura|

## 📖 Documentação da API (Swagger)

### Acesso ao Swagger UI
```
http://localhost:8080/swagger-ui/index.html
```

### Como Autenticar no Swagger:
1. **Fazer Login**: Use o endpoint `/auth/login` com as credenciais acima
2. **Copiar Token**: Pegue o `access_token` da resposta
3. **Autorizar**: Clique no botão **🔒 Authorize** no topo do Swagger
4. **Colar Token**: Insira o token (sem prefixo "Bearer")
5. **Testar**: Agora você pode usar todos os endpoints protegidos

### Outros Formatos da Documentação:
- **JSON**: http://localhost:8080/v3/api-docs
- **YAML**: http://localhost:8080/v3/api-docs.yaml

## 📋 Endpoints Disponíveis

### 🔐 **Autenticação**
- `POST /auth/login` - Fazer login
- `POST /auth/register` - Registrar novo usuário (role READER por padrão)

### 👨‍💼 **Autores** (`/api/v1/authors`)
- `GET /` - Listar autores (paginado + filtros)
- `POST /` - Criar autor *(requer WRITER)*
- `GET /{id}` - Buscar por ID
- `PUT /{id}` - Atualizar *(requer WRITER)*
- `DELETE /{id}` - Deletar *(requer WRITER)*

### 📖 **Livros** (`/api/v1/books`)
- `GET /` - Listar livros (paginado + filtros)
- `POST /` - Criar livro *(requer WRITER)*
- `GET /{id}` - Buscar por ID
- `PUT /{id}` - Atualizar *(requer WRITER)*
- `DELETE /{id}` - Deletar *(requer WRITER)*

### 🏷️ **Gêneros** (`/api/v1/genres`)
- `GET /` - Listar gêneros (paginado + filtros)
- `POST /` - Criar gênero *(requer WRITER)*
- `GET /{id}` - Buscar por ID
- `PUT /{id}` - Atualizar *(requer WRITER)*
- `DELETE /{id}` - Deletar *(requer WRITER)*

## 🧪 Exemplos de Uso

### 1. Fazer Login
```bash
curl -X POST http://localhost:8080/auth/login \\
  -H "Content-Type: application/json" \\
  -d '{
    "username": "admin",
    "password": "123456"
  }'
```

**Resposta:**
```json
{
  "access_token": "eyJhbGciOiJIUzUxMiJ9...",
  "token_type": "Bearer",
  "expires_in": 3600,
  "username": "admin",
  "roles": ["READER", "WRITER"]
}
```

### 2. Criar um Autor
```bash
curl -X POST http://localhost:8080/api/v1/authors \\
  -H "Authorization: Bearer SEU_TOKEN_AQUI" \\
  -H "Content-Type: application/json" \\
  -d '{
    "name": "J.K. Rowling",
    "biography": "Autora britânica, criadora da série Harry Potter"
  }'
```

### 3. Criar um Livro
```bash
curl -X POST http://localhost:8080/api/v1/books \\
  -H "Authorization: Bearer SEU_TOKEN_AQUI" \\
  -H "Content-Type: application/json" \\
  -d '{
    "title": "Harry Potter e a Pedra Filosofal",
    "description": "Primeiro livro da série Harry Potter",
    "isbn": "978-85-325-1101-4",
    "publishedDate": "1997-06-26",
    "authorId": 1,
    "genreId": 1
  }'
```

### 4. Listar Livros com Filtros
```bash
# Buscar por título
curl "http://localhost:8080/api/v1/books?title=Harry"

# Paginação
curl "http://localhost:8080/api/v1/books?page=0&size=10&sort=title,asc"

# Por autor
curl "http://localhost:8080/api/v1/books?authorId=1"
```

## 🛠️ Comandos de Desenvolvimento

### Build e Testes
```bash
# Compilar
./mvnw clean compile

# Executar testes
./mvnw test

# Gerar JAR
./mvnw package

# Executar localmente (sem Docker)
./mvnw spring-boot:run
```

### Docker
```bash
# Rebuild da API
docker-compose up --build api

# Logs
docker-compose logs -f api
docker-compose logs -f postgres

# Parar serviços
docker-compose down

# Limpar dados do banco (cuidado!)
docker-compose down -v
```

### Banco de Dados
```bash
# Conectar no PostgreSQL
docker exec -it books_postgres psql -U books_user -d books

# Ver tabelas
\\dt

# Reset completo (apaga dados)
docker-compose down -v && docker-compose up -d
```

## 📊 Banco de Dados

### Esquema Principal:
- **authors** - Autores com biografia
- **genres** - Gêneros com descrição
- **books** - Livros (referencia autor e gênero)
- **users** - Usuários para autenticação

### Migrations:
- Gerenciadas pelo **Liquibase**
- Arquivos em: `src/main/resources/db/changelog/`
- Execução automática no startup

## 🔧 Configurações

### Variáveis de Ambiente:
```bash
SPRING_PROFILES_ACTIVE=dev    # Profile ativo
APP_PORT=8080                 # Porta da aplicação
DB_HOST=localhost             # Host do PostgreSQL
DB_PORT=5432                  # Porta do PostgreSQL
DB_NAME=books                 # Nome do banco
DB_USER=books_user            # Usuário do banco
DB_PASSWORD=books_password    # Senha do banco
JWT_SECRET=your-secret-key    # Chave para JWT (min. 32 chars)
```

### Profiles:
- **dev**: Configurações para desenvolvimento (logs SQL, etc.)

## 🎯 Para Avaliadores

### Cenários de Teste Sugeridos:

1. **Autenticação**:
   - Login com usuários diferentes
   - Teste de permissões (reader vs writer)

2. **CRUD Completo**:
   - Criar autor → Criar gênero → Criar livro
   - Listar com paginação e filtros
   - Atualizar e deletar

3. **Segurança**:
   - Acessar endpoints sem token (deve dar 401)
   - Usar token expirado
   - Tentar operações de escrita com usuário reader

4. **Validações**:
   - Enviar dados inválidos (campos obrigatórios, formatos)

### Comandos Rápidos para Avaliação:
```bash
# 1. Clonar e rodar
git clone <repo> && cd books-api
docker-compose up -d

# 2. Testar API
curl http://localhost:8080/actuator/health

# 3. Acessar Swagger
open http://localhost:8080/swagger-ui/index.html

# 4. Ver logs
docker-compose logs -f api
```

## 📄 Licença

Este projeto está sob a licença **MIT**. Veja o arquivo `LICENSE` para mais detalhes.

---

**Desenvolvido por Lucas Brum** 🚀
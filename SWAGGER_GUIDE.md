# 📋 **DOCUMENTAÇÃO SWAGGER - BOOKS API**

## 🚀 **Como Acessar a Documentação**

### **1. Swagger UI (Interface Interativa)**
```
http://localhost:8080/swagger-ui/index.html
```

### **2. OpenAPI JSON (Especificação Raw)**
```
http://localhost:8080/v3/api-docs
```

### **3. OpenAPI YAML**
```
http://localhost:8080/v3/api-docs.yaml
```

## 🔐 **Autenticação no Swagger**

### **Passo 1: Fazer Login**
1. Acesse o endpoint `/auth/login`
2. Use as credenciais de teste:
   ```json
   {
     "username": "admin",
     "password": "123456"
   }
   ```

### **Passo 2: Configurar Token**
1. Copie o `access_token` da resposta
2. Clique no botão **"Authorize"** (🔒) no topo da página
3. Cole o token no campo (sem prefixo "Bearer ")
4. Clique em **"Authorize"**

### **Usuários de Teste Disponíveis**
```json
{
  "reader": {
    "username": "reader",
    "password": "123456",
    "roles": ["READER"]
  },
  "writer": {
    "username": "writer", 
    "password": "123456",
    "roles": ["READER", "WRITER"]
  },
  "admin": {
    "username": "admin",
    "password": "123456", 
    "roles": ["READER", "WRITER"]
  }
}
```

## 📚 **Endpoints Disponíveis**

### **🔐 Autenticação**
- `POST /auth/login` - Fazer login
- `POST /auth/register` - Registrar usuário

### **👨‍💼 Autores**
- `GET /api/v1/authors` - Listar autores (paginado + filtros)
- `POST /api/v1/authors` - Criar autor (requer WRITER)
- `GET /api/v1/authors/{id}` - Buscar autor por ID
- `PUT /api/v1/authors/{id}` - Atualizar autor (requer WRITER)
- `DELETE /api/v1/authors/{id}` - Deletar autor (requer WRITER)

### **📖 Livros**
- `GET /api/v1/books` - Listar livros (paginado + filtros)
- `POST /api/v1/books` - Criar livro (requer WRITER)
- `GET /api/v1/books/{id}` - Buscar livro por ID
- `PUT /api/v1/books/{id}` - Atualizar livro (requer WRITER)
- `DELETE /api/v1/books/{id}` - Deletar livro (requer WRITER)

### **🏷️ Gêneros**
- `GET /api/v1/genres` - Listar gêneros (paginado + filtros)
- `POST /api/v1/genres` - Criar gênero (requer WRITER)
- `GET /api/v1/genres/{id}` - Buscar gênero por ID
- `PUT /api/v1/genres/{id}` - Atualizar gênero (requer WRITER)
- `DELETE /api/v1/genres/{id}` - Deletar gênero (requer WRITER)

## 🛡️ **Permissões**

### **READER**
- ✅ Pode fazer **GET** em todos os endpoints de livros, autores e gêneros

### **WRITER** 
- ✅ Pode fazer **GET** em todos os endpoints
- ✅ Pode fazer **POST**, **PUT**, **DELETE** em todos os endpoints

## 🎯 **Exemplos de Uso**

### **Criar um Autor**
```json
{
  "name": "J.K. Rowling",
  "biography": "Autora britânica, criadora da série Harry Potter"
}
```

### **Criar um Gênero**
```json
{
  "name": "Fantasia",
  "description": "Livros com elementos mágicos e fantásticos"
}
```

### **Criar um Livro**
```json
{
  "title": "Harry Potter e a Pedra Filosofal",
  "description": "Primeiro livro da série Harry Potter",
  "isbn": "978-85-325-1101-4",
  "publishedDate": "1997-06-26",
  "authorId": 1,
  "genreId": 1
}
```

## 🔧 **Configurações**

### **Paginação**
- `page`: Número da página (começando em 0)
- `size`: Tamanho da página (padrão: 20)
- `sort`: Campo para ordenação (ex: "name,asc")

### **Filtros**
- **Autores**: `name` (busca parcial)
- **Livros**: `title`, `authorId`, `genreId`
- **Gêneros**: `name` (busca parcial)

---

## 🚨 **Status Atual**
**⚠️ NOTA**: Atualmente todos os endpoints estão temporariamente liberados (sem autenticação) para facilitar os testes. Em produção, a autenticação JWT estará ativa.

# ChainDonate API

Backend da plataforma **ChainDonate**, que permite doações transparentes com rastreamento on-chain via Bitcoin.

## ⚙️ Tecnologias

* Java 17 + Spring Boot
* Spring Security + JWT
* PostgreSQL
* Docker + Docker Compose
* Swagger/OpenAPI 3 (documentação interativa)
* RESTful API

---

## 🚀 Como rodar localmente

### 1. Clone o projeto

```bash
git clone https://github.com/luizsolely/chain-donate.git
cd chain-donate
```

### 2. Crie o arquivo `.env`

```bash
cp .env.example .env
```

Edite `.env` com suas variáveis reais.

### 3. Suba os containers com Docker

```bash
docker-compose up --build
```

Acesse:

* API: [http://localhost:8080](http://localhost:8080)
* Swagger UI: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

---

## 🛡️ Autenticação JWT

Para usar endpoints protegidos:

1. Gere um token via endpoint `/auth/login`
2. Clique em `Authorize` no Swagger UI
3. Cole o token (sem `Bearer `, o Swagger adiciona automaticamente)

---

## 📄 Documentação da API

Acesse a documentação Swagger em:
📍 [`/swagger-ui.html`](http://localhost:8080/swagger-ui.html)

---

## 📂 Arquivo `.env.example`

O arquivo `.env.example` contém as variáveis de ambiente necessárias para execução local e pode ser usado como base para criar seu `.env` real:

```env
# Banco de dados PostgreSQL
SPRING_DATASOURCE_URL=jdbc:postgresql://chaindonate-db:5432/chaindonate
SPRING_DATASOURCE_USERNAME=seu_usuario
SPRING_DATASOURCE_PASSWORD=sua_senha

POSTGRES_USER=seu_usuario
POSTGRES_PASSWORD=sua_senha

# JWT
JWT_SECRET=sua_chave_jwt_segura
JWT_EXPIRATION_MS=86400000
```


Este projeto foi criado como parte de um portfólio backend com foco em boas práticas. Contribuições são bem-vindas via *pull request* ou issues.

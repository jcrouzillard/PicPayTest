# 💳 PayAPI – Desafio Técnico PicPay

Projeto desenvolvido como parte de um estudo técnico com foco em backend Java, arquitetura de microsserviços e mensageria com Kafka.

---

## 🖼️ Arquitetura da Solução
![Arquitetura](docs/images/image-picpay.png)

---

## 📦 Stack utilizada

- Java 21
- Spring Boot 3
- Spring Data JPA
- PostgreSQL (via Docker)
- Apache Kafka (via Docker)
- Lombok
- Swagger / OpenAPI
- Docker Compose
- Bean Validation (Jakarta)

---

## 📂 Estrutura do Projeto

```
src/
├── main/
│   ├── java/com.julien.payapi/
│   │   ├── controller/        # REST Controllers
│   │   ├── dto/               # Data Transfer Objects
│   │   ├── entity/            # JPA Entities e Enums
│   │   ├── kafka/             # Kafka Producer e Consumer
│   │   ├── repository/        # Interfaces JPA
│   │   ├── service/           # Regras de negócio
│   │   └── PayApiApplication  # Classe principal
│   └── resources/
│       ├── application.yml              # Configuração base
│       ├── application-dev.yml         # Ambiente de desenvolvimento
│       └── application-prod.yml        # Ambiente de produção
├── docker-compose.yml
└── pom.xml
```

---

## 🚀 Como executar

### 1. Subir infraestrutura com Docker

```bash
docker-compose up -d
```

### 2. Rodar a aplicação

```bash
./mvnw spring-boot:run
```

---

## ✅ Funcionalidades

- Criar pagamento (`POST /payments`)
- Buscar pagamento por ID (`GET /payments/{id}`)
- Buscar pagamento por token (`GET /payments/token/{token}`)
- Enviar eventos de pagamento para Kafka
- Consumir eventos de Kafka
- Gerar `token` único para cada pagamento
- Suporte a múltiplos tipos de pagamento: `PIX`, `BOLETO`, `CREDIT_CARD`, `DEBIT_CARD`
- Validações com Jakarta Bean Validation

---

## 📌 Segurança e boas práticas

- Uso de `UUID` como token público no payload
- Camada de DTO para abstrair a entidade
- Enum `PaymentType` validado via `@NotNull`
- Configuração de ambientes separada via YAML
- Integração segura com Kafka usando `JsonSerializer` e `JsonDeserializer`

---

## 📬 Exemplo de requisição (POST /payments)

```bash
curl --location 'http://localhost:8080/payments' \
--header 'Content-Type: application/json' \
--data '{
  "description": "Assinatura Mensal",
  "amount": 89.99,
  "type": "PIX"
}'
```

### Resposta esperada

```json
{
  "id": 1,
  "description": "Assinatura Mensal",
  "amount": 89.99,
  "status": "PENDING",
  "type": "PIX",
  "token": "d93f25ab-f820-4aeb-9f3c-b109e884fc1b",
  "createdAt": "2025-07-20T18:00:00"
}
```

---

## 🔍 Observações

- O campo `type` é **obrigatório** e deve conter um valor válido entre: `PIX`, `BOLETO`, `CREDIT_CARD`, `DEBIT_CARD`
- Validações com `@NotBlank`, `@DecimalMin`, `@NotNull` são aplicadas no DTO
- Em caso de erro de validação, retorna HTTP 400 com mensagem detalhada

---

Desenvolvido para fins de estudo técnico em entrevista técnica PicPay 💚
---

## 🔐 Novas Funcionalidades e Melhorias

### ✅ Tipos de Pagamento (`type`)
A API agora aceita o campo obrigatório `type` no payload de criação de pagamento com os seguintes valores válidos:
- `PIX`
- `BOLETO`
- `CREDIT_CARD`
- `DEBIT_CARD`

> Exemplo:
```json
{
  "description": "Assinatura",
  "amount": 99.90,
  "type": "PIX"
}
```

### 🔑 Token único
Cada pagamento recebe um `token` do tipo UUID que pode ser utilizado para consultas seguras por:
- `GET /payments/token/{token}`

### 📋 Validação com Jakarta Bean Validation
As requisições são validadas com anotações como:
- `@NotNull`, `@NotBlank`, `@DecimalMin`
- Respostas HTTP 400 retornam mensagens claras de erro de campo

---

## 🧪 Exemplo com validação e token

```bash
curl --location 'http://localhost:8080/payments' \
--header 'Content-Type: application/json' \
--data '{
  "description": "Premium",
  "amount": 49.99,
  "type": "PIX"
}'
```

Resposta:
```json
{
  "id": 1,
  "description": "Premium",
  "amount": 49.99,
  "status": "PENDING",
  "type": "PIX",
  "token": "1f45e9ba-7b90-40c2-872a-df0176ac11f4",
  "createdAt": "2025-07-20T18:00:00"
}
```
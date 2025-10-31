# Backend API - Locação App

API REST para gerenciamento de locações. 

## Tecnologias:

- Java 21
- Spring Boot 3
- Spring Security (JWT)
- Flyway
- JPA / Hibernate
- MySQL
- Docker / Docker Compose
## Requisitos

* Docker (v20.10 ou superior)

## Como Rodar o Projeto

1.  **Clonar o repositório:**
    ```bash
    git clone https://github.com/gabrielyorlando/locacao-app
    ```

2.  **Entrar na pasta do projeto:**
    ```bash
    cd locacao-app
    ```

3.  **Subir os contêineres (Banco e Aplicação):**
    ```bash
    docker compose up
    ```

A aplicação estará disponível em `http://localhost:9090`.

## Autenticação (JWT)

A API utiliza autenticação via JWT.

1.  Para obter um token, faça uma requisição `POST` para a rota de login:
    `POST http://localhost:9090/login`

    **Body:**
    ```json
    {
      "username": "testeUser",
      "password": "teste123"
    }
    ```

2.  A resposta conterá o token de acesso.

3.  Use este token em todas as requisições futuras no header `Authorization`:
    `Authorization: Bearer <token_recebido>`

### Credenciais Padrão

* **Usuário:** `testeUser`
* **Senha:** `teste123`

## Monitoramento (Actuator)

O projeto utiliza o Spring Boot Actuator para expor endpoints de monitoramento e gerenciamento.

* **Verificar Saúde (Health Check):**
  Para verificar se a aplicação está no ar (status `UP`):
  `GET http://localhost:9090/actuator/health`

* **Endpoints Principais:**
  O endpoint base é `http://localhost:9090/actuator`. Por padrão, apenas `/health` é exposto. Para expor outros endpoints úteis (como `metrics`, `info`, `env`), ajuste seu `application.yml`:
    ```yaml
    management:
      endpoints:
        web:
          exposure:
            include: "health,info,metrics,env" # Ou "*" para todos
    ```

## Documentação e Testes

* **Swagger UI:**
`http://localhost:9090/doc`

* **Postman Collection:**
  https://api.postman.com/collections/25720030-ef5a2dcc-34b7-4e5d-baee-32041f8fa682?access_key=PMAT-01K8Y7JF1624X87091YJC4HR3K
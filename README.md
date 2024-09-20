# Spring Boot API - Gerenciamento de Wishlist

Este é um projeto que utiliza **Spring Boot** para criar uma API REST de gerenciamento de wishlist. A aplicação permite criar, buscar, verificar e excluir produtos de uma determinada wishlist.

## Tecnologias Utilizadas

- Java 21
- Spring Boot 3.2.9
- Spring Data Mongo
- Spring Security 6.2.6
- JWT 4.2.1
- Lombok
- Maven

## Funcionalidades

- Criação de usuário
- Autenticação para receber o token de acesso
- Busca da lista de produtos disponíveis
- Cadastro de produtos na Wishlist
- Listagem de todos os produtos da Wishlist
- Verificação se um produto está na Wishlist
- Remoção de um produto da Wishlist

## Execução

### Passos para rodar o projeto localmente

1. Clone o repositório:
    ```bash
    git clone https://github.com/analuizacarvalho/wishlist.git
    ```

2. Importe no Intellij:
 
3. Execute o :
   ```bash
   mvn clean install
   
4. Crie um Application simples e execute

5. A aplicação estará disponível em `http://localhost:8080`.

## Endpoints

### Authorization

| Método | Endpoint            | Descrição         | Necessario Token |
|--------|---------------------|-------------------|------------------|
| POST   | `/api/auth/login`   | Loga o usuário    | Não              |

### Users

| Método | Endpoint            | Descrição                | Necessario Token |
|--------|----------------------|-------------------------|------------------|
| POST    | `/api/users/register`| Cria um novo usuario   | Não              |

### Products

| Método | Endpoint            | Descrição                 | Necessario Token |
|--------|---------------------|---------------------------|------------------|
| GET    | `/api/products`      | Lista todos os produtos  | Sim              |

### Wishlist

| Método | Endpoint                           | Descrição                                                     | Necessario Token |
|--------|------------------------------------|---------------------------------------------------------------|------------------|
| GET    | `/api/wishlist`                    | Lista todos os produtos que estão na Wishlist do usuário      | Sim              |
| GET    | `/api/wishlist/{productId}/exists` | Verifica se um determinado produto está na Wishlist do usuário| Sim              |
| POST   | `/api/wishlist/{productId}`        | Adiciona um produto na Wishlist do usuário                    | Sim              |
| DELETE | `/api/wishlist/{productId}`        | Remove um produto da Wishlist do usuário                      | Sim              |

### Observações

Para os endpoint que precisam de autenticação, o token deve ir como **Bearer Token**.

Ex:

Prefixo: Bearer

Token: eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0aGFpYW5lcmliZWlyb0BnbWFpbC5jb20iLCJpc3MiOiJsdWl6YWxhYnMtYXBpIiwiZXhwIjoxNzI2ODA2NzYwfQ.1JnMO4F1f-7wsT5PO2x_xnfQfHSJZi_mVsSfmE4XZ7k


## Configuração do Banco de Dados

O projeto está configurado para usar o banco de dados **MongoDB** Atlas, sendo gerenciado em nuvem. Caso queira trocar para outro banco, alterar a linha de configuração no application.properties

1. Altere a informação:
    ```properties
    spring.data.mongodb.uri
    ```

## Testes

Para executar os testes unitários, utilize o comando:

```bash
mvn test
# TaskAPI - API de Gerenciamento de Tarefas

![Java](https://img.shields.io/badge/Java-17-blue)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.2.5-green)
![Maven](https://img.shields.io/badge/Maven-4.0.0-red)
![Security](https://img.shields.io/badge/Security-JWT-blueviolet)

Uma aplicação web simples para gerenciamento de tarefas com autenticação de usuário baseada em Token JWT, construída com Spring Boot para o back-end e HTML/CSS/JS para o front-end.

## Tabela de Conteúdos

1.  [Sobre o Projeto](#sobre-o-projeto)
2.  [Funcionalidades](#funcionalidades)
3.  [Tecnologias Utilizadas](#tecnologias-utilizadas)
4.  [Como Executar](#como-executar)
5.  [Endpoints da API](#endpoints-da-api)
6.  [Estrutura do Projeto](#estrutura-do-projeto)

## Sobre o Projeto

O **TaskAPI** foi desenvolvido para demonstrar a implementação de um sistema de autenticação seguro em uma API REST usando Spring Security e JSON Web Tokens (JWT). A aplicação permite que usuários se cadastrem, façam login e gerenciem uma lista de tarefas pessoais.

O front-end é servido diretamente pelo back-end Spring Boot, criando uma aplicação web autocontida.

## Funcionalidades

-   ✅ **Autenticação de Usuários**: Sistema completo de registro e login.
-   🔐 **Segurança com JWT**: Endpoints protegidos que só podem ser acessados com um token válido.
-   📝 **Gerenciamento de Tarefas**: Usuários autenticados podem criar e visualizar suas próprias tarefas.
-   💾 **Banco de Dados em Memória**: Utiliza H2 Database para facilitar a execução e os testes sem a necessidade de um banco de dados externo.
-   🖥️ **Interface Simples**: Front-end intuitivo para interagir com a API.

## Tecnologias Utilizadas

#### **Back-end**
-   **Java 17**
-   **Spring Boot 3.2.5**
-   **Spring Security**: Para a camada de autenticação e autorização.
-   **Spring Data JPA**: Para a persistência de dados.
-   **JWT (JSON Web Token)**: Biblioteca `io.jsonwebtoken` para geração e validação de tokens.
-   **H2 Database**: Banco de dados relacional em memória.
-   **Maven**: Gerenciador de dependências e build.

#### **Front-end**
-   HTML5
-   CSS3
-   JavaScript (Vanilla JS)

## Como Executar

Siga os passos abaixo para executar a aplicação localmente.

#### **Pré-requisitos**

-   **JDK 17** ou superior instalado.
-   **Apache Maven** instalado e configurado no PATH do sistema.

#### **Passos para Instalação**

1.  **Clone o repositório** (ou baixe o código-fonte) para a sua máquina.

2.  **Abra um terminal** na pasta raiz do projeto (`TaskAPI/`).

3.  **Execute o comando Maven** para iniciar a aplicação:
    ```bash
    mvn spring-boot:run
    ```

4.  **Acesse a aplicação** no seu navegador:
    [http://localhost:8080](http://localhost:8080)

5.  **(Opcional) Acesse o Console do H2 Database** para visualizar o banco de dados em tempo real:
    * URL: [http://localhost:8080/h2-console](http://localhost:8080/h2-console)
    * **JDBC URL**: `jdbc:h2:mem:testdb`
    * **User Name**: `sa`
    * **Password**: (deixe em branco)

## Endpoints da API

A seguir estão os endpoints RESTful disponíveis na aplicação.

### Autenticação (`/auth`)

| Método | Endpoint           | Descrição                                 | Corpo da Requisição (JSON)             |
| :----- | :----------------- | :---------------------------------------- | :------------------------------------- |
| `POST` | `/auth/register`   | Registra um novo usuário.                 | `{ "username": "user", "password": "123" }` |
| `POST` | `/auth/login`      | Autentica um usuário e retorna um token JWT. | `{ "username": "user", "password": "123" }` |

### Tarefas (`/tasks`)

*Obs: Todos os endpoints de tarefas são protegidos e exigem um cabeçalho de autorização.*
`Authorization: Bearer <seu-token-jwt>`

| Método | Endpoint       | Descrição                                 | Corpo da Requisição (JSON)                |
| :----- | :------------- | :---------------------------------------- | :---------------------------------------- |
| `GET`  | `/tasks/list`  | Retorna a lista de tarefas do usuário logado. | N/A                                       |
| `POST` | `/tasks/post`  | Cria uma nova tarefa para o usuário logado.   | `{ "title": "Nova Tarefa", "description": "Descrição..." }` |

## Estrutura do Projeto

```
TaskAPI/
├── src
│   ├── main
│   │   ├── java/com/Token
│   │   │   ├── config/         # Configurações do Spring Security e JWT Filter
│   │   │   ├── controllers/    # Controladores da API (Endpoints)
│   │   │   ├── DTOs/           # Data Transfer Objects
│   │   │   ├── models/         # Entidades JPA (User, Task)
│   │   │   ├── repositories/   # Repositórios Spring Data
│   │   │   ├── services/       # Lógica de negócio
│   │   │   └── utils/          # Utilitários (JwtUtil)
│   │   └── resources
│   │       ├── static/         # Arquivos do Front-end (HTML, CSS, JS)
│   │       └── application.properties # Configurações da aplicação
│   └── test/...
└── pom.xml                   # Dependências e build do Maven
```
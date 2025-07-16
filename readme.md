# Autores

- **Lucas Pinheiro Caldas** (20220040190)
- **Matheus Queiroz de Almeida Pereira** (20220055380)

# TaskAPI - API de Gerenciamento de Tarefas

![Java](https://img.shields.io/badge/Java-17-blue)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.2.5-green)
![Maven](https://img.shields.io/badge/Maven-4.0.0-red)
![Security](https://img.shields.io/badge/Security-JWT-blueviolet)
![CI/CD](https://img.shields.io/badge/CI/CD-GitLab-orange)

Uma aplicação web simples para gerenciamento de tarefas com autenticação de usuário baseada em Token JWT, construída com Spring Boot para o back-end e HTML/CSS/JS para o front-end.

Este projeto está configurado com um pipeline de Integração Contínua e Entrega Contínua (CI/CD) usando GitLab CI.

## Tabela de Conteúdos

1.  [Sobre o Projeto](#sobre-o-projeto)
2.  [Funcionalidades](#funcionalidades)
3.  [Tecnologias Utilizadas](#tecnologias-utilizadas)
4.  [Como Executar Localmente](#como-executar-localmente)
5.  [Pipeline de CI/CD](#pipeline-de-cicd)
6.  [Endpoints da API](#endpoints-da-api)
7.  [Estrutura do Projeto](#estrutura-do-projeto)

## Sobre o Projeto

O **TaskAPI** foi desenvolvido para demonstrar a implementação de um sistema de autenticação seguro em uma API REST usando Spring Security e JSON Web Tokens (JWT). A aplicação permite que usuários se cadastrem, façam login e gerenciem uma lista de tarefas pessoais.

O front-end é servido diretamente pelo back-end Spring Boot, criando uma aplicação web autocontida.

## Funcionalidades

- ✅ **Autenticação de Usuários**: Sistema completo de registro e login.
- 🔐 **Segurança com JWT**: Endpoints protegidos que só podem ser acessados com um token válido.
- 📝 **Gerenciamento de Tarefas**: Usuários autenticados podem criar e visualizar suas próprias tarefas.
- 💾 **Banco de Dados em Memória**: Utiliza H2 Database para facilitar a execução e os testes sem a necessidade de um banco de dados externo.
- 🐳 **Containerização com Docker**: `Dockerfile` otimizado com múltiplos estágios.
- 🚀 **CI/CD com GitLab**: Pipeline automatizado para build, testes e publicação de imagem Docker.

## Tecnologias Utilizadas

#### **Back-end**

- **Java 17**
- **Spring Boot 3.2.5**
- **Spring Security**: Para a camada de autenticação e autorização.
- **Spring Data JPA**: Para a persistência de dados.
- **JWT (JSON Web Token)**: Biblioteca `io.jsonwebtoken` para geração e validação de tokens.
- **H2 Database**: Banco de dados relacional em memória.
- **Maven**: Gerenciador de dependências e build.

#### **Front-end**

- HTML5
- CSS3
- JavaScript (Vanilla JS)

#### **DevOps**

- **Docker**: Para containerização.
- **GitLab CI/CD**: Para automação do pipeline.

## Como Executar Localmente

Siga os passos abaixo para executar a aplicação na sua máquina.

#### **Pré-requisitos**

- **JDK 17** ou superior instalado.
- **Apache Maven** instalado e configurado no PATH do sistema.

#### **Passos para Instalação**

1.  **Clone o repositório** para a sua máquina.

2.  **Abra um terminal** na pasta raiz do projeto (`TaskAPI/`).

3.  **Execute o comando Maven** para iniciar a aplicação:

    ```bash
    mvn spring-boot:run
    ```

4.  **Acesse a aplicação** no seu navegador:
    [http://localhost:8080](http://localhost:8080)

5.  **(Opcional) Acesse o Console do H2 Database** para visualizar o banco de dados em tempo real:
    - URL: [http://localhost:8080/h2-console](http://localhost:8080/h2-console)
    - **JDBC URL**: `jdbc:h2:mem:testdb`
    - **User Name**: `sa`
    - **Password**: (deixe em branco)

---

## Pipeline de CI/CD

Este projeto utiliza GitLab CI/CD para automatizar o processo de integração e entrega. O pipeline está definido no arquivo `.gitlab-ci.yml` e é dividido em três estágios sequenciais:

1.  **`build`**:

    - **O que faz?** Este estágio utiliza o `build-job` para compilar o código-fonte e empacotar a aplicação em um arquivo `.jar`, pulando a execução dos testes (`-DskipTests`). O `.jar` resultante é salvo como um artefato para ser usado nos estágios seguintes.
    - **Gatilho**: Executado a cada `push` para qualquer branch no repositório.

2.  **`test`**:

    - **O que faz?** O `test-job` é responsável por executar todos os testes automatizados do projeto (`mvn test`). Ele garante que as novas alterações não quebraram nenhuma funcionalidade existente.
    - **Gatilho**: Executado após o estágio de `build` ser concluído com sucesso.

3.  **`package`**:
    - **O que faz?** O `package-job` constrói a imagem Docker da aplicação utilizando o `Dockerfile`. Em seguida, ele faz a autenticação no registro de contêineres do GitLab e publica a nova imagem.
    - **Gatilho**: Executado após o estágio de `build` ser concluído com sucesso. A imagem gerada é marcada com o nome da branch que acionou o pipeline (ex: `main`, `develop`).

### Como Acompanhar o Pipeline

1.  Após fazer um `push` para o seu repositório no GitLab, vá para a seção **CI/CD > Pipelines**.
2.  Clique no pipeline mais recente para ver a execução de cada estágio (`build`, `test`, `package`) em tempo real.
3.  Ao final da execução bem-sucedida, a imagem Docker estará disponível em **Deploy > Container Registry**.

### Dockerfile

O `Dockerfile` utiliza uma **abordagem de múltiplos estágios** para otimizar a imagem final:

- **Estágio `builder`**: Usa uma imagem completa do Maven para compilar o projeto. Isso gera o arquivo `.jar` executável.
- **Estágio final**: Usa uma imagem JRE (Java Runtime Environment) muito mais leve, que apenas contém o necessário para rodar a aplicação. O `.jar` do estágio anterior é copiado para esta imagem, resultando em uma imagem final menor, mais rápida e mais segura.

---

## Endpoints da API

A seguir estão os endpoints RESTful disponíveis na aplicação.

### Autenticação (`/auth`)

| Método | Endpoint         | Descrição                                    | Corpo da Requisição (JSON)                  |
| :----- | :--------------- | :------------------------------------------- | :------------------------------------------ |
| `POST` | `/auth/register` | Registra um novo usuário.                    | `{ "username": "user", "password": "123" }` |
| `POST` | `/auth/login`    | Autentica um usuário e retorna um token JWT. | `{ "username": "user", "password": "123" }` |

### Tarefas (`/tasks`)

_Obs: Todos os endpoints de tarefas são protegidos e exigem um cabeçalho de autorização._
`Authorization: Bearer <seu-token-jwt>`

| Método | Endpoint      | Descrição                                     | Corpo da Requisição (JSON)                                  |
| :----- | :------------ | :-------------------------------------------- | :---------------------------------------------------------- |
| `GET`  | `/tasks/list` | Retorna a lista de tarefas do usuário logado. | N/A                                                         |
| `POST` | `/tasks/post` | Cria uma nova tarefa para o usuário logado.   | `{ "title": "Nova Tarefa", "description": "Descrição..." }`

... 
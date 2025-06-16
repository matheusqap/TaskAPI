# Autores

- **Lucas Pinheiro Caldas**
- **Matheus Queiroz de Almeida Pereira**

# TaskAPI - API de Gerenciamento de Tarefas

![Java](https://img.shields.io/badge/Java-17-blue)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.2.5-green)
![Maven](https://img.shields.io/badge/Maven-4.0.0-red)
![Security](https://img.shields.io/badge/Security-JWT-blueviolet)
![CI/CD](https://img.shields.io/badge/CI/CD-GitLab-orange)


## Sobre o Projeto
Uma aplicação web simples para gerenciamento de tarefas que permite que usuários se cadastrem, façam login e gerenciem uma lista de tarefas pessoais.

O **TaskAPI** foi desenvolvido para demonstrar a implementação de um sistema de autenticação seguro em uma API REST usando Spring Security e JSON Web Tokens (JWT), além disso a aplicação é totalmente containerizada com Docker e possui um pipeline de CI/CD configurado no GitLab. 

## Funcionalidades

- ✅ **Autenticação de Usuários**: Sistema completo de registro e login.
- 🔐 **Segurança com JWT**: Endpoints protegidos que só podem ser acessados com um token válido.
- 📝 **Gerenciamento de Tarefas**: Usuários autenticados podem criar e visualizar suas próprias tarefas.
- 💾 **Banco de Dados em Memória**: Utiliza H2 Database para facilitar a execução e os testes sem a necessidade de um banco de dados externo.
- 🐳 **Containerização com Docker**: `Dockerfile` otimizado com múltiplos estágios.
- 🚀 **CI/CD com GitLab**: Pipeline automatizado para build, testes e publicação de imagem Docker.

## Como Executar a Aplicação (GitLab Container Registry)
A aplicação está containerizada e pode ser executada facilmente usando Docker. Siga os passos abaixo para configurar e iniciar a aplicação localmente. A imagem está hospedada no registro do GitLab, facilitando o acesso e a execução.
#### **Passos para Instalação**

1. **Certifique-se de que o Docker está instalado** e em execução na sua máquina.
Baixando a imagem do gitlab registry

2.  **Abra um terminal** e execute o seguinte comando para baixar a imagem Docker:
    ```bash
    docker pull registry.gitlab.com/lucpc/taskapi:II-Unidade
    ```
3.  **Execute a imagem** com o seguinte comando:

    ```bash
    docker run -p 8080:8080 registry.gitlab.com/lucpc/taskapi:II-Unidade
    ```
4.  **Acesse a aplicação** no seu navegador:
    [http://localhost:8080](http://localhost:8080)

## Pipeline de CI/CD
Este projeto utiliza GitLab CI/CD para automatizar o processo de integração e entrega. O pipeline está definido no arquivo `.gitlab-ci.yml` e é dividido em três estágios sequenciais:

1.  **`build`**:

    - **O que faz?** Este estágio utiliza o `build-job` para compilar o código-fonte e empacotar a aplicação em um arquivo `.jar`, pulando a execução dos testes (`-DskipTests`) que só serão executados na fase de testes da pipeline. O `.jar` resultante é salvo como um artefato para ser usado nos estágios seguintes.
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


# Finance Manager - Gerenciador Financeiro Pessoal

Este é um projeto de gerenciador financeiro pessoal desenvolvido em Java com Spring Boot. Ele permite que os usuários controlem suas finanças, registrando receitas e despesas, gerando relatórios e acompanhando seu saldo.

## Funcionalidades Principais

*   **Cadastro e Autenticação de Usuários:** Os usuários podem criar contas e fazer login com segurança usando JWT (JSON Web Tokens).
*   **Registro de Transações:** Os usuários podem registrar suas receitas e despesas, especificando o tipo, valor, data e descrição.
*   **Relatórios Financeiros:** O sistema gera relatórios financeiros detalhados, mostrando o total de receitas, despesas, saldo do período e saldo final consolidado.
*   **Segurança:** A aplicação utiliza Spring Security para proteger os dados dos usuários e garantir a segurança das transações.

## Tecnologias Utilizadas

*   **Linguagem de Programação:** Java 17+
*   **Framework:** Spring Boot 3+
*   **Banco de Dados:** H2 (banco de dados em memória para desenvolvimento)
*   **Segurança:** Spring Security
*   **Autenticação:** JSON Web Tokens (JWT) com a biblioteca `jjwt`.
*   **Build Tool:** Maven
*   **Persistência:** Spring Data JPA (Hibernate)

## Pré-requisitos

Antes de começar, você precisará ter instalado em sua máquina:

*   **Java Development Kit (JDK):** Versão 17 ou superior.
*   **Maven:** Versão 3.6 ou superior.
*   **Um IDE de sua preferência:** IntelliJ IDEA, Eclipse, VS Code, etc.
*   **Uma ferramenta de teste de API:** Insomnia, Postman, etc.

## Configuração e Execução

Siga estas etapas para configurar e executar o projeto:

1.  **Clone o repositório:**
    ```bash
    git clone <https://github.com/GermanoNott/financemanager.git>
    cd financemananger
    ```

2.  **Compile e execute o projeto com Maven:**
    ```bash
    mvn spring-boot:run
    ```
    Isso irá baixar as dependências, compilar o código e iniciar a aplicação Spring Boot na porta `8080`.

3.  **Acesse a aplicação:**
    A aplicação é uma API REST. Use uma ferramenta como o Insomnia ou Postman para interagir com os endpoints.

    *   **H2 Console:** Para acessar o console do H2 (apenas para desenvolvimento), acesse `http://localhost:8080/h2-console` no seu navegador. Use as credenciais configuradas no arquivo `src/main/resources/application.properties`.
        *   **JDBC URL:** `jdbc:h2:mem:testdb`
        *   **Username:** `germanott`
        *   **Password:** (deixe em branco ou use a senha que definiu)

## Endpoints da API

A seguir estão os principais endpoints da API. Todas as rotas protegidas exigem um `Bearer Token` no cabeçalho `Authorization`.

*   `POST /auth/registro`
    *   **Descrição:** Registra um novo usuário.
    *   **Corpo da Requisição (JSON):**
        ```json
        {
            "nome": "Seu Nome",
            "email": "email@exemplo.com",
            "senha": "sua_senha"
        }
        ```

*   `POST /auth/login`
    *   **Descrição:** Autentica um usuário e retorna um token JWT.
    *   **Corpo da Requisição (JSON):**
        ```json
        {
            "email": "email@exemplo.com",
            "senha": "sua_senha"
        }
        ```

*   `POST /transacoes` **(Protegido)**
    *   **Descrição:** Cria uma nova transação para o usuário autenticado.
    *   **Corpo da Requisição (JSON):**
        ```json
        {
            "descricao": "Salário",
            "valor": 5000.00,
            "data": "2025-10-30",
            "tipo": "RECEITA"
        }
        ```

*   `GET /transacoes` **(Protegido)**
    *   **Descrição:** Lista todas as transações do usuário autenticado.

*   `GET /relatorios` **(Protegido)**
    *   **Descrição:** Gera um relatório financeiro completo para o usuário autenticado.

## Contribuição

Contribuições são bem-vindas! Se você encontrar algum problema ou tiver alguma sugestão de melhoria, sinta-se à vontade para abrir uma *issue* ou enviar um *pull request*.

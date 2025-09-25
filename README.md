# Sistema de Gestão de Projetos e Equipes

Uma aplicação web simples para gerenciar projetos, equipes e usuários, desenvolvida com o objetivo de demonstrar os conceitos de uma aplicação full-stack com o ecossistema Spring.

---

## Tecnologias Utilizadas

* **Backend:**
    * Java 17 (LTS)
    * Spring Boot 3.x
    * Spring MVC (para a camada de Controller)
    * Spring Data JPA / Hibernate (para a persistência de dados)
* **Frontend:**
    * Thymeleaf (para a renderização de páginas HTML no servidor)
* **Banco de Dados:**
    * PostgreSQL 15+
* **Build & Dependências:**
    * Apache Maven

---

## Pré-requisitos

Antes de começar, garanta que você tem as seguintes ferramentas instaladas e configuradas no seu sistema:

1.  **JDK 17 ou superior:**
    * [Download via Adoptium](https://adoptium.net/pt-BR/temurin/releases/)
    * Verifique a instalação com `java -version` no terminal.
2.  **Apache Maven 3.8 ou superior:**
    * [Download via site oficial](https://maven.apache.org/download.cgi)
    * Verifique a instalação com `mvn -version` no terminal.
3.  **PostgreSQL 15 ou superior:**
    * [Download via EDB](https://www.enterprisedb.com/downloads/postgres-postgresql-downloads)
    * É recomendado ter a ferramenta gráfica **pgAdmin 4** instalada (geralmente vem junto com o instalador da EDB).
4.  **Git:**
    * [Download via site oficial](https://git-scm.com/downloads)
5.  **IDE (Opcional, mas recomendado):**
    * Visual Studio Code com o "Extension Pack for Java".
    * IntelliJ IDEA Community Edition.

---

## ⚙️ Configuração do Ambiente

Siga estes passos para preparar o ambiente antes de executar a aplicação pela primeira vez.

### 1. Clonar o Repositório
Abra um terminal e clone o projeto para a sua máquina local (se o código estiver em um repositório).
```bash
git clone <URL_DO_SEU_REPOSITORIO_GIT>
cd nome-da-pasta-do-projeto
```

### 2. Configurar o Banco de Dados
Você precisa criar o banco de dados e as tabelas que a aplicação irá usar.

**a) Criar o Banco de Dados:**
Abra o **pgAdmin 4**, conecte-se ao seu servidor e crie um novo banco de dados com o nome: `gestao_projetos`.

**b) Criar as Tabelas:**
Dentro do banco `gestao_projetos`, abra a **Query Tool** (Ferramenta de Consulta) e execute o script SQL abaixo para criar todas as tabelas necessárias:

```sql
-- Apaga as tabelas antigas se existirem, na ordem correta
DROP TABLE IF EXISTS projeto_equipes;
DROP TABLE IF EXISTS equipe_membros;
DROP TABLE IF EXISTS projetos;
DROP TABLE IF EXISTS equipes;
DROP TABLE IF EXISTS usuarios;

-- Cria a tabela de Usuários
CREATE TABLE usuarios (
    id SERIAL PRIMARY KEY,
    nome_completo VARCHAR(255) NOT NULL,
    cpf VARCHAR(14) UNIQUE NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    cargo VARCHAR(100),
    login VARCHAR(100) UNIQUE NOT NULL,
    senha VARCHAR(255) NOT NULL,
    perfil VARCHAR(50) NOT NULL
);

-- Cria a tabela de Equipes
CREATE TABLE equipes (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    descricao TEXT
);

-- Cria a tabela de Projetos
CREATE TABLE projetos (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    descricao TEXT,
    data_inicio DATE,
    data_termino_prevista DATE,
    status VARCHAR(50),
    id_gerente INTEGER,
    CONSTRAINT fk_gerente
        FOREIGN KEY(id_gerente) 
        REFERENCES usuarios(id)
);

-- Cria a tabela de associação Equipe <-> Membros(Usuários)
CREATE TABLE equipe_membros (
    id_equipe INTEGER REFERENCES equipes(id) ON DELETE CASCADE,
    id_usuario INTEGER REFERENCES usuarios(id) ON DELETE CASCADE,
    PRIMARY KEY (id_equipe, id_usuario)
);

-- Cria a tabela de associação Projeto <-> Equipes
CREATE TABLE projeto_equipes (
    projeto_id BIGINT REFERENCES projetos(id) ON DELETE CASCADE,
    equipe_id BIGINT REFERENCES equipes(id) ON DELETE CASCADE,
    PRIMARY KEY (projeto_id, equipe_id)
);

-- Mensagem de sucesso
SELECT 'Tabelas criadas com sucesso!' AS resultado;
```

### 3. Configurar a Aplicação
Abra o arquivo `src/main/resources/application.properties` e ajuste as credenciais do seu banco de dados, principalmente a senha.

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/gestao_projetos
spring.datasource.username=postgres
spring.datasource.password=sua_senha_aqui  # <-- ALTERE AQUI PARA A SUA SENHA!

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

---

## ▶️ Como Executar a Aplicação

Com o ambiente configurado, você pode iniciar a aplicação de duas maneiras:

### Método 1: Usando o Maven (via Terminal)
1.  Abra um terminal na pasta raiz do projeto (onde está o arquivo `pom.xml`).
2.  Execute o comando:
    ```bash
    mvn spring-boot:run
    ```
3.  Aguarde até que o terminal exiba os logs do Spring Boot, finalizando com uma mensagem como `Tomcat started on port(s): 8080`.

### Método 2: Usando sua IDE (VS Code / IntelliJ)
1.  Abra o projeto na sua IDE.
2.  Encontre a classe principal da aplicação, chamada `SistemaGestaoApplication.java`.
3.  Clique com o botão direito sobre o arquivo e selecione "Run" ou clique no ícone de "Play" (▶️) que aparece no editor.

---

## 🌐 Acessando a Aplicação

Após iniciar o servidor, abra seu navegador de internet e acesse as seguintes URLs:

* **Dashboard Principal:** [http://localhost:8080/](http://localhost:8080/)
* **Lista de Projetos:** [http://localhost:8080/projetos](http://localhost:8080/projetos)
* **Lista de Equipes:** [http://localhost:8080/equipes](http://localhost:8080/equipes)
* **Lista de Usuários:** [http://localhost:8080/usuarios](http://localhost:8080/usuarios)

A partir dessas páginas, você pode navegar para os formulários de cadastro.

# Projeto FullEducationSys 

## Descrição
Este projeto consiste em uma API Rest back-end desenvolvida em Java, utilizando o framework Spring Boot. A finalidade é oferecer uma solução para a gestão escolar, permitindo o cadastro e gerenciamento de usuários, docentes, turmas, alunos, cursos, matérias e notas.

## Problema Resolvido
O sistema visa facilitar a gestão de instituições educacionais, automatizando processos como cadastro de alunos, docentes, turmas e notas, além de fornecer acesso seguro e restrito às informações conforme o papel do usuário.

## Técnicas e Tecnologias Utilizadas
- Java
- Spring Boot
- Spring Security
- PostgreSQL
- DTOs para requisições e respostas
- GitFlow para versionamento do código
- Trello para organização de tarefas
- JWT para autenticação e autorização de usuários

## Como Executar
1. Clonar o repositório para a máquina local.
2. Configurar o banco de dados PostgreSQL.
3. Executar a aplicação Spring Boot.
4. Utilizar as rotas especificadas na documentação para interagir com a API.

## Melhorias Futuras
- Implementação de testes automatizados.
- Aprimoramento da segurança da API.
- Integração com outras plataformas educacionais.
- Melhorias na interface de usuário.

## Endpoints da Aplicação

### Endpoint de Login
- **POST /login**
    - Permite que um usuário faça login no sistema.

### Endpoint de Cadastro
- **POST /cadastro**
    - Permite que um novo usuário seja cadastrado no sistema.

### Endpoints para Entidade Docente
- **POST /docentes**
    - Cria um docente.
- **GET /docentes/{id}**
    - Obtém um docente por ID.
- **PUT /docentes/{id}**
    - Atualiza um docente por ID.
- **DELETE /docentes/{id}**
    - Exclui um docente por ID.
- **GET /docentes**
    - Lista todos os docentes.

### Endpoints para Entidade Turma
- **POST /turmas**
    - Cria uma turma.
- **GET /turmas/{id}**
    - Obtém uma turma por ID.
- **PUT /turmas/{id}**
    - Atualiza uma turma por ID.
- **DELETE /turmas/{id}**
    - Exclui uma turma por ID.
- **GET /turmas**
    - Lista todas as turmas.

### Endpoints para Entidade Aluno
- **POST /alunos**
    - Cria um aluno.
- **GET /alunos/{id}**
    - Obtém um aluno por ID.
- **PUT /alunos/{id}**
    - Atualiza um aluno por ID.
- **DELETE /alunos/{id}**
    - Exclui um aluno por ID.
- **GET /alunos**
    - Lista todos os alunos.

### Endpoints para Entidade Curso
- **POST /cursos**
    - Cria um curso.
- **GET /cursos/{id}**
    - Obtém um curso por ID.
- **PUT /cursos/{id}**
    - Atualiza um curso por ID.
- **DELETE /cursos/{id}**
    - Exclui um curso por ID.
- **GET /cursos**
    - Lista todos os cursos.

### Endpoints para Entidade Matéria
- **GET /cursos/{id_curso}/materias**
    - Lista as matérias de um curso.
- **POST /materias**
    - Cria uma matéria.
- **GET /materias/{id}**
    - Obtém uma matéria por ID.
- **PUT /materias/{id}**
    - Atualiza uma matéria por ID.
- **DELETE /materias/{id}**
    - Exclui uma matéria por ID.

### Endpoints para Entidade Notas
- **GET /alunos/{id_aluno}/notas**
    - Lista as notas de um aluno.
- **POST /notas**
    - Cria uma nota.
- **GET /notas/{id}**
    - Obtém uma nota por ID.
- **PUT /notas/{id}**
    - Atualiza uma nota por ID.
- **DELETE /notas/{id}**
    - Exclui uma nota por ID.

### Endpoint para Obter Pontuação Total do Aluno
- **GET /alunos/{id}/pontuacao**
    - Calcula e retorna a pontuação total de um aluno.

    
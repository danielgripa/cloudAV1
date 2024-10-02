# CloudAV1

Este é o repositório do projeto **CloudAV1**, que faz parte do desenvolvimento de um sistema de transações baseado em cloud. O projeto está disponível online para acesso através do link: [API-DOC](https://cloudav1-cyg2a3a7fpdfd4cw.canadacentral-01.azurewebsites.net/swagger-ui/index.html).

## Sobre o Projeto

O **CloudAV1** é uma aplicação desenvolvida para gerenciar transações financeiras de clientes, onde é possível realizar operações como cadastro de clientes, associar cartões e realizar transações entre contas. O objetivo principal do projeto é criar uma plataforma segura e eficiente para gerenciamento de transações, explorando tecnologias modernas para desenvolvimento de back-end e integração em nuvem.

## Tecnologias Utilizadas

- **Java (versão 21)**: Linguagem de programação principal utilizada para o desenvolvimento do back-end.
- **Spring Boot**: Framework para construção da aplicação, fornecendo um conjunto robusto de funcionalidades para a criação de APIs REST.
- **Maven 3.3.4**: Ferramenta de automação de build e gerenciamento de dependências.
- **Banco de Dados**: PostgreSQL ou MySQL (dependendo da configuração).
- **Cloud Hosting**: O projeto está hospedado em ambiente de nuvem.

## Funcionalidades

1. **Gestão de Clientes**: Cadastro, atualização, exclusão e busca de clientes.
2. **Gestão de Cartões**: Associação de cartões a clientes para realização de transações.
3. **Transações Seguras**: Permite a realização de transações financeiras entre diferentes contas.

## Configuração do Ambiente de Desenvolvimento

Para configurar o ambiente de desenvolvimento e executar a aplicação localmente, siga os seguintes passos:

1. **Clone o Repositório**:
   ```bash
   git clone https://github.com/danielgripa/cloudAV1.git
   ```

2. **Instale as Dependências**:
   Certifique-se de ter o Maven e o Java instalados. Na raiz do projeto, execute:
   ```bash
   mvn clean install
   ```

3. **Configure o Banco de Dados**:
   Atualize o arquivo de configuração para se conectar ao banco de dados desejado (PostgreSQL ou MySQL).

4. **Execute a Aplicação**:
   ```bash
   mvn spring-boot:run
   ```

## Como Contribuir

Se você deseja contribuir com o projeto, siga os passos abaixo:

1. Faça um fork do repositório.
2. Crie uma branch para a sua feature:
   ```bash
   git checkout -b minha-feature
   ```
3. Faça commit das suas alterações:
   ```bash
   git commit -m 'Adiciona nova funcionalidade'
   ```
4. Faça push para a sua branch:
   ```bash
   git push origin minha-feature
   ```
5. Abra um Pull Request.

## Desenvolvedor

Projeto desenvolvido por **Daniel Gripa**.


---


# CloudAV1

Este é o repositório do projeto **CloudAV1**, uma aplicação desenvolvida para gerenciar clientes e endereços. O projeto está disponível online para acesso através do link: [API-DOC](https://cloudav1-cyg2a3a7fpdfd4cw.canadacentral-01.azurewebsites.net/swagger-ui/index.html).

## Sobre o Projeto

O **CloudAV1** é uma aplicação construída para gerenciar informações de clientes e seus endereços, oferecendo operações CRUD (Create, Read, Update, Delete) para ambas as entidades. O objetivo principal do projeto é fornecer uma plataforma eficiente e segura para o gerenciamento de dados de clientes, explorando tecnologias modernas para desenvolvimento de back-end e hospedagem em nuvem.

## Tecnologias Utilizadas

- **Java (versão 21)**: Linguagem de programação principal utilizada para o desenvolvimento do back-end.
- **Spring Boot**: Framework utilizado para construção da aplicação, fornecendo um conjunto robusto de funcionalidades para a criação de APIs REST.
- **Maven 3.3.4**: Ferramenta de automação de build e gerenciamento de dependências.
- **Banco de Dados**: PostgreSQL ou MySQL (dependendo da configuração).
- **Cloud Hosting**: O projeto está hospedado em ambiente de nuvem.

## Funcionalidades

1. **Gestão de Clientes**: Cadastro, atualização, exclusão e busca de clientes.
2. **Gestão de Endereços**: Cadastro, atualização, exclusão e busca de endereços.
3. **Associação Cliente-Endereço**: Permite associar múltiplos endereços a um cliente, assim como desassociar endereços existentes.

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


# Sistema de Gestão de Clientes e Avaliação de Crédito com Microsserviços




## Descrição do Projeto

Este projeto é uma solução backend robusta e escalável, desenvolvida com uma arquitetura de microsserviços, projetada para gerenciar o cadastro de clientes e realizar a avaliação de crédito de forma eficiente e segura. A aplicação integra-se com o Keycloak para autenticação e autorização, garantindo que apenas requisições válidas e autorizadas acessem os recursos dos microsserviços. O sistema permite o cadastro de usuários com informações básicas (CPF, nome, idade), a gestão de cartões de crédito e um módulo de avaliação de crédito que sugere cartões e limites aprovados com base na renda do cliente. A comunicação entre os serviços é orquestrada por um Gateway API e um Eureka Server para descoberta de serviços, com mensageria assíncrona via RabbitMQ para emissão de cartões.




## Funcionalidades Principais

O sistema oferece as seguintes funcionalidades:

### Módulo de Clientes (`msclientes`)
- **Cadastro de Usuário:** Permite o registro de novos usuários com CPF, nome e idade. Um ID único é gerado automaticamente para cada usuário.
- **Consulta de Dados do Cliente:** Recupera os dados de um cliente específico.
- **Verificação de Status:** Verifica se o serviço de clientes está em execução.

### Módulo de Cartões (`mscartoes`)
- **Cadastro de Cartão:** Permite o registro de novos tipos de cartão, incluindo nome, bandeira (VISA ou MASTERCARD), renda mínima e limite básico.
- **Consulta de Cartões por Renda:** Retorna todos os cartões cadastrados com renda mínima até um valor especificado.
- **Consulta de Cartões por Cliente:** Lista os cartões emitidos para um cliente específico, utilizando o CPF como parâmetro. Esta funcionalidade requer que o cartão já esteja emitido para o cliente.

### Módulo de Avaliação de Crédito (`msavaliacaocredito`)
- **Avaliação de Cliente:** Recebe o CPF e a renda de um cliente e retorna os cartões disponíveis para ele, juntamente com o limite aprovado para cada cartão, com base na renda fornecida.
- **Solicitação de Emissão de Cartão:** Inicia o processo de emissão de um cartão para um cliente. Requer o ID do cartão, CPF do cliente, endereço e o limite liberado (obtido na avaliação de crédito). Após a solicitação, um protocolo aleatório é retornado e o cartão é emitido para o cliente.
- **Consulta de Situação do Cliente:** Retorna a situação completa de um cliente, incluindo seus dados cadastrais (ID, nome, idade) e todos os cartões que lhe foram emitidos, detalhando nome, bandeira e limite liberado para cada cartão.




## Tecnologias Utilizadas

Este projeto foi desenvolvido utilizando as seguintes tecnologias e ferramentas:

- **Spring Boot:** Versão 3.4.6 para o desenvolvimento dos microsserviços.
- **Java:** Versão 21 como linguagem de programação.
- **Spring Cloud Gateway:** Para roteamento e segurança das requisições.
- **Docker:** Para conteinerização das aplicações e seus ambientes.
- **Insomnia:** Ferramenta para testar as APIs (não faz parte do código, mas é útil para o desenvolvimento).
- **Eureka Server:** Para descoberta e registro de serviços.
- **Keycloak:** Para gerenciamento de identidade e acesso (autenticação e autorização).
- **RabbitMQ:** Para mensageria assíncrona, utilizada na emissão de cartões.
- **Maven:** Ferramenta de automação de build e gerenciamento de dependências.
- **Dockerfile:** Para a criação de imagens Docker dos microsserviços.




## Arquitetura

O projeto adota uma arquitetura de microsserviços, onde cada funcionalidade principal é encapsulada em um serviço independente. Essa abordagem permite maior escalabilidade, resiliência e facilidade de manutenção. A comunicação entre os serviços é gerenciada da seguinte forma:

- **Microsserviços Independentes:** `msclientes`, `mscartoes` e `msavaliacaocredito` operam como unidades autônomas, cada um com sua própria responsabilidade.
- **Eureka Server:** Atua como um servidor de descoberta de serviços, permitindo que os microsserviços se registrem e se descubram mutuamente, facilitando a comunicação dinâmica.
- **Spring Cloud Gateway:** Funciona como o ponto de entrada único para todas as requisições externas. Ele é responsável pelo roteamento das requisições para os microsserviços apropriados, além de aplicar políticas de segurança (integração com Keycloak) e balanceamento de carga.
- **Keycloak:** Gerencia a autenticação e autorização. Antes de qualquer requisição chegar aos microsserviços, o Gateway valida o token JWT emitido pelo Keycloak, garantindo que apenas usuários autenticados e autorizados acessem os recursos.
- **RabbitMQ:** Utilizado para comunicação assíncrona entre os microsserviços, especialmente no fluxo de emissão de cartões. Isso garante que operações demoradas ou que não exigem resposta imediata sejam processadas de forma eficiente e desacoplada.

Esta arquitetura garante que o sistema seja robusto, seguro e capaz de lidar com um grande volume de requisições, ao mesmo tempo em que oferece flexibilidade para futuras expansões e modificações.




## Como Rodar o Projeto

Para executar este projeto em seu ambiente local, siga os passos abaixo:

### Pré-requisitos
- Java 21 ou superior
- Maven
- Docker e Docker Compose
- Keycloak (configurado e em execução)
- RabbitMQ (em execução)

### Configuração do Keycloak
1. Certifique-se de ter uma instância do Keycloak em execução. Você pode usar o Docker para isso:
   ```bash
   docker run -p 8080:8080 -e KEYCLOAK_ADMIN=admin -e KEYCLOAK_ADMIN_PASSWORD=admin quay.io/keycloak/keycloak:latest start-dev
   ```
2. Acesse o painel administrativo do Keycloak (geralmente em `http://localhost:8080`).
3. Crie um novo `Realm` (por exemplo, `microsservices-realm`).
4. Dentro do `Realm`, crie um `Client` (por exemplo, `microsservices-client`) com as configurações apropriadas para um aplicativo `public` ou `confidential` (dependendo da sua necessidade de segurança).
5. Crie usuários e atribua-lhes as roles necessárias para testar a aplicação.

### Configuração do RabbitMQ
1. Certifique-se de ter uma instância do RabbitMQ em execução. Você pode usar o Docker para isso:
   ```bash
   docker run -d --hostname my-rabbit --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:3-management
   ```

### Executando os Microsserviços

#### 1. Clonar o Repositório
```bash
git clone <URL_DO_SEU_REPOSITORIO>
cd <NOME_DO_DIRETORIO_DO_PROJETO>
```

#### 2. Configurar os Arquivos `application.yml`
- Para cada microsserviço (`msclientes`, `mscartoes`, `msavaliacaocredito`, `eurekaserver`, `mscloudgateway`), verifique e ajuste os arquivos `src/main/resources/application.yml` e `application-production.yml` conforme necessário, especialmente as configurações de conexão com o Keycloak, Eureka Server e RabbitMQ.

#### 3. Construir os Projetos
Navegue até o diretório raiz de cada microsserviço e execute o Maven para construir o projeto:
```bash
cd msclientes
mvn clean install
cd ../mscartoes
mvn clean install
cd ../msavaliacaocredito
mvn clean install
cd ../eurekaserver
mvn clean install
cd ../mscloudgateway
mvn clean install
```

#### 4. Iniciar os Serviços
É recomendado iniciar os serviços na seguinte ordem:

1. **Eureka Server:**
   ```bash
   cd eurekaserver
   java -jar target/eurekaserver-0.0.1-SNAPSHOT.jar
   ```
   Verifique se o Eureka Server está em execução em `http://localhost:8761`.

2. **Microsserviços (`msclientes`, `mscartoes`, `msavaliacaocredito`):**
   Abra um novo terminal para cada microsserviço e inicie-os:
   ```bash
   cd msclientes
   java -jar target/msclientes-0.0.1-SNAPSHOT.jar
   ```
   ```bash
   cd mscartoes
   java -jar target/mscartoes-0.0.1-SNAPSHOT.jar
   ```
   ```bash
   cd msavaliacaocredito
   java -jar target/msavaliacaocredito-0.0.1-SNAPSHOT.jar
   ```

3. **Spring Cloud Gateway:**
   ```bash
   cd mscloudgateway
   java -jar target/mscloudgateway-0.0.1-SNAPSHOT.jar
   ```

### Executando com Docker Compose (Recomendado para Ambiente de Desenvolvimento)

Para simplificar a execução de todos os serviços, você pode usar o Docker Compose. Certifique-se de ter um arquivo `docker-compose.yml` configurado na raiz do seu projeto que inclua todos os serviços (Keycloak, RabbitMQ, Eureka Server, e seus microsserviços).

1. **Construir as Imagens Docker:**
   Navegue até o diretório raiz de cada microsserviço e construa a imagem Docker (se ainda não tiver feito):
   ```bash
   cd msclientes
   docker build -t msclientes .
   ```
   Repita para `mscartoes`, `msavaliacaocredito`, `eurekaserver` e `mscloudgateway`.

2. **Iniciar os Serviços com Docker Compose:**
   Na raiz do seu projeto (onde o `docker-compose.yml` está localizado):
   ```bash
   docker-compose up --build
   ```
   Isso irá construir (se necessário) e iniciar todos os contêineres definidos no seu `docker-compose.yml`.

### Testando as APIs

Você pode usar ferramentas como Insomnia ou Postman para testar as APIs. Lembre-se de obter um token de acesso do Keycloak antes de fazer requisições aos endpoints protegidos pelo Gateway. Caso contrário vai dar erro 401 unauthorized.

**Exemplo de Obtenção de Token (Keycloak):**

`GET (http://localhost:8081/realms/mscourserealm/protocol/openid-connect/token)`
-> esse foi o que utilizei na minha aplicação, mas deve-se configurar o keycloak na sua máquina para isso.

**Body (x-www-form-urlencoded):**
- `client_id`: `<seu_client_id>`
- `username`: `<seu_usuario>`
- `password`: `<sua_senha>`
- `grant_type`: `password`

Com o token em mãos, inclua-o no cabeçalho `Authorization` como `Bearer <token>` em suas requisições para o Gateway.




## Contribuições

Contribuições são bem-vindas! Se você deseja contribuir com este projeto, por favor, siga os passos abaixo:

1. Faça um fork do repositório.
2. Crie uma nova branch para sua feature (`git checkout -b feature/MinhaNovaFeature-seunome`).
3. Faça suas alterações e commit-as (`git commit -m 'Adiciona nova feature'`).
4. Envie suas alterações para o repositório remoto (`git push origin feature/MinhaNovaFeature`).
5. Abra um Pull Request detalhando suas alterações.



## Autor

Desenvolvido por Kauã Reis Rodrigues, em busca de desenvolvimento, conhecimento e aprendizado na prática!



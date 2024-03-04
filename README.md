# Big Chat Brasil

## Introdução
Bem-vindo ao repositório da aplicação Big Chat Brasil! Esta aplicação é uma plataforma de chat robusta, oferecendo uma experiência de comunicação interativa.

## Pré-requisitos
Antes de começar, certifique-se de que você tem o Maven e o Docker instalados em sua máquina. Essas ferramentas são essenciais para a configuração e execução do Big Chat Brasil.

## Configuração

### Configuração de Ambiente
Antes de iniciar a construção do pacote, você precisa configurar o ambiente:

#### Arquivo de Variáveis de Ambiente:
Renomeie o arquivo **dev-env.env** para **.env**. Este arquivo contém as variáveis de ambiente necessárias para a execução da aplicação.

#### Configuração do Twilio (Opcional):
Caso deseje utilizar a API do Twilio para funcionalidades de mensagens, insira suas credenciais do Twilio no arquivo **.env**. Certifique-se de adicionar as chaves para o SID, Token.

### Construindo o Pacote
Para começar a usar o Big Chat Brasil, siga os seguintes passos para construir o pacote da aplicação:

### Limpeza e Instalação com Maven:
No terminal, navegue até o diretório raiz da aplicação e execute os seguintes comandos para limpar e instalar as dependências do Maven, bem como construir o pacote:


~~~
mvn clean
mvn install
~~~
### Verificação do Pacote:
Após a construção, verifique se o arquivo .jar gerado na pasta target tem o nome bigchatbrasil-0.0.1.jar. Caso o nome seja diferente, você precisará atualizar o Dockerfile:

Altere o nome no comando COPY para corresponder ao nome do arquivo .jar gerado.
Altere também o nome no ENTRYPOINT para refletir o novo nome do arquivo.
Configurando o Docker
Baixando a Imagem do PostgreSQL:
Execute o seguinte comando para baixar a imagem do PostgreSQL, caso você ainda não a tenha:

~~~
docker pull postgres
~~~

### Executando com Docker Compose:
Ainda no diretório raiz do projeto bigchatbrasil, execute o Docker Compose para iniciar a aplicação:

~~~
docker-compose up
~~~
ou 
~~~
docker compose up
~~~

Sua aplicação deve iniciar sem problemas.

### Documentação Adicional
Para testes das funcionalidades, consulte a documentação disponível no Postman.


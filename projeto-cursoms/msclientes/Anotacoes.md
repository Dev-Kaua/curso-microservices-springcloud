## Sobre o pom.xml:
-> adicionou-se o spring-cloud-starter-netflix-eureka-client no msclientes;
-> não é necessário inserir a versão nele, pois criamos o dependencyManagement,
    que está configurado para inserir certa versão em todos os arquivos spring-cloud;

## Sobre o application.yml:
### server:
-> inserimos a porta (port) como 0 porque?
    --> quando colocamos a porta 0, a aplicação vai subir em uma porta aleatória (randomic)
        assim, gerando sempre portas que ainda não estão ocupadas no eureka server para
        aplicar os principios de microsserviços sobre gerar diversas instâncias conforme
        a necessidade/demanda.
### eureka:
-> na propriedade eureka/client/service-url/defaultZone, estamos dizendo pro sistema básicamente
    onde que está nosso servidor eureka. Para ele saber em qual servidor eureka eu quero que
    ele se registre.
-> no instance/instance-id, dizemos qual é o id dessa instância lá dentro do eureka server.
    --> toda vez que registrarmos um microsserviço no eureka, ele vai estar registrado com um
        id, mas não podemos registrar duas instâncias com o mesmo nome/id.
    --> Vamos criar uma estratégia para que a própria aplicação spring boot gere um id aleatório.
    --> Mas não podemos perder a referência que esse nome/id é do microsserviço de clientes,
        então no mínimo ele tem que ter o nome da aplicação (application/name) + uma porta random.
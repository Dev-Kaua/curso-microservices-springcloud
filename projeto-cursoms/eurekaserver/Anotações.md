### Pra que serve o application.yml?
lá dentro do eureka server, ou na comunicação entre os microsserviços, vamos fazer essa comunicação
através de nomes. Nome esse que é definido no application.yml(originalmente application.properties mas alterado
para .yml para melhor organização do projeto) --> pode dar o nome que quiser
*OBS:* O eureka server é um conglomerado de microsserviços que fazem parte de um mesmo projeto.
### O application.yml contém:
-> O nome que vamos nos referir na comunicação entre microsserviços;
-> a porta de acesso;
-> Outras configurações, como por exemplo a que fizemos lá uma configuração para 
    o eureka não ficar autoregistrado nele mesmo.
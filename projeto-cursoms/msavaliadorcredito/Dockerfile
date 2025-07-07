FROM maven:3.9.9-eclipse-temurin-21 as build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests


FROM openjdk:21
WORKDIR /app
COPY --from=build ./app/target/*.jar ./app.jar

ARG RABBITMQ_SERVER=rabbitmq-host
ARG EUREKA_SERVER=localhost

ENTRYPOINT java -jar -Dspring.profiles.active=production app.jar
#deve-se criar o network para que os microsserviços consigam se conectar via docker
# -> o network é criado via comando -> docker network create nome-do-network
#Além disso, deve-se inserir o nome do container dos serviços ao invés de localhost
#Exemplo: localhost:8761 -> cursoms-eureka:8761
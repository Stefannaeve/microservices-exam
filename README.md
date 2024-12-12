# Bookhub

## Setup guide

```shell
mvn clean install
```

```
Start application 
```

```
Run the populateDatabase in the scratchFiles folder, this is to populate the database with books
```

## Using the application

```
Use the bookScratchFile in the scratchFile to test the application
```



net stop mysql80
mvn spring-boot:run "-Dspring-boot.run.profiles=docker"

docker rmi stefannaeve/microservices:latest-book stefannaeve/microservices:latest-comment stefannaeve/microservices:latest-user stefannaeve/microservices:latest-manager stefannaeve/microservices:latest-gateway
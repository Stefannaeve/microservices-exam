# Bookhub

## Setup guide

```shell
    mvn clean install
```

```
Configure Mysql in the project
```

```shell
echo $null >> src/main/resources/application.properties
```

#### Add this text to the application.properties file
```
spring.application.name=microservices-exam
spring.jpa.hibernate.ddl-auto=update
spring.datasource.url=jdbc:mysql://localhost:3306/bookHub
spring.datasource.username=
spring.datasource.password=
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.show-sql=true
```

```
Configure the application.properties, and add password and username for your database
```

```
Go into the sqlInitializer in the resource directory, and run the whole file
```

```
Start application 
```

```
Agree to enable lombok on popup
```

```
Run the populateDatabase in the scratchFiles folder, this is to populate the database with books
```

## Using the application

```
   Use the bookScratchFile in the scratchFile to test the application
```
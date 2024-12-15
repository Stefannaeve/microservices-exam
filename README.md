```
            .-'''-.        .-'''-.                                                    
           '   _    \     '   _    \                                                  
/|       /   /` '.   \  /   /` '.   \     .           .                    /|         
||      .   |     \  ' .   |     \  '   .'|         .'|                    ||         
||      |   '      |  '|   '      |  '.'  |        <  |                    ||         
||  __  \    \     / / \    \     / /<    |         | |                    ||  __     
||/'__ '.`.   ` ..' /   `.   ` ..' /  |   | ____    | | .'''-.     _    _  ||/'__ '.  
|:/`  '. '  '-...-'`       '-...-'`   |   | \ .'    | |/.'''. \   | '  / | |:/`  '. ' 
||     | |                            |   |/  .     |  /    | |  .' | .' | ||     | | 
||\    / '                            |    /\  \    | |     | |  /  | /  | ||\    / ' 
|/\'..' /                             |   |  \  \   | |     | | |   `'.  | |/\'..' /  
'  `'-'`                              '    \  \  \  | '.    | '.'   .'|  '/'  `'-'`   
                                     '------'  '---''---'   '---'`-'  `--'  
```

# Welcome to Bookhub

### Introduction

This application creates a endpoint for serving and storing books for users. Both public domain and private books. 
Public domain books will be added through a external api for Gutenberg. You are able to add comments to these books, and
add which page this comment belong to

### Features

- Search through books by genre (Never finished)
- Positive, negative, and neutral comments
- Book status
  - What status you are in reading
  - What page you are on

### Building project
We have put most of our efforts into making the project work from docker, and not built our project with spring-boot:run
since the very beginning of the semester.

Consul, Mysql and rabbitMQ has exclusively been run on docker for us, therefore we have not made any effort into 
migrate the code away from docker.

When using compose up, you are expected to have a .env file, which will run the application. This will run the docker-images
from stefannaeve/microservices on dockerHub, which will be our latest rendition of our exam, or you may make up your own
name in the .env file, to make the project run and build locally.

.env file (inside docker folder)
```
DOCKERHUB_USERNAME=stefannaeve
IMAGE_TAG=latest
DATABASE_PASSWORD=
```
- Add your own database password
- Keep "stefannaeve" if you want to pull the images from the groups dockerhub library
- Change "stefannaeve" to something else if you wish to build local files

##### Building with docker compose from root folder
```shell
mvn clean install # For local runs
```
```shell
cd docker
docker compose --project-name book-hub up --scale book=1 --scale user=1 --scale comment=1 -d
```
Docker compose up if you want the top amount of each service which is 3
```shell
cd docker
docker compose --project-name book-hub up
```

##### Closing project with docker compose, and remove local docker images from this project
```shell
docker compose --project-name book-hub down --rmi local
```
This if you want to remove volumes at the same time
```shell
docker compose --project-name book-hub down --rmi local --volumes
```
Remove volumes after down
```shell
docker volume rm book-hub_book_db book-hub_comment_db book-hub_user_db
```

### Pull request to main branch
If you fork this repo, you may add your own github secrets, and make a pull request into main, to make the workflow file 
run.

This workflow file will 
- Build the project
- Log into the specified dockerhub account
- Push images to the docker account specified
  - Same image is pushed twice, one with a portion of the commit hash tag, and one with latest tag

GITHUB SECRETS

| Secret    |
|-----------|
| DATABASE_PASSWORD    |
| DOCKERHUB_TOKEN |
| DOCKERHUB_USERNAME |


### Modules / service endpoints
- Gateway: ```http://localhost:8000```
- Book Service: ```http://localhost:8082```
- Comment Service: ```http://localhost:8081```
- User Service: ```http://localhost:8083```
- Manager Service: ```http://localhost:8080```
- Consul UI: ```http://localhost:8500```
- RabbitMQ UI: ```http://localhost:15672```



net stop mysql80
mvn spring-boot:run "-Dspring-boot.run.profiles=docker"

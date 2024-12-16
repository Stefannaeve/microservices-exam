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

## Introduction

Our main objective in this project is to create a server that allows users to host their own books, as well as fetch public domain books from gutenberg.
The application will also provide a rating system where the user can rate books.
The user can add their own comments to books and will be able specify if the comment is positive, negative or neutral.
A book reading status will be included in this application. This means that the user can manually add their reading status.
We are making the application for a specific audience. People looking for an open source
solution for handling books. These people will be more centered around the idea of an
application with functionality rather than flashy user interfaces
We emphasize the addition of books to the database, and comments on specific books as
the core components of the project, and will work further to optimize these points before
adding additional features to the project

## User stories

1. As a user,
   I want to add my books to the system,
   So that I can have my entire collection in one remotely accessible place.
   Functionality allows users to add new books to the system, including metadata such, but not limited to as title, author, and publication year.

2. As a user,
   I want to be able to add comments to my books,
   So that I can keep track of the story, characters, and setting.
   A feature enabling users to add comments positive/negative/neutral to books.

3. As a user,
   I want to add already existing public domain books to my library,
   So that I can have all the books I am interacting with in my unified personal library.

4. As a user,
   I want to resuscitate a book,
   So that I can finish a book that I never got to finish
   A feature allowing users to browse through their reading status, and in this case find
   books that were never finished. Giving the user the ability to change reading status
   (Not yet started, In progress, Finished, Did not finish, To be read)

5. As a user,
   I want to rate a book,
   So that I can keep track of which ones I liked or did not like based on a rating from 1
   to 10

## Candidates

| Candidates | Individual responsibilities                                              |
|------------|--------------------------------------------------------------------------|
| 2          | Consul, User, External API                                               |
| 29         | RabbitMQ, Comment                                                        |
| 4          | Docker, Gateway, Manager, Book, Github & github configuration & workflow |

## Prerequisite
- **Java 21**
- **Maven v3.3.4**
- **Docker compose v2.30.3**

## Building project
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

## How to test project
We have made a postman collection, and exported it as a JSON file. The JSON file is inside the document folder in root. 
If you want to test the project through the JSON file we made, you can import it in postman.

![img.png](documents/img.png)

## Pull request to main branch
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


## Modules / service endpoints
- Gateway: ```http://localhost:8000```
- Book Service: ```http://localhost:8082```
- Comment Service: ```http://localhost:8081```
- User Service: ```http://localhost:8083```
- Manager Service: ```http://localhost:8080```
- Consul UI: ```http://localhost:8500```
- RabbitMQ UI: ```http://localhost:15672```

## Tools learned in PGR3402
### A bit about consul:
We are using Consul for health checks and discovery of the different services. We currently also have central control of configuration set up, but are currently running using only one profile.
Since the project runs exclusively on docker, we haven't had a need for common use cases like being able to swap to different databases for production and development/testing, but the templates are there to allow us to do so when it would be needed.

Consul pulls KV pairs from the file “consul_kv_docker.json” as the project starts from docker compose, through the post_start command in the compose file. The services that use this configuration depend on a health check returning healthy, which happens when “consul kv get -keys” returns something other than null.

The central configuration is stored using config as a prefix, then service and profile separated by a comma.

### A bit about external api use:
We are getting our public domain books from gutenberg.org. This website has a robust api for fetching both their catalog, to give us a list of available books, as well as their contents.

We have an api endpoint that allows you to fetch the gutenberg book catalog, and populate the database with book metadata. The endpoint takes in a number, so you can choose how much of the catalog you want to process, since the collection is so large.

When the catalog has been processed users can start fetching the book contents.
When a user makes a request for a book we check if we have the book contents stored. If we have it then it's sent, if not we fetch it from gutenberg, store it on our server, then send it.
This allows us to slowly populate the database based on what the users actually want.

### Docker
We are using docker to centralize the hosting of all our code. This makes the development environment uniform across all the software team, simplifying the workflow of the development team.

#### Comprehensive service hosting:
All services are hosted through docker, this ensures consistency and scalability. This includes, Consul, RabbitMQ, Gateway, various databases, and local services.

#### Dynamic configuration
**Github Secrets**
We Use github secrets to securely manage the environment variables during the workflow runs
**.env**
We use a .env file for easy configuration for local runs

#### Automated image delivery
We have a workflow file for automated delivery of local docker images to Docker Hub, streamlining deployment.

#### Service dependency management
The "service_depends" ensures that all the services start in the right order.

#### Reliable network between services
A docker network makes sure consistent and dependable communication between all services.

#### Port allocation for local services
Local services, except gateway and manager, are assigned a range of ports (our case 3), to supports multiple instances running concurrently

#### Persistent data storage
We use docker volumes for database services to retain data even when the containers are restarted

net stop mysql80
mvn spring-boot:run "-Dspring-boot.run.profiles=docker"



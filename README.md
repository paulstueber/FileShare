File Upload Demo Application
======

Demonstration application for a multi service architecture.

# Architecture
The architecture used for this project has the purpose do give an idea how to build scalable 
and easy to maintain applications. However, given only the task of uploading files using a 
web frontend, the recommended architecture would of course be an monolithic one (see 
[Microservice MicroservicePrerequisites](https://martinfowler.com/bliki/MicroservicePrerequisites.html)).

![architecture](docs/architecture.png?raw=true "System Architecture"")

## Services
There are three services implemented. A single backend service providing an REST API (**media**),
a frontend service (**ui**) providing the web based frontend and an API-Gateway and a discovery 
service (**discovery**) based on eureka.

### MEDIA service
The media service does nothing but accepting and providing media files via a REST API. It stores 
the binaries on the hard drive and metadata in a database (MongoDB).

### UI service
The UI service provides the **web frontend**. Additionally it handles all authentication and 
user requests. All user data is stored in a database (MongoDB).

The UI server also provides the API-Gateway (using zuul) to avoid CORS problems and simplify 
client-server communication. Endpoints defined in the gateway are discovered using an eureka client.

### DISCOVERY service
This is basically a wrapper to only start and run an eureka server using spring boot.

# run the application
The easiest should be to run the entire application using docker-compose. 

## build & run
you can either build every project separately using ```mvn clean install -B``` in every service 
directory of run ```./build.sh``` in the root folder.

### docker-compose
Once every service is build, simply run: 

```docker-compose up```

It will build all missing images and run containers using the properties defined in the file ```docker-compose.yml```

### manually
To manually run a service use:

```mvn spring-boot run```

or if it is already build:

```java -jar SERVICE/target/servive.jar```

## configure
All neccessary configurations are defined in each **application.yml** in the projects resource directory. 
However, when using **docker-compose** you can overwrite any property using the **environment** 
property. Properties defined in the **application.yml** can be overwritten using the following schema:

```
#application.yml

config:
  init:
    user:
      name: 'admin@localhost'
```

```
# docer-compose.yml
services:
    service:
        [...]
        environment:
            - CONFIG_INIT_USER_NAME=admin@localhost
```

## Usage
Once the application is started, visit (replace localhost with correct hostname):

**UI**
```http://localhost:8080```

**EUREKA**
```http://localhost:8761```

**MEDIA**
```http://localhost:8081```

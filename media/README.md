Media Service
======

Very simple media service that allows to upload an retrieve media files

# Setup dev env
Requirements:
* [java8](http://www.oracle.com/technetwork/java/javase/downloads/index.html)
* [maven](https://maven.apache.org/)
* [mongoDB](https://www.mongodb.com/) ([mongoDb docker](https://hub.docker.com/_/mongo/))

## mongo
```
docker run --name mongo -p 27017:27017 -v /var/lib/mongodb/:/data/db -d mongo
```

## configuration
There are three possible ways to configure the service. 
1. **Environment variables (recommended):**
   
   ```
   export UPLOAD_FOLDER_FILES=/tmp/uploads
   ```

3. **Properties file (not recommended)**
   It is of also possible to directly set all parameters in the application.yml.

## run, build & test
When everything is set up, simply run one of the following commands:

* **run dev**
   ```
   mvn spring-boot:run
   ```

* **build and test**

   ```
   $mvn clean install
   ```

* **build fat jar (all required modules bundled)**

   ```
   $ mvn clean package spring-boot:repackage
   ```

* **test coverage**
   After the generation the report is located under target/site/jacoco

   ```
   mvn clean install -P coverage
   ```
   ```

* **checkstyles, findbugs & unit tests**

    ```
    mvn clean test
    mvn checkstyle:check
    mvn findbugs:check
    mvn compile findbugs:check
    ```

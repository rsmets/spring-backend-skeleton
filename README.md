Skeleton for backend spring projects. My hope is I can reuse this as base spring-ing off point for more pointed projects.

MAVEN WAY TO DOCKER ACTIONS:

    1) mvn dockerfile:build
    2) mvn dockerfile:push

   OR simply

    1) mvn install
        - which triggers both 'mvn dockerfile:build' && 'mvn dockerfile:push'
          due to the build execution definitions the pom file.
        - however currently is having issues during the push phase... https://github.com/spotify/dockerfile-maven/issues/51
        - note need to populate the required env vars for tests to pass
            i.e. mvn -DDB_HOST=localhost -DDB_PORT=27017 -DDB_NAME=nexkey install 
            or mvn install -DskipTests=true

TO RUN THE CONTAINER:
    - need to populate the env variables & open ports
    
    docker run -e DB_HOST='host.docker.internal' -e DB_PORT='27017' -e DB_NAME='nexkey' -e DB_USER='' -e DB_PW='' -e KEY_PW='' -p 8433:8433 -p 27017:27017 rsmets/spring-backend-skeleton:0.0.1-SNAPSHOT
    
    docker run -e DB_HOST='ds241369-a0.mlab.com' -e DB_PORT='41369' -e DB_NAME='nexkey-sandbox' -e DB_USER='user' -e DB_PW='pw' -p 8433:8433 -p 41369:41369 rsmets/spring-backend-skeleton:0.0.2

OLD NONE MAVEN WAY TO DO DOCKER ACTIONS:

How to build the docker image:

    docker build -t skeleton .

How to run the docker container on port 5000

    docker run -p 5000:8080 skeleton

How to tag the image

    docker tag skeleton rsmets/skeleton:0.0.1-SNAPSHOT

How to push the image to docker hub

    docker login
    docker push rsmets/skeleton:0.0.1-SNAPSHOT
    
    OR if used the maven install way of building and tagging then:
    docker push rsmets/spring-backend-skeleton:0.0.1-SNAPSHOT

How to pull the image from docker hub and run

    docker run -p 5000:8080 rsmets/skeleton:0.0.1-SNAPSHOT


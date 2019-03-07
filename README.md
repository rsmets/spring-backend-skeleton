Skeleton for backend spring projects. My hope is I can reuse this as base spring-ing off point for more pointed projects.

MAVEN WAY TO DOCKER ACTIONS:

    1) mvn dockerfile:build
    2) mvn dockerfile:push

   OR simply

    1) mvn install
        - which triggers both 'mvn dockerfile:build' && 'mvn dockerfile:push'
          due to the build execution definitions the pom file.
        - however currently is having issues during the push phase... https://github.com/spotify/dockerfile-maven/issues/51


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

How to pull the image from docker hub and run

    docker run -p 5000:8080 rsmets/skeleton:0.0.1-SNAPSHOT


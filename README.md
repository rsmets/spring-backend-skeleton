Skeleton for backend spring projects. My hope is I can reuse this as base spring-ing off point for more pointed projects.

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
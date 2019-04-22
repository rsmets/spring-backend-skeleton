FROM openjdk:11-jdk

MAINTAINER ray.smets@nexkey.com

## Set the working directory to /app
#WORKDIR /app
#
## Copy the current directory conents to the container at /app
#ADD . /app
#
#ARG ENV_VARS="-DDB_HOST=localhost -DDB_PORT=27017 -DDB_NAME=nexkey"
#
## Install any needed package libs
#RUN mvn clean install ${ENV_VARS}
#
## Build and create jar using maven
##RUN mvn package
#
## Expose port to the world outside the container
#EXPOSE 8080
#
## Define evn vars
#ENV JAVA_OPTS=${ENV_VARS}
#
## Run jar when container launchees
#CMD ["java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar spring-backend-skeleton-0.0.1-SNAPSHOT.jar",
#"spring-backend-skeleton-0.0.1-SNAPSHOT.jar"]

VOLUME /tmp
ARG JAR_FILE=target/spring-backend-skeleton-0.0.1-SNAPSHOT.jar
#COPY ${JAR_FILE} app.jar
ADD ${JAR_FILE} app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]

#ARG DEPENDENCY=target/spring-backend-skeleton-0.0.1-SNAPSHOT.jar
#COPY ${DEPENDENCY}/BOOT-INF/lib /app/lib
#COPY ${DEPENDENCY}/META-INF /app/META-INF
#COPY ${DEPENDENCY}/BOOT-INF/classes /app
#ENTRYPOINT ["java","-cp","app:app/lib/*","com.skeleton.project.SpringBackendSkeletonApplication"]


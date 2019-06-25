FROM openjdk:11-jdk

MAINTAINER ray.smets@nexkey.com

# Populating the java run env via the docker env variables
ENV DB_HOST=**DB_HOST**
ENV DB_PORT=**DB_PORT**
ENV DB_NAME=**DB_NAME**
ENV DB_USER=**DB_USER**
ENV DB_PW=**DB_PW**
ENV DB_NEW=**DB_NEW**
ARG ENV_VARS="-DDB_HOST=${DB_HOST} -DDB_PORT=${DB_PORT} -DDB_NAME=${DB_NAME} -DDB_USER=${DB_USER} -DDB_PW=${DB_PW} -DDB_NEW=${DB_NEW}"

## Expose port to the world outside the container
EXPOSE 8080
EXPOSE 27017
EXPOSE 8433

# Define evn vars
ENV JAVA_OPTS=${ENV_VARS}

VOLUME /tmp
ARG JAR_FILE=target/spring-backend-skeleton-0.0.1-SNAPSHOT.jar
#COPY ${JAR_FILE} app.jar
ADD ${JAR_FILE} app.jar

ARG CERT_FILE='*.nexkey.com.p12'
ADD ${CERT_FILE} '*.nexkey.com.p12'

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /app.jar"]


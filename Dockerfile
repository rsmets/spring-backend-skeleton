FROM openjdk:11-jdk
VOLUME /tmp
EXPOSE 8080
ARG JAR_FILE=target/spring-backend-skeleton-0.0.1-SNAPSHOT.jar
#COPY ${JAR_FILE} app.jar
ADD ${JAR_FILE} skeleton.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/skeleton.jar"]
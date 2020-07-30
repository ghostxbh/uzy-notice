FROM openjdk:8-alpine

# Required for starting application up.
RUN apk update && apk add /bin/sh

RUN mkdir -p /opt/app
ENV PROJECT_HOME /opt/app

COPY target/spring-boot-mongo-1.0.jar $PROJECT_HOME/spring-boot-mongo.jar

WORKDIR $PROJECT_HOME

CMD ["java", "-Dspring.data.mongodb.uri=mongodb://uzykj:1qaz@WSX#EDC@127.0.0.1:27017/notice","-Djava.security.egd=file:/dev/./urandom","-jar","./spring-boot-mongo.jar"]
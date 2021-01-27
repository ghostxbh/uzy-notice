FROM openjdk:8-alpine

# Required for starting application up.
RUN apk update && apk add /bin/sh

RUN mkdir -p /opt/app
ENV PROJECT_HOME /opt/app

COPY target/uzy-notice.jar $PROJECT_HOME/uzy-notice.jar

WORKDIR $PROJECT_HOME

CMD ["java", "-Dspring.data.mongodb.uri=mongodb://root:zxcASDqwe@34.92.138.182:27017","-jar","./uzy-notice.jar"]
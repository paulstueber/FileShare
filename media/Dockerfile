FROM openjdk:8u131-jre-alpine

EXPOSE 8081

ADD ./target/media.jar /srv/

WORKDIR /srv

ENTRYPOINT exec java -jar media.jar
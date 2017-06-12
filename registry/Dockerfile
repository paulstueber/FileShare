FROM openjdk:8u131-jre-alpine

EXPOSE 8761

ADD ./target/registry.jar /srv/

WORKDIR /srv

ENTRYPOINT exec java -jar registry.jar
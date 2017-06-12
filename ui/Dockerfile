FROM openjdk:8u131-jre-alpine

EXPOSE 8080

ADD ./target/ui.jar /srv/

WORKDIR /srv

ENTRYPOINT exec java -jar ui.jar
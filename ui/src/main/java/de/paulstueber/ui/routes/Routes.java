package de.paulstueber.ui.routes;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;

/**
 * Camel route to handle all incomming messages to from the import queue (subscriber to the import topic)
 */
@Component
public class Routes extends GenericRouteBuilder {

    /**
     * URI of direct camel route for sending DTOs
     */
    public static final String ROUTE_DIRECT_SEND_DTO = "direct:sendDTO";

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${spring.data.mongodb.collection.idempotent}")
    private String idempotentCollection;

    @Value("${spring.data.mongodb.database}")
    private String database;

    /**
     * configure & register all jukin rss feed routes with polling consumers
     */
    protected void configureRoutes() {
        this.initSendMessageRoute();
    }

    /**
     * convert an object to a hash map and send the serialized source
     */
    private void initSendMessageRoute() {
        // this route is also called from outside this route definitions
        from(ROUTE_DIRECT_SEND_DTO)
                .marshal(new JacksonDataFormat(objectMapper, HashMap.class))
                .toD("kafka:{{config.kafka.topic.out}}?brokers={{config.kafka.host}}:{{config.kafka.port}}");

        from("kafka:{{config.kafka.topic.out}}?brokers={{config.kafka.host}}:{{config.kafka.port}}"
                + "&maxPollRecords={{config.kafka.consumer.maxPollRecords}}"
                + "&consumersCount={{config.kafka.consumer.consumersCount}}"
                + "&seekTo={{config.kafka.consumer.seekTo}}"
                + "&groupId={{spring.application.name}}")
                .routeId("FromKafka")
                .log("${body}");

    }
}

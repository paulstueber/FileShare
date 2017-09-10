package de.paulstueber.ui.routes;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.MongoSocketException;
import com.mongodb.MongoTimeoutException;
import de.paulstueber.ui.model.message.ErrorMessage;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.Message;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;
import java.util.HashMap;

/**
 * Generic Camel Routes for whole application with exception handler implementations for all routes
 */
public abstract class GenericRouteBuilder extends RouteBuilder {

    /**
     * camel route name to handle exception and publish an error message error topic/queue
     */
    public static final String ROUTE_DIRECT_PUBLISH_EXCEPTION = "direct:publishException";

    private static final Logger LOGGER = LoggerFactory.getLogger(GenericRouteBuilder.class);

    @Value("${spring.application.name}")
    private String serviceName;

    @Autowired
    private CamelContext camelContext;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Add routes to camel context. Otherwise won't be able to directly reference routes from Spring.
     * @throws Exception if adding route fails
     */
    @PostConstruct
    public void registerRoute() throws Exception {
        camelContext.addRoutes(this);
    }

    /**
     * @see RouteBuilder
     * @throws Exception as per super class
     */
    @Override
    public void configure() throws Exception {
        configureExceptions();
        configureRoutes();
    }

    @SuppressWarnings("unchecked")
    protected void configureExceptions() {
        // retry mongo timeout exceptions indefinitely
        onException(MongoTimeoutException.class, MongoSocketException.class)
                .maximumRedeliveries(Integer.MAX_VALUE) // retry indefinitely
                .retryAttemptedLogLevel(LoggingLevel.ERROR) // log retry error stack as Log Level ERROR
                .redeliveryDelay(1000 * 60);  // wait one minutes until redelivery

        onException(JsonMappingException.class)
                .handled(true)
                .to(ROUTE_DIRECT_PUBLISH_EXCEPTION);

        from(ROUTE_DIRECT_PUBLISH_EXCEPTION)
                .process(exchange -> {
                    ErrorMessage errorDTO = mapErrorDTO(exchange);
                    exchange.getIn().setBody(errorDTO);
                    LOGGER.error(errorDTO.getMessage());
                })
                // serialize the DTO to JSON
                .marshal(new JacksonDataFormat(objectMapper, HashMap.class))
                .toD("kafka:{{config.kafka.topic.error}}?brokers={{config.kafka.host}}:{{config.kafka.port}}");

    }

    private ErrorMessage mapErrorDTO(final Exchange exchange) {
        Message originalMessage = exchange.getUnitOfWork().getOriginalInMessage();
        Exception exception = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Exception.class);
        ErrorMessage errorDTO = ErrorMessage.builder()
                .service(serviceName)
                .stackTrace(ExceptionUtils.getStackTrace(exception))
                .message(exception.getMessage())
                .build();
        errorDTO.addContext("camelMessage", originalMessage);
        errorDTO.addContext("camelHeaders", exchange.getIn().getHeaders());
        return errorDTO;
    }

    protected abstract void configureRoutes();
}

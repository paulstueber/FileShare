package de.paulstueber.ui.config;

import org.apache.camel.CamelContext;
import org.apache.camel.impl.DefaultShutdownStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * camel configuration class to define adjustments such as the shut down strategy (reduce timeout from 300 to 1 second)
 */
@Configuration
public class CamelConfig {

    @Autowired
    private CamelContext camelContext;

    /**
     * Create a shutdown strategy to reduce the shutdown timeout from 300 to 1 second
     * @return
     */
    @Bean
    DefaultShutdownStrategy defaultShutdownStrategy() {
        DefaultShutdownStrategy defaultShutdownStrategy = new DefaultShutdownStrategy(camelContext);
        defaultShutdownStrategy.setTimeout(1);
        return defaultShutdownStrategy;
    }
}

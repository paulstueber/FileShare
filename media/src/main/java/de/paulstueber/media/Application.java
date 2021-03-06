package de.paulstueber.media;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Media Service - Application entry point
 */
@SpringBootApplication
@EnableDiscoveryClient
@SuppressWarnings("hideutilityclassconstructor")
public class Application {

    /**
     * Main method called when starting the service
     * @param args command line parameter
     */
    public static void main(final String[] args) {
        SpringApplication.run(Application.class, args);
    }
}

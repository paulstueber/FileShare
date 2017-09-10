package de.paulstueber.ui.utils;

import lombok.NonNull;
import org.apache.camel.CamelContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Utility for Camel related generic functionality
 */
@Service
public class CamelUtils {

    @Autowired
    private CamelContext camelContext;

    /**
     * Forwards a message to camel endoint 'camelEndpoint'
     * @param message the message string to forward
     */
    public void forward(@NonNull final String camelEndpoint, @NonNull final Object message) {
        camelContext.createProducerTemplate().requestBody(camelEndpoint, message);
    }
}

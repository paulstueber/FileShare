package de.paulstueber.ui.model.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Error object to be published if an error occurs
 */
@Data
@AllArgsConstructor
@Builder
public class ErrorMessage {
    private String service;
    private LocalDateTime time = LocalDateTime.now();
    private String message;
    private String stackTrace;
    private Map<String, Object> context = new HashMap<>();

    /**
     * constructor with default values (Lombok)
     */
    public static class ErrorMessageBuilder {
        private LocalDateTime time = LocalDateTime.now();
        private Map<String, Object> context = new HashMap<>();
    }

    /**
     * Add a new context message (any valuable information on the error message) to the error dto
     *
     * @param key key name
     * @param details additional information - any serializable object
     */
    public void addContext(@NonNull final String key, final Object details) {
        this.context.put(key, details);
    }
}

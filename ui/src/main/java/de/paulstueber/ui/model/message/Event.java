package de.paulstueber.ui.model.message;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import de.paulstueber.ui.model.FSEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Event class holding the original resource and information what action was performed
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Event {

    /**
     * Action to be applied for the message
     */
    public enum Action {
        /**
         * Create new. If the target already exists, it should be ignored
         */
        CREATE,
        /**
         * Update an existing entity. This can only be processed if the target entity already exists
         */
        UPDATE,
        /**
         * Create OR update an entity. Overwrite everything.
         */
        SAVE,
        /**
         * Delete an existing entity
         */
        DELETE,
        /**
         * merge an existing entity
         */
        MERGE,
        /**
         * Create and/or overwrite an entity
         */
        RECREATE,
        /**
         * REDUMP entities
         */
        REDUMP;
    }

    private String service;
    private String originId;
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime time = LocalDateTime.now();
    private Action action;
    private FSEntity content;

    /**
     * Constructor
     * @param service name of source service
     * @param action action
     * @param content content dto
     */
    public Event(final String service, final Action action, final FSEntity content) {
        this(service, UUID.randomUUID().toString(), action, content);
    }

    /**
     * Constructor
     * @param service name of source service
     * @param originId id set for a message thrown in the notification circle for the first time
     * @param action action
     * @param content content dto
     */
    public Event(@NonNull final String service, final String originId, @NonNull final Action action, final FSEntity content) {
        this.service = service;
        this.originId = originId;
        this.action = action;
        this.content = content;
    }
}

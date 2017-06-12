package de.paulstueber.ui.exception;

import lombok.NonNull;

/**
 * thrown if an requested entity does not exist
 */
public class EntityNotFoundException extends Exception {

    /**
     * default constructor
     *
     * @param message descriptive error message
     */
    public EntityNotFoundException(@NonNull final String message) {
        super(message);
    }
}

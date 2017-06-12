package de.paulstueber.ui.exception;

import lombok.NonNull;

/**
 * thrown if an requested entity already exists (e.g. creation)
 */
public class EntityAlreadyExistsException extends Exception {

    /**
     * default constructor
     *
     * @param message descriptive error message
     */
    public EntityAlreadyExistsException(@NonNull final String message) {
        super(message);
    }
}

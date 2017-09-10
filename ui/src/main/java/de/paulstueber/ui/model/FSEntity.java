package de.paulstueber.ui.model;

import java.io.Serializable;

/**
 * Required interface for all entities to be sent as message
 */
public interface FSEntity extends Serializable {

    /**
     * Required getter for all entities that are stored in DB
     * @return the entities ID
     */
    String getId();
}

package de.paulstueber.ui.model;

/**
 * user roles
 */
public enum Role {
    /**
     * Default user with no special privileges
     */
    USER,
    /**
     * Default user with limited privileges
     */
    MODERATOR,
    /**
     * Admin user with special/extended rights
     */
    ADMIN,
    /**
     * Admin user with super special & super extended rights
     */
    SUPER_ADMIN;
}

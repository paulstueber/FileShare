package de.paulstueber.ui.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 * Controller advice class, to automatically bind objects to all View Models
 */
@ControllerAdvice
public class CurrentUserControllerAdvice {

    /**
     * Binds the currently logged in user to all Views.
     *
     * @param authentication
     * @return the currently logged in user
     */
    @ModelAttribute("currentUser")
    public User getCurrentUser(final Authentication authentication) {
        return (authentication == null) ? null : (User) authentication.getPrincipal();
    }


}

package de.paulstueber.media.controller;

import de.paulstueber.media.exception.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Global exception handler for all controllers
 */
@ControllerAdvice
class ControllerExceptionHandler {

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)  // 500
    @ExceptionHandler(IOException.class)
    /**
     * Exception handler for all internal exceptions
     * @param e the exception
     * @return 500 response entity
     */
    public void internalException() { }

    @ResponseStatus(HttpStatus.NOT_FOUND)  // 404
    @ExceptionHandler({FileNotFoundException.class, EntityNotFoundException.class})
    /**
     * Exception handler for unavailable resources
     * @param e the exception
     * @return 404 response entity
     */
    public void schemaException() { }
}

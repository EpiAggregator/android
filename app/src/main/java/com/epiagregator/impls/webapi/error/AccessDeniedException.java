package com.epiagregator.impls.webapi.error;

/**
 * Created by etien on 27/10/2016.
 */

public class AccessDeniedException extends Exception {
    /**
     * Serialization version number.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Initializes this instance with default error message
     * {@literal "No internet connection"}.
     */
    public AccessDeniedException() {
        this("Access denied");
    }

    /**
     * Initializes this instance with the specified error {@code message}.
     *
     * @param message the error message.
     */
    public AccessDeniedException(String message) {
        super(message);
    }

    /**
     * Initializes this instance with the specified error {@code message}
     * and the {@code throwable} root cause.
     *
     * @param message   the error message.
     * @param throwable the root cause of the error.
     */
    public AccessDeniedException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
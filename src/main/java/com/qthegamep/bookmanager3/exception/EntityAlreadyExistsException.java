package com.qthegamep.bookmanager3.exception;

/**
 * This exception should be thrown when trying to add an entity to the database when it already exists.
 */
public class EntityAlreadyExistsException extends RuntimeException {

    /**
     * This is the constructor that calls the super constructor and passes the error message to is.
     *
     * @param message to be displayed or logged.
     */
    public EntityAlreadyExistsException(String message) {
        super(message);
    }
}

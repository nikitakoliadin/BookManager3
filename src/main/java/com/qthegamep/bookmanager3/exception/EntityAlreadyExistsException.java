package com.qthegamep.bookmanager3.exception;

/**
 * This exception should be thrown when trying to add an entity to the database whet it already exists.
 */
public class EntityAlreadyExistsException extends RuntimeException {

    public EntityAlreadyExistsException(String message) {
        super(message);
    }
}

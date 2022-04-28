package com.notification.service.root.exception;

public class EntityNotUpdatedException extends RuntimeException {
    private final String message;

    public EntityNotUpdatedException(String entityName) {
        message = entityName + " was not updated because id is null or \"0\"";
    }

    @Override
    public String getMessage() {
        return message;
    }
}

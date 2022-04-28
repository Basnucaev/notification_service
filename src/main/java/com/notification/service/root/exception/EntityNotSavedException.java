package com.notification.service.root.exception;

public class EntityNotSavedException extends RuntimeException {
    private final String message;

    public EntityNotSavedException(String entityName) {
        message = entityName + " was not saved because id is not null";
    }

    @Override
    public String getMessage() {
        return message;
    }
}

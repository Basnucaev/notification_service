package com.notification.service.root.exception;

public class ClientNotFoundException extends RuntimeException {
    private final String message;

    public ClientNotFoundException(Long id) {
        this.message = "Client with id= " + id + " not found";
    }

    @Override
    public String getMessage() {
        return message;
    }
}

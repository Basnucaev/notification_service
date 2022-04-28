package com.notification.service.root.exception;

public class MailingNotFoundException extends RuntimeException {
    private final String message;

    public MailingNotFoundException(Long id) {
        this.message = "Mailing with id= " + id + " not found";
    }

    @Override
    public String getMessage() {
        return message;
    }
}

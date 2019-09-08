package io.hexlet.hexletcorrection.controller.exception;

import static java.lang.String.format;

public class UserNotFoundException extends RuntimeException {

    private String message;

    public UserNotFoundException(Long id) {
        super(format("User with ID '%d' not found", id));
        this.message = format("User with ID '%d' not found", id);
    }

    public UserNotFoundException(String name) {
        super(format("User with name '%s' not found", name));
        this.message = format("User with name '%s' not found", name);
    }

    @Override
    public String getMessage() {
        return message;
    }
}

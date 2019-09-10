package io.hexlet.hexletcorrection.controller.exception;

import static java.lang.String.format;

public class AccountNotFoundException extends RuntimeException {

    private static final String ACCOUNT_NOT_FOUND = "Account with ID '%d' not found";

    private final String message;

    public AccountNotFoundException(Long id) {
        super(format(ACCOUNT_NOT_FOUND, id));
        this.message = format(ACCOUNT_NOT_FOUND, id);
    }

    @Override
    public String getMessage() {
        return message;
    }
}

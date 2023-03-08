package io.hexlet.typoreporter.web.exception;

import org.springframework.http.ProblemDetail;
import org.springframework.web.ErrorResponseException;

import static java.text.MessageFormat.format;
import static org.springframework.http.HttpStatus.NOT_FOUND;

public class AccountNotFoundException extends ErrorResponseException {

    private static final String NOT_FOUND_MSG = "Account with email=''{0}'' not found";

    public AccountNotFoundException(final String email) {
        super(NOT_FOUND, ProblemDetail.forStatusAndDetail(NOT_FOUND, "Account not found"), null, format(NOT_FOUND_MSG, email), new Object[]{email});
    }
}

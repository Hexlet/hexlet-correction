package io.hexlet.typoreporter.web.exception;

import org.zalando.problem.AbstractThrowableProblem;

import static java.text.MessageFormat.format;
import static org.zalando.problem.Status.NOT_FOUND;

public class AccountNotFoundException extends AbstractThrowableProblem {

    private static final String NOT_FOUND_MSG = "Account with email=''{0}'' not found";

    public AccountNotFoundException(final String email) {
        super(null, "Account not found", NOT_FOUND, format(NOT_FOUND_MSG, email));
    }
}

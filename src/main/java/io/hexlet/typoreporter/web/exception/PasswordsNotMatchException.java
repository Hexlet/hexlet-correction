package io.hexlet.typoreporter.web.exception;

import org.springframework.validation.FieldError;
import org.zalando.problem.AbstractThrowableProblem;
import static org.zalando.problem.Status.CONFLICT;

public class PasswordsNotMatchException extends AbstractThrowableProblem {

    private static final String MESSAGE_TEMPLATE = "Passwords doesn't match";

    public PasswordsNotMatchException() {
        super(null, "Passwords doesn't match", CONFLICT, MESSAGE_TEMPLATE);
    }

    public static FieldError fieldNameError() {
        return new FieldError(
            "updatePassword",
            "confirmPassword",
            null,
            false,
            null,
            null,
            MESSAGE_TEMPLATE
        );
    }
}

package io.hexlet.typoreporter.web.exception;

import org.springframework.validation.FieldError;
import org.zalando.problem.AbstractThrowableProblem;
import static org.zalando.problem.Status.CONFLICT;

public class OldPasswordWrongException extends AbstractThrowableProblem {
    private static final String MESSAGE_TEMPLATE = "Wrong old password";

    public OldPasswordWrongException() {
        super(null, "Wrong old password", CONFLICT, MESSAGE_TEMPLATE);
    }

    public static FieldError fieldNameError() {
        return new FieldError(
            "updatePassword",
            "oldPassword",
            null,
            false,
            null,
            null,
            MESSAGE_TEMPLATE
        );
    }
}

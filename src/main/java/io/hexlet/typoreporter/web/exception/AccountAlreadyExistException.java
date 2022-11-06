package io.hexlet.typoreporter.web.exception;

import org.springframework.validation.FieldError;
import org.zalando.problem.AbstractThrowableProblem;
import static java.text.MessageFormat.format;
import static org.zalando.problem.Status.CONFLICT;

public class AccountAlreadyExistException extends AbstractThrowableProblem {

    private static final String MESSAGE_TEMPLATE = "Account with {0} ''{1}'' already exists";

    private final String fieldName;

    private final String errorValue;

    public AccountAlreadyExistException(final String fieldName, final String errorValue) {
        super(null, "Account already exists", CONFLICT, format(MESSAGE_TEMPLATE, fieldName, errorValue));
        this.fieldName = fieldName;
        this.errorValue = errorValue;
    }

    public FieldError toFieldError(final String objectName) {
        return new FieldError(
            objectName,
            fieldName,
            errorValue,
            false,
            null,
            null,
            format(MESSAGE_TEMPLATE,fieldName, errorValue)
        );
    }
}

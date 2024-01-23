package io.hexlet.typoreporter.handler.exception;

import org.springframework.http.ProblemDetail;
import org.springframework.validation.FieldError;
import org.springframework.web.ErrorResponseException;

import static java.text.MessageFormat.format;
import static org.springframework.http.HttpStatus.CONFLICT;

public class AccountAlreadyExistException extends ErrorResponseException {

    private static final String MESSAGE_TEMPLATE = "Account with {0} ''{1}'' already exists";

    private final String fieldName;

    private final String errorValue;

    public AccountAlreadyExistException(final String fieldName, final String errorValue) {
        super(CONFLICT, ProblemDetail.forStatusAndDetail(CONFLICT, "Account already exists"), null, format(MESSAGE_TEMPLATE, fieldName, errorValue), new Object[]{fieldName, errorValue});
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
            format(MESSAGE_TEMPLATE, fieldName, errorValue)
        );
    }
}

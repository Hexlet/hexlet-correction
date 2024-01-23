package io.hexlet.typoreporter.handler.exception;

import org.springframework.http.ProblemDetail;
import org.springframework.validation.FieldError;
import org.springframework.web.ErrorResponseException;

import static org.springframework.http.HttpStatus.CONFLICT;

public class OldPasswordWrongException extends ErrorResponseException implements ConvertibleToFieldError {

    private static final String MESSAGE_TEMPLATE = "Wrong old password";

    public OldPasswordWrongException() {
        super(CONFLICT, ProblemDetail.forStatusAndDetail(CONFLICT, "Wrong old password"), null, MESSAGE_TEMPLATE, null);
    }

    @Override
    public FieldError toFieldError(final String objectName) {
        return new FieldError(
            objectName,
            "oldPassword",
            null,
            false,
            null,
            null,
            MESSAGE_TEMPLATE
        );
    }
}

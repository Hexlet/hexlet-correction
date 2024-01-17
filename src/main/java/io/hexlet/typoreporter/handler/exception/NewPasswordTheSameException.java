package io.hexlet.typoreporter.handler.exception;

import org.springframework.http.ProblemDetail;
import org.springframework.validation.FieldError;
import org.springframework.web.ErrorResponseException;

import static org.springframework.http.HttpStatus.CONFLICT;

public class NewPasswordTheSameException extends ErrorResponseException implements ConvertibleToFieldError {

    private static final String MESSAGE_TEMPLATE = "New password is the same as the old one";

    public NewPasswordTheSameException() {
        super(CONFLICT, ProblemDetail.forStatusAndDetail(CONFLICT, "The same new password"), null, MESSAGE_TEMPLATE, null);
    }

    @Override
    public FieldError toFieldError(final String objectName) {
        return new FieldError(
            objectName,
            "newPassword",
            null,
            false,
            null,
            null,
            MESSAGE_TEMPLATE
        );
    }
}

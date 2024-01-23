package io.hexlet.typoreporter.handler.exception;

import org.springframework.http.ProblemDetail;
import org.springframework.validation.FieldError;
import org.springframework.web.ErrorResponseException;

import static java.text.MessageFormat.format;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

public class WorkspaceAlreadyExistException extends ErrorResponseException {

    private static final String MESSAGE_TEMPLATE = "Workspace with {0} ''{1}'' already exists";

    private final String fieldName;

    private final String errorValue;

    public WorkspaceAlreadyExistException(String fieldName, String errorValue) {
        super(BAD_REQUEST, ProblemDetail.forStatusAndDetail(BAD_REQUEST, "Workspace already exists"), null, format(MESSAGE_TEMPLATE, fieldName, errorValue),
            new Object[]{fieldName, errorValue});
        this.fieldName = fieldName;
        this.errorValue = errorValue;
    }

    public FieldError toFieldError(String objectName) {
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

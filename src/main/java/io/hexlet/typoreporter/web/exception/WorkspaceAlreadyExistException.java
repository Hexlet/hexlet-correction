package io.hexlet.typoreporter.web.exception;

import org.springframework.validation.FieldError;
import org.springframework.web.ErrorResponseException;

import static java.text.MessageFormat.format;
import static org.springframework.http.HttpStatus.CONFLICT;

public class WorkspaceAlreadyExistException extends ErrorResponseException {

    private static final String MESSAGE_TEMPLATE = "Workspace with {0} ''{1}'' already exists";

    private final String fieldName;

    private final String errorValue;

    public WorkspaceAlreadyExistException(String fieldName, String errorValue) {
        super(CONFLICT);
        this.setTitle("Workspace already exists");
        this.getBody().setDetail(format(MESSAGE_TEMPLATE, fieldName, errorValue));
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

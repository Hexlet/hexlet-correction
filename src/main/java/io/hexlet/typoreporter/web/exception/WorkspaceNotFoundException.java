package io.hexlet.typoreporter.web.exception;

import org.springframework.http.ProblemDetail;
import org.springframework.web.ErrorResponseException;

import static java.text.MessageFormat.format;
import static org.springframework.http.HttpStatus.NOT_FOUND;

public class WorkspaceNotFoundException extends ErrorResponseException {

    private static final String NAME_NOT_FOUND_MSG = "Workspace with name=''{0}'' not found";

    public WorkspaceNotFoundException(final String name) {
        super(NOT_FOUND, ProblemDetail.forStatusAndDetail(NOT_FOUND, "Workspace not found"), null, format(NAME_NOT_FOUND_MSG, name), new Object[]{name});
    }
}

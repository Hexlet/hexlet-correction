package io.hexlet.typoreporter.handler.exception;

import org.springframework.http.ProblemDetail;
import org.springframework.web.ErrorResponseException;

import static java.text.MessageFormat.format;
import static org.springframework.http.HttpStatus.NOT_FOUND;

public class WorkspaceNotFoundException extends ErrorResponseException {

    private static final String NAME_NOT_FOUND_MSG = "Workspace with name=''{0}'' not found";

    private static final String ID_NOT_FOUND_MSG = "Workspace with id=''{0}'' not found";

    public WorkspaceNotFoundException(final Long id) {
        super(NOT_FOUND, ProblemDetail.forStatusAndDetail(NOT_FOUND, "Workspace not found"), null, format(ID_NOT_FOUND_MSG, id), new Object[]{id});
    }

    public WorkspaceNotFoundException(final String wksIdStr, final Throwable cause) {
        super(NOT_FOUND, ProblemDetail.forStatusAndDetail(NOT_FOUND, "Workspace not found"), cause,
            "Workspace with id='" + wksIdStr + "' not found"
            , new Object[]{wksIdStr});
    }
}

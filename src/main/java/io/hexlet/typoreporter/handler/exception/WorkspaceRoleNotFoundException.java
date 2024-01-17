package io.hexlet.typoreporter.handler.exception;

import org.springframework.http.ProblemDetail;
import org.springframework.web.ErrorResponseException;

import static java.text.MessageFormat.format;
import static org.springframework.http.HttpStatus.NOT_FOUND;

public class WorkspaceRoleNotFoundException extends ErrorResponseException {
    private static final String NAME_NOT_FOUND_MSG = "Workspace role with accountId=''{0}'' and workspaceId=''{1}'' not found";

    public WorkspaceRoleNotFoundException(final Long accountId, final Long workspaceId) {
        super(NOT_FOUND, ProblemDetail.forStatusAndDetail(NOT_FOUND, "Workspace role not found"), null,
            format(NAME_NOT_FOUND_MSG, accountId, workspaceId), new Object[]{accountId, workspaceId});
    }

}

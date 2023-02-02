package io.hexlet.typoreporter.web.exception;

import org.springframework.web.ErrorResponseException;

import static java.text.MessageFormat.format;
import static org.springframework.http.HttpStatus.NOT_FOUND;

public class WorkspaceNotFoundException extends ErrorResponseException {

    public WorkspaceNotFoundException(final String name) {
        super(NOT_FOUND);
        this.setTitle("Workspace not found");
        this.getBody().setDetail(format("Workspace with name=''{0}'' not found", name));
    }
}

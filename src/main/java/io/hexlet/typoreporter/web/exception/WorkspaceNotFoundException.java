package io.hexlet.typoreporter.web.exception;

import org.zalando.problem.AbstractThrowableProblem;

import static java.text.MessageFormat.format;
import static org.zalando.problem.Status.NOT_FOUND;

public class WorkspaceNotFoundException extends AbstractThrowableProblem {

    private static final String NAME_NOT_FOUND_MSG = "Workspace with name=''{0}'' not found";

    public WorkspaceNotFoundException(final String name) {
        super(null, "Workspace not found", NOT_FOUND, format(NAME_NOT_FOUND_MSG, name));
    }
}

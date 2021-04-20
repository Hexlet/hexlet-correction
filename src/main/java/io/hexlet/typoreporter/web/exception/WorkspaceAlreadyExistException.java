package io.hexlet.typoreporter.web.exception;

import io.hexlet.typoreporter.service.dto.workspace.CreateWorkspace;
import org.springframework.validation.FieldError;
import org.zalando.problem.AbstractThrowableProblem;

import static java.text.MessageFormat.format;
import static org.zalando.problem.Status.CONFLICT;

public class WorkspaceAlreadyExistException extends AbstractThrowableProblem {

    private static final String MESSAGE_TEMPLATE = "Workspace with name ''{0}'' already exists";

    public WorkspaceAlreadyExistException(final String wksName) {
        super(null, "Workspace already exists", CONFLICT, format(MESSAGE_TEMPLATE, wksName));
    }

    public static FieldError fieldNameError(final CreateWorkspace createWorkspace) {
        return new FieldError(
            "createWorkspace",
            "name",
            createWorkspace.name(),
            false,
            null,
            null,
            format(MESSAGE_TEMPLATE, createWorkspace.name())
        );
    }
}

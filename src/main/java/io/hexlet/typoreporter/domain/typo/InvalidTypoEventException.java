package io.hexlet.typoreporter.domain.typo;

import org.zalando.problem.AbstractThrowableProblem;

import java.text.MessageFormat;

import static org.zalando.problem.Status.BAD_REQUEST;

public class InvalidTypoEventException extends AbstractThrowableProblem {

    private static final String MESSAGE = "Invalid event ''{0}'' for typo status ''{1}''. Valid events: {2}";

    public InvalidTypoEventException(final TypoStatus status, final TypoEvent event) {
        super(
                null,
                "Invalid event",
                BAD_REQUEST,
                MessageFormat.format(MESSAGE, event, status, status.getValidEvents())
        );
    }
}

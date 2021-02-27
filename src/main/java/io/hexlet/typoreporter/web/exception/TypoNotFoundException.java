package io.hexlet.typoreporter.web.exception;

import org.zalando.problem.AbstractThrowableProblem;

import static java.text.MessageFormat.format;
import static org.zalando.problem.Status.NOT_FOUND;

public class TypoNotFoundException extends AbstractThrowableProblem {

    private static final String MESSAGE = "Typo with id=''{0}'' not found";

    public TypoNotFoundException(final Long id) {
        super(null, "Typo not found", NOT_FOUND, format(MESSAGE, id));
    }
}

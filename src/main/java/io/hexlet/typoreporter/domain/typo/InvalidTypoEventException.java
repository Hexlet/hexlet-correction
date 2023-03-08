package io.hexlet.typoreporter.domain.typo;

import org.springframework.http.ProblemDetail;
import org.springframework.web.ErrorResponseException;

import static java.text.MessageFormat.format;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

public class InvalidTypoEventException extends ErrorResponseException {

    private static final String MESSAGE = "Invalid event ''{0}'' for typo status ''{1}''. Valid events: {2}";

    public InvalidTypoEventException(final TypoStatus status, final TypoEvent event) {
        super(BAD_REQUEST, ProblemDetail.forStatusAndDetail(BAD_REQUEST, "Invalid event"), null,
            format(MESSAGE, event, status, status.getValidEvents()), new Object[]{status, event});
    }
}

package io.hexlet.typoreporter.handler.exception;

import org.springframework.http.ProblemDetail;
import org.springframework.web.ErrorResponseException;

import static java.text.MessageFormat.format;
import static org.springframework.http.HttpStatus.NOT_FOUND;

public class TypoNotFoundException extends ErrorResponseException {

    private static final String MESSAGE = "Typo with id=''{0}'' not found";

    public TypoNotFoundException(final Long id) {
        super(NOT_FOUND, ProblemDetail.forStatusAndDetail(NOT_FOUND, "Typo not found"), null, format(MESSAGE, id), new Object[]{id});
    }
}

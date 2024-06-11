package io.hexlet.typoreporter.handler.exception;

import org.springframework.http.ProblemDetail;
import org.springframework.web.ErrorResponseException;

import static java.text.MessageFormat.format;
import static org.springframework.http.HttpStatus.FORBIDDEN;

public class ForbiddenDomainException extends ErrorResponseException {

    private static final String MESSAGE = "Domain ''{0}'' is forbidden";

    public ForbiddenDomainException(final String domain) {
        super(FORBIDDEN, ProblemDetail.forStatusAndDetail(FORBIDDEN,
                                        "Domain is forbidden"),
                                        null,
                                        format(MESSAGE, domain),
                                        new Object[]{domain});
    }
}

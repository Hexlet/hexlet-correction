package io.hexlet.typoreporter.handler.exception;

import org.springframework.http.ProblemDetail;
import org.springframework.web.ErrorResponseException;

import static org.springframework.http.HttpStatus.NOT_FOUND;

public class AllowedUrlNotFoundException extends ErrorResponseException {
    public AllowedUrlNotFoundException(final String url, final Long wksId) {
        super(NOT_FOUND, ProblemDetail.forStatusAndDetail(NOT_FOUND, "Allowed URL not found"), null,
            "Allowed URL with name='" + url + "' for workspace with id=" + wksId + " not found",
            new Object[]{url});
    }
}

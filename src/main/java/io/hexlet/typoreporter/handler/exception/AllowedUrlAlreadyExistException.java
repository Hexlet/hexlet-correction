package io.hexlet.typoreporter.handler.exception;

import org.springframework.http.ProblemDetail;
import org.springframework.web.ErrorResponseException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

public class AllowedUrlAlreadyExistException extends ErrorResponseException {
    public AllowedUrlAlreadyExistException(final String url, final Long wksId) {
        super(BAD_REQUEST, ProblemDetail.forStatusAndDetail(BAD_REQUEST, "Allowed URL already exists"), null,
            "URL \"" + url + "\" has already been added to workspace with ID=" + wksId,
            new Object[]{url});
    }
}

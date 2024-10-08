package io.hexlet.typoreporter.handler.exception;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.web.ErrorResponseException;

public class OAuth2Exception extends ErrorResponseException {
    public OAuth2Exception(HttpStatusCode status, ProblemDetail body, Throwable cause) {
        super(status, body, cause);
    }
}

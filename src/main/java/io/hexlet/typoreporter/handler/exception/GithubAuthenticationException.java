package io.hexlet.typoreporter.handler.exception;

import org.springframework.web.reactive.function.client.WebClientException;

public class GithubAuthenticationException extends WebClientException {

    public GithubAuthenticationException(String s) {
        super(s);
    }
}

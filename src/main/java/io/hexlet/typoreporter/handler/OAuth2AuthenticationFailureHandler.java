package io.hexlet.typoreporter.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;

public class OAuth2AuthenticationFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {

        String errorMessage = exception.getMessage();
        if (errorMessage == null) {
            OAuth2AuthenticationException oAuth2Exception = (OAuth2AuthenticationException) exception;
            OAuth2Error error = oAuth2Exception.getError();
            errorMessage = error.getErrorCode();
        }

        request.getSession().setAttribute("errorMessage", errorMessage.trim());
        response.sendRedirect("/login?error");
    }
}

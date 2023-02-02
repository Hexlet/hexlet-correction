package io.hexlet.typoreporter.web.exception;

import org.springframework.web.ErrorResponseException;

import static java.text.MessageFormat.format;
import static org.springframework.http.HttpStatus.NOT_FOUND;

public class TypoNotFoundException extends ErrorResponseException {

    public TypoNotFoundException(final Long id) {
        super(NOT_FOUND);
        this.setTitle("Typo not found");
        this.getBody().setDetail(format("Typo with id=''{0}'' not found", id));
    }
}

package io.hexlet.hexletcorrection.controller.exception;

import static java.lang.String.format;

public class CorrectionNotFoundException extends RuntimeException {

    private static final String CORRECTION_NOT_FOUND = "Correction with ID '%d' not found";

    private final String message;

    public CorrectionNotFoundException(Long id) {
        super(format(CORRECTION_NOT_FOUND, id));
        this.message = format(CORRECTION_NOT_FOUND, id);
    }

    @Override
    public String getMessage() {
        return message;
    }
}

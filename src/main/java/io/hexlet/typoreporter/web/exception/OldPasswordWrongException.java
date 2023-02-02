package io.hexlet.typoreporter.web.exception;

import org.springframework.validation.FieldError;
import org.springframework.web.ErrorResponseException;

import static org.springframework.http.HttpStatus.CONFLICT;

public class OldPasswordWrongException extends ErrorResponseException implements ConvertibleToFieldError {

    public OldPasswordWrongException() {
        super(CONFLICT);
        this.setTitle("Wrong old password");
        this.getBody().setDetail("Wrong old password");
    }

    @Override
    public FieldError toFieldError(final String objectName) {
        return new FieldError(
            objectName,
            "oldPassword",
            null,
            false,
            null,
            null,
            "Wrong old password"
        );
    }
}

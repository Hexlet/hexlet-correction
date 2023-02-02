package io.hexlet.typoreporter.web.exception;

import org.springframework.validation.FieldError;
import org.springframework.web.ErrorResponseException;

import static org.springframework.http.HttpStatus.CONFLICT;

public class NewPasswordTheSameException extends ErrorResponseException implements ConvertibleToFieldError {

    public NewPasswordTheSameException() {
        super(CONFLICT);
        this.setTitle("The same new password");
        this.getBody().setDetail("New password is the same as the old one");
    }

    @Override
    public FieldError toFieldError(final String objectName) {
        return new FieldError(
            objectName,
            "newPassword",
            null,
            false,
            null,
            null,
            "New password is the same as the old one"
        );
    }
}

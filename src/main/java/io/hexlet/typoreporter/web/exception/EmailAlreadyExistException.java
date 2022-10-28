package io.hexlet.typoreporter.web.exception;

import io.hexlet.typoreporter.service.dto.account.UpdateProfile;
import org.springframework.validation.FieldError;
import org.zalando.problem.AbstractThrowableProblem;
import static java.text.MessageFormat.format;
import static org.zalando.problem.Status.CONFLICT;

public class EmailAlreadyExistException extends AbstractThrowableProblem {

    private static final String MESSAGE_TEMPLATE = "Account with email ''{0}'' already exists";

    public EmailAlreadyExistException(final String email) {
        super(null, "Account already exists", CONFLICT, format(MESSAGE_TEMPLATE, email));
    }

    public static FieldError fieldNameError(final UpdateProfile updateProfile) {
        return new FieldError(
            "updateProfile",
            "email",
            updateProfile.getEmail(),
            false,
            null,
            null,
            format(MESSAGE_TEMPLATE, updateProfile.getEmail())
        );
    }
}

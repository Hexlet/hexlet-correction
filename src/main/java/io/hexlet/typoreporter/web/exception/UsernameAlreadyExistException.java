package io.hexlet.typoreporter.web.exception;

import io.hexlet.typoreporter.service.dto.account.UpdateProfile;
import org.springframework.validation.FieldError;
import org.zalando.problem.AbstractThrowableProblem;
import static java.text.MessageFormat.format;
import static org.zalando.problem.Status.CONFLICT;

public class UsernameAlreadyExistException extends AbstractThrowableProblem {

    private static final String MESSAGE_TEMPLATE = "Account with username ''{0}'' already exists";

    public UsernameAlreadyExistException(final String username) {
        super(null, "Account already exists", CONFLICT, format(MESSAGE_TEMPLATE, username));
    }

    public static FieldError fieldNameError(final UpdateProfile updateProfile) {
        return new FieldError(
            "updateProfile",
            "username",
            updateProfile.getUsername(),
            false,
            null,
            null,
            format(MESSAGE_TEMPLATE, updateProfile.getUsername())
        );
    }
}

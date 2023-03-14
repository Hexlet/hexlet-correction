package io.hexlet.typoreporter.service.account;

import lombok.Getter;
import org.springframework.core.NestedRuntimeException;

import static java.text.MessageFormat.format;

@Getter
public class UsernameAlreadyExistException extends NestedRuntimeException {

    private final String username;

    public UsernameAlreadyExistException(String username) {
        super(format("Account with username ''{0}'' already exists", username));
        this.username = username;
    }
}

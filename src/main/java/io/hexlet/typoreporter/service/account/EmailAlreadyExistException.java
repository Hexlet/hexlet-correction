package io.hexlet.typoreporter.service.account;

import lombok.Getter;
import org.springframework.core.NestedRuntimeException;

import static java.text.MessageFormat.format;

@Getter
public class EmailAlreadyExistException extends NestedRuntimeException {

    private final String email;

    public EmailAlreadyExistException(String email) {
        super(format("Account with email ''{0}'' already exists", email));
        this.email = email;
    }
}

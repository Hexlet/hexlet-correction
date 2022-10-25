package io.hexlet.typoreporter.service.converter;

import io.hexlet.typoreporter.domain.account.Account;
import io.hexlet.typoreporter.service.dto.account.SignupAccount;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class CreateAccountToAccount implements Converter<SignupAccount, Account> {

    // TODO docs
    @Override
    public Account convert(SignupAccount source) {
        return new Account()
            .setUsername(source.getUsername())
            .setEmail(source.getEmail())
            .setFirstName(source.getFirstName())
            .setLastName(source.getLastName())
            .setPassword(source.getPassword());
    }
}

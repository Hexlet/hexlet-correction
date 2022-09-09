package io.hexlet.typoreporter.service.converter;

import io.hexlet.typoreporter.domain.account.Account;
import io.hexlet.typoreporter.service.dto.account.CreateAccount;
import org.springframework.stereotype.Component;
import org.springframework.core.convert.converter.Converter;

@Component
public class CreateAccountToAccount implements Converter<CreateAccount, Account> {

    // TODO docs
    @Override
    public Account convert(CreateAccount source) {
        return new Account()
            .setUsername(source.username())
            .setEmail(source.email())
            .setFirstName(source.firstName())
            .setLastName(source.lastName())
            .setPassword(source.password());
    }
}

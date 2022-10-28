package io.hexlet.typoreporter.service.converter;

import io.hexlet.typoreporter.domain.account.Account;
import io.hexlet.typoreporter.service.dto.account.InfoAccount;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class AccountToInfoAccount implements Converter<Account, InfoAccount> {

    // TODO docs
    @Override
    public InfoAccount convert(final Account source) {
        return new InfoAccount(
            source.getEmail(),
            source.getUsername(),
            source.getFirstName(),
            source.getLastName()
        );
    }
}

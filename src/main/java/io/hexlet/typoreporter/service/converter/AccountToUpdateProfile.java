package io.hexlet.typoreporter.service.converter;

import io.hexlet.typoreporter.domain.account.Account;
import io.hexlet.typoreporter.service.dto.account.UpdateProfile;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class AccountToUpdateProfile implements Converter<Account, UpdateProfile> {

    // TODO docs
    @Override
    public UpdateProfile convert(final Account source) {
        return new UpdateProfile()
            .setUsername(source.getUsername())
            .setEmail(source.getEmail())
            .setFirstName(source.getFirstName())
            .setLastName(source.getLastName());
    }
}

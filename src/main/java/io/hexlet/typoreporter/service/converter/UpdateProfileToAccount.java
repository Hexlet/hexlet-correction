package io.hexlet.typoreporter.service.converter;

import io.hexlet.typoreporter.domain.account.Account;
import io.hexlet.typoreporter.service.dto.account.UpdateProfile;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

@Component
public class UpdateProfileToAccount implements Converter<Pair<Account, UpdateProfile>, Account> {

    // TODO docs
    @Override
    public Account convert(final Pair<Account, UpdateProfile> pair) {
        final UpdateProfile updateProfile = pair.getSecond();

        return pair.getFirst()
            .setUsername(updateProfile.getUsername())
            .setEmail(updateProfile.getEmail())
            .setFirstName(updateProfile.getFirstName())
            .setLastName(updateProfile.getLastName());
    }
}

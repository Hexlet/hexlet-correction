package io.hexlet.typoreporter.service.converter;

import io.hexlet.typoreporter.domain.account.Account;
import io.hexlet.typoreporter.service.dto.account.UpdateProfile;
import io.hexlet.typoreporter.service.mapper.AccountMapper;
import io.hexlet.typoreporter.service.mapper.AccountMapperImpl;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

@Component
public class UpdateProfileToAccount implements Converter<Pair<Account, UpdateProfile>, Account> {
    private final AccountMapper accountMapper = new AccountMapperImpl();
    // TODO docs
    @Override
    public Account convert(final Pair<Account, UpdateProfile> pair) {
        final UpdateProfile updateProfile = pair.getSecond();
        return accountMapper.toAccount(updateProfile);
    }
}

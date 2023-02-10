package io.hexlet.typoreporter.service.converter;

import io.hexlet.typoreporter.domain.account.Account;
import io.hexlet.typoreporter.service.dto.account.UpdateProfile;
import io.hexlet.typoreporter.service.mapper.AccountMapper;
import io.hexlet.typoreporter.service.mapper.AccountMapperImpl;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;


@Component
public class AccountToUpdateProfile implements Converter<Account, UpdateProfile> {
    private final AccountMapper accountMapper = new AccountMapperImpl();
    // TODO docs
    @Override
    public UpdateProfile convert(final Account source) {
        return accountMapper.toUpdateProfile(source);
    }
}

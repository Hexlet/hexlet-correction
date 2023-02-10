package io.hexlet.typoreporter.service.converter;

import io.hexlet.typoreporter.domain.account.Account;
import io.hexlet.typoreporter.service.dto.account.InfoAccount;
import io.hexlet.typoreporter.service.mapper.AccountMapper;
import io.hexlet.typoreporter.service.mapper.AccountMapperImpl;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class AccountToInfoAccount implements Converter<Account, InfoAccount> {
    private final AccountMapper accountMapper = new AccountMapperImpl();
    // TODO docs
    @Override
    public InfoAccount convert(final Account source) {
        return accountMapper.toInfoAccount(source);
    }
}

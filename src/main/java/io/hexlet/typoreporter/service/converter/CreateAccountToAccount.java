package io.hexlet.typoreporter.service.converter;

import io.hexlet.typoreporter.domain.account.Account;
import io.hexlet.typoreporter.service.dto.account.SignupAccount;
import io.hexlet.typoreporter.service.mapper.AccountMapper;
import io.hexlet.typoreporter.service.mapper.AccountMapperImpl;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class CreateAccountToAccount implements Converter<SignupAccount, Account> {
    private final AccountMapper accountMapper = new AccountMapperImpl();
    // TODO docs
    @Override
    public Account convert(SignupAccount source) {
        return accountMapper.toAccount(source);
    }
}

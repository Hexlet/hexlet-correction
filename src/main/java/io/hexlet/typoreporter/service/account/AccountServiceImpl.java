package io.hexlet.typoreporter.service.account;


import io.hexlet.typoreporter.domain.account.Account;
import io.hexlet.typoreporter.repository.AccountRepository;
import io.hexlet.typoreporter.service.dto.account.AccountRegistration;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    private final AccountMapper accountMapper;

    @Override
    @Transactional
    public Account registerAccount(final AccountRegistration accountRegistration) {
        Account newAccount = accountMapper.toAccountRegistration(accountRegistration);
        return accountRepository.save(newAccount);
    }
}

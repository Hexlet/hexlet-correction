package io.hexlet.typoreporter.service;

import io.hexlet.typoreporter.domain.account.Account;
import io.hexlet.typoreporter.domain.account.AuthProvider;
import io.hexlet.typoreporter.repository.AccountRepository;
import io.hexlet.typoreporter.service.dto.account.CreateAccount;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final ConversionService conversionService;
    private final PasswordEncoder encoder;

    public boolean saveAccount(Account account) {
        Optional<Account> accountFromDB = accountRepository.findAccountByUsername(account.getUsername());
        if (accountFromDB.isPresent()) {
            return false;
        }

        // account.setRoles(Collections.singleton(new Role(1L, "ROLE_USER"))); // TODO make roles
        account.setPassword(encoder.encode(account.getPassword()));
        accountRepository.save(account);
        return true;
    }

    public boolean saveAccount(CreateAccount source) {
        Account sourceAccount = conversionService.convert(source, Account.class);
        if (sourceAccount == null) {
            return false;
        }
        sourceAccount.setAuthProvider(AuthProvider.EMAIL); // TODO remove hard-coded stuff

        return saveAccount(sourceAccount);
    }

    // TODO implement allAccounts, deleteAccount, findAccountById, updateAccount
}

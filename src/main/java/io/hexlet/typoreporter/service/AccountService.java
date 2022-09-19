package io.hexlet.typoreporter.service;

import io.hexlet.typoreporter.domain.account.Account;
import io.hexlet.typoreporter.domain.account.AuthProvider;
import io.hexlet.typoreporter.repository.AccountRepository;
import io.hexlet.typoreporter.service.converter.CreateAccountToAccount;
import io.hexlet.typoreporter.service.dto.account.CreateAccount;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final CreateAccountToAccount converter;

    private final BCryptPasswordEncoder encoder;

    public boolean saveAccount(Account account) {
        Account accountFromDB = accountRepository.findAccountByUsername(account.getUsername())
            .orElse(null);

        if (accountFromDB != null) {
            return false;
        }

        // account.setRoles(Collections.singleton(new Role(1L, "ROLE_USER"))); // TODO make roles
        account.setPassword(encoder.encode(account.getPassword()));
        accountRepository.save(account);
        return true;
    }

    public boolean saveAccount(CreateAccount source) {
        Account sourceAccount = converter.convert(source);
        if (sourceAccount == null) {
            return false;
        }
        sourceAccount.setAuthProvider(AuthProvider.EMAIL); // TODO remove hard-coded stuff
        sourceAccount.setExternalOpenId("1L");

        return saveAccount(sourceAccount);
    }

    // TODO implement allAccounts, deleteAccount, findAccountById, updateAccount
}

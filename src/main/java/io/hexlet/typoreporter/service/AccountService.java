package io.hexlet.typoreporter.service;

import io.hexlet.typoreporter.domain.account.Account;
import io.hexlet.typoreporter.domain.account.AuthProvider;
import io.hexlet.typoreporter.repository.AccountRepository;
import io.hexlet.typoreporter.service.dto.account.SignupAccount;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AccountService implements SignUpAccount, QueryAccount {

    private final AccountRepository accountRepository;

    private final ConversionService conversionService;

    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public boolean existsByUsername(String username) {
        return accountRepository.existsByUsername(username);
    }

    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return accountRepository.existsByEmail(email);
    }

    @Override
    public Account signup(SignupAccount signupAccount) {
        Account sourceAccount = conversionService.convert(signupAccount, Account.class);
        if (sourceAccount == null) {
            return null;
        }
        sourceAccount.setAuthProvider(AuthProvider.EMAIL);

        boolean existsByUsername = existsByUsername(sourceAccount.getUsername());
        boolean existsByEmail = existsByEmail(sourceAccount.getEmail());
        if (existsByUsername || existsByEmail) {
            return null;
        }

        // account.setRoles(Collections.singleton(new Role(1L, "ROLE_USER"))); // TODO make roles
        sourceAccount.setPassword(passwordEncoder.encode(sourceAccount.getPassword()));
        return accountRepository.save(sourceAccount);
    }
}

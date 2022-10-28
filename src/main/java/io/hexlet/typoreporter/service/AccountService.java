package io.hexlet.typoreporter.service;

import io.hexlet.typoreporter.domain.account.Account;
import io.hexlet.typoreporter.domain.account.AuthProvider;
import io.hexlet.typoreporter.repository.AccountRepository;
import io.hexlet.typoreporter.service.dto.account.InfoAccount;
import io.hexlet.typoreporter.service.dto.account.SignupAccount;
import io.hexlet.typoreporter.service.dto.account.UpdatePassword;
import io.hexlet.typoreporter.service.dto.account.UpdateProfile;
import io.hexlet.typoreporter.web.exception.EmailAlreadyExistException;
import io.hexlet.typoreporter.web.exception.OldPasswordWrongException;
import io.hexlet.typoreporter.web.exception.PasswordsNotMatchException;
import io.hexlet.typoreporter.web.exception.UsernameAlreadyExistException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

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

    @Transactional(readOnly = true)
    public Optional<InfoAccount> getInfoAccount() {
        final var authentication = SecurityContextHolder.getContext().getAuthentication();
        final String name = authentication.getName();

        return accountRepository.findAccountByUsername(name)
            .map(account -> conversionService.convert(account, InfoAccount.class));
    }

    @Transactional(readOnly = true)
    public Optional<UpdateProfile> getUpdateProfile() {
        final var authentication = SecurityContextHolder.getContext().getAuthentication();
        final String name = authentication.getName();

        return accountRepository.findAccountByUsername(name)
            .map(account -> conversionService.convert(account, UpdateProfile.class));
    }

    @Transactional(readOnly = true)
    public Optional<Account> getAccount() {
        final var authentication = SecurityContextHolder.getContext().getAuthentication();
        final String name = authentication.getName();

        return accountRepository.findAccountByUsername(name);
    }

    @Transactional
    public Optional<Account> updateProfile(final UpdateProfile updateProfile) {
        final var sourceAccount = getAccount();

        final String username = sourceAccount.get().getUsername();
        if (!username.equals(updateProfile.getUsername()) && existsByUsername(updateProfile.getUsername())) {
            throw new UsernameAlreadyExistException(updateProfile.getUsername());
        }
        final String email = sourceAccount.get().getEmail();
        if (!email.equals(updateProfile.getEmail()) && existsByEmail(updateProfile.getEmail())) {
            throw new EmailAlreadyExistException(updateProfile.getEmail());
        }

        return sourceAccount
            .map(oldAcc -> oldAcc.setFirstName(updateProfile.getFirstName()))
            .map(oldAcc -> oldAcc.setLastName(updateProfile.getLastName()))
            .map(oldAcc -> oldAcc.setUsername(updateProfile.getUsername()))
            .map(oldAcc -> oldAcc.setEmail(updateProfile.getEmail()))
            .map(accountRepository::save);
    }

    @Transactional
    public Optional<Account> updatePassword(final UpdatePassword updatePassword) {
        final var sourceAccount = getAccount();
        final String password = sourceAccount.get().getPassword();

        if (!passwordEncoder.matches(updatePassword.getOldPassword(), password)) {
           throw new OldPasswordWrongException();
        }
        if (!updatePassword.getNewPassword().equals(updatePassword.getConfirmPassword())) {
            throw new PasswordsNotMatchException();
        }

        return sourceAccount
            .map(oldAcc -> oldAcc.setPassword(passwordEncoder.encode(updatePassword.getNewPassword())))
            .map(accountRepository::save);
    }
}

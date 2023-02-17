package io.hexlet.typoreporter.service;

import io.hexlet.typoreporter.domain.account.Account;
import io.hexlet.typoreporter.domain.account.AuthProvider;
import io.hexlet.typoreporter.domain.workspace.Workspace;
import io.hexlet.typoreporter.repository.AccountRepository;
import io.hexlet.typoreporter.service.dto.account.InfoAccount;
import io.hexlet.typoreporter.service.dto.account.SignupAccount;
import io.hexlet.typoreporter.service.dto.account.UpdatePassword;
import io.hexlet.typoreporter.service.dto.account.UpdateProfile;
import io.hexlet.typoreporter.service.dto.workspace.WorkspaceInfo;
import io.hexlet.typoreporter.service.mapper.AccountMapper;
import io.hexlet.typoreporter.service.mapper.WorkspaceMapper;
import io.hexlet.typoreporter.web.exception.NewPasswordTheSameException;
import io.hexlet.typoreporter.web.exception.OldPasswordWrongException;
import io.hexlet.typoreporter.web.exception.AccountAlreadyExistException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class AccountService implements SignUpAccount, QueryAccount {

    private final AccountRepository accountRepository;

    private final AccountMapper accountMapper;

    private final WorkspaceMapper workspaceMapper;

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
        Account sourceAccount = accountMapper.toAccount(signupAccount);
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
    public Optional<InfoAccount> getInfoAccount(final String name) {
        return accountRepository.findAccountByUsername(name)
            .map(accountMapper::toInfoAccount);
    }

    @Transactional(readOnly = true)
    public Optional<WorkspaceInfo> getWorkspaceInfoByUsername(final String name) {
        final var sourceAccount = accountRepository.findAccountByUsername(name);

        Optional<Workspace> sourceWorkspace = Optional.empty();
        if (sourceAccount.isPresent()) {
            sourceWorkspace = Optional.ofNullable(sourceAccount.get().getWorkspace());
        }

        return sourceWorkspace.map(workspaceMapper::toWorkspaceInfo);
    }

    @Transactional(readOnly = true)
    public Optional<UpdateProfile> getUpdateProfile(final String name) {
        return accountRepository.findAccountByUsername(name)
            .map(accountMapper::toUpdateProfile);
    }

    public Optional<Account> updateProfile(final UpdateProfile updateProfile, final String name) {
        final var sourceAccount = accountRepository.findAccountByUsername(name);

        if (sourceAccount.isPresent()) {
            final String username = sourceAccount.get().getUsername();
            if (!username.equals(updateProfile.getUsername()) && existsByUsername(updateProfile.getUsername())) {
                throw new AccountAlreadyExistException("username", updateProfile.getUsername());
            }

            final String email = sourceAccount.get().getEmail();
            if (!email.equals(updateProfile.getEmail()) && existsByEmail(updateProfile.getEmail())) {
                throw new AccountAlreadyExistException("email", updateProfile.getEmail());
            }
        }

        return sourceAccount
            .map(oldAcc -> accountMapper.toAccount(updateProfile, oldAcc))
            .map(accountRepository::save);
    }

    public Optional<Account> updatePassword(final UpdatePassword updatePassword, final String name) {
        final var sourceAccount = accountRepository.findAccountByUsername(name);

        if (sourceAccount.isPresent()) {
            final String password = sourceAccount.get().getPassword();

            if (!passwordEncoder.matches(updatePassword.getOldPassword(), password)) {
                throw new OldPasswordWrongException();
            }

            if (passwordEncoder.matches(updatePassword.getNewPassword(), password)) {
                throw new NewPasswordTheSameException();
            }
        }

        return sourceAccount
            .map(oldAcc -> oldAcc.setPassword(passwordEncoder.encode(updatePassword.getNewPassword())))
            .map(accountRepository::save);
    }

    public String getCurrentAccountEmail() {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        return accountRepository.findAccountByUsername(name).orElseThrow().getEmail();
    }
}

package io.hexlet.typoreporter.service;

import io.hexlet.typoreporter.domain.account.Account;
import io.hexlet.typoreporter.domain.account.AuthProvider;
import io.hexlet.typoreporter.repository.AccountRepository;
import io.hexlet.typoreporter.repository.WorkspaceRoleRepository;
import io.hexlet.typoreporter.service.account.EmailAlreadyExistException;
import io.hexlet.typoreporter.service.account.UsernameAlreadyExistException;
import io.hexlet.typoreporter.service.account.signup.SignupAccount;
import io.hexlet.typoreporter.service.account.signup.SignupAccountUseCase;
import io.hexlet.typoreporter.service.dto.account.InfoAccount;
import io.hexlet.typoreporter.service.dto.account.UpdatePassword;
import io.hexlet.typoreporter.service.dto.account.UpdateProfile;
import io.hexlet.typoreporter.service.dto.workspace.WorkspaceRoleInfo;
import io.hexlet.typoreporter.service.mapper.AccountMapper;
import io.hexlet.typoreporter.service.mapper.WorkspaceRoleMapper;
import io.hexlet.typoreporter.utils.TextUtils;
import io.hexlet.typoreporter.handler.exception.AccountAlreadyExistException;
import io.hexlet.typoreporter.handler.exception.AccountNotFoundException;
import io.hexlet.typoreporter.handler.exception.NewPasswordTheSameException;
import io.hexlet.typoreporter.handler.exception.OldPasswordWrongException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class AccountService implements SignupAccountUseCase, QueryAccount {

    private final AccountRepository accountRepository;

    private final AccountMapper accountMapper;

    private final WorkspaceRoleMapper workspaceRoleMapper;

    private final PasswordEncoder passwordEncoder;

    private final WorkspaceRoleRepository workspaceRoleRepository;

    @Transactional(readOnly = true)
    public boolean existsByUsername(String username) {
        return accountRepository.existsByUsername(username);
    }

    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return accountRepository.existsByEmail(email);
    }

    @Override
    public InfoAccount signup(SignupAccount signupAccount) throws UsernameAlreadyExistException,
        EmailAlreadyExistException {
        final String normalizedEmail = TextUtils.toLowerCaseData(signupAccount.email());
        final String normalizedUsername = TextUtils.toLowerCaseData(signupAccount.username());
        if (existsByEmail(normalizedEmail)) {
            throw new EmailAlreadyExistException(normalizedEmail);
        }
        if (existsByUsername(normalizedUsername)) {
            throw new UsernameAlreadyExistException(normalizedUsername);
        }
        final var accToSave = accountMapper.toAccount(signupAccount);
        accToSave.setEmail(normalizedEmail);
        accToSave.setUsername(normalizedUsername);
        accToSave.setPassword(passwordEncoder.encode(signupAccount.password()));
        accToSave.setAuthProvider(AuthProvider.EMAIL);
        accountRepository.save(accToSave);
        return accountMapper.toInfoAccount(accToSave);
    }

    @Transactional(readOnly = true)
    public InfoAccount getInfoAccount(final String email) {
        return accountRepository.findAccountByEmail(email)
            .map(accountMapper::toInfoAccount)
            .orElseThrow(() -> new AccountNotFoundException(email));
    }

    @Transactional(readOnly = true)
    public List<WorkspaceRoleInfo> getWorkspacesInfoListByEmail(final String email) {
        return workspaceRoleRepository.getWorkspaceRolesByAccountEmail(email).stream()
            .map(workspaceRoleMapper::toWorkspaceRoleInfo)
            .toList();
    }

    @Transactional(readOnly = true)
    public UpdateProfile getUpdateProfile(final String email) {
        return accountRepository.findAccountByEmail(email)
            .map(accountMapper::toUpdateProfile)
            .orElseThrow(() -> new AccountNotFoundException(email));
    }

    @Transactional(readOnly = true)
    public List<Account> findAll() {
        return accountRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Account findByEmail(String email) {
        return accountRepository.findAccountByEmail(email)
            .orElseThrow(() -> new AccountNotFoundException(email));
    }

    public Account updateProfile(final UpdateProfile updateProfile, final String email) {
        final var sourceAccount = accountRepository.findAccountByEmail(email)
            .orElseThrow(() -> new AccountNotFoundException(email));
        final String sourceUserName = sourceAccount.getUsername();
        final String normalizedUserName = TextUtils.toLowerCaseData(updateProfile.getUsername());
        final String normalizedEmail = TextUtils.toLowerCaseData(updateProfile.getEmail());
        if (!sourceUserName.equals(normalizedUserName) && existsByUsername(normalizedUserName)) {
            throw new AccountAlreadyExistException("username", normalizedUserName);
        }
        final String sourceEmail = sourceAccount.getEmail();
        if (!sourceEmail.equals(normalizedEmail) && existsByEmail(normalizedEmail)) {
            throw new AccountAlreadyExistException("email", normalizedEmail);
        }
        Account updAccount = accountMapper.toAccount(updateProfile, sourceAccount);
        updAccount.setUsername(normalizedUserName);
        updAccount.setEmail(normalizedEmail);
        accountRepository.save(updAccount);
        return updAccount;
    }

    public Account updatePassword(final UpdatePassword updatePassword, final String email) {
        final var sourceAccount = accountRepository.findAccountByEmail(email)
            .orElseThrow(() -> new AccountNotFoundException(email));
        final String password = sourceAccount.getPassword();
        if (!passwordEncoder.matches(updatePassword.getOldPassword(), password)) {
            throw new OldPasswordWrongException();
        }
        if (passwordEncoder.matches(updatePassword.getNewPassword(), password)) {
            throw new NewPasswordTheSameException();
        }
        final String newPassword = passwordEncoder.encode(updatePassword.getNewPassword());
        sourceAccount.setPassword(newPassword);
        accountRepository.save(sourceAccount);
        return sourceAccount;
    }
}

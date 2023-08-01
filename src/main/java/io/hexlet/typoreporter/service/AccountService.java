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
import io.hexlet.typoreporter.web.exception.AccountAlreadyExistException;
import io.hexlet.typoreporter.web.exception.AccountNotFoundException;
import io.hexlet.typoreporter.web.exception.NewPasswordTheSameException;
import io.hexlet.typoreporter.web.exception.OldPasswordWrongException;
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
        return accountRepository.existsByUsernameIgnoreCase(username);
    }

    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return accountRepository.existsByEmailIgnoreCase(email);
    }

    @Override
    public InfoAccount signup(SignupAccount signupAccount) throws UsernameAlreadyExistException, EmailAlreadyExistException {
        if (existsByEmail(signupAccount.email())) {
            throw new EmailAlreadyExistException(signupAccount.email());
        }
        if (existsByUsername(signupAccount.username())) {
            throw new UsernameAlreadyExistException(signupAccount.username());
        }
        final var accToSave = accountMapper.toAccount(signupAccount);
        accToSave.setPassword(passwordEncoder.encode(signupAccount.password()));
        accToSave.setAuthProvider(AuthProvider.EMAIL);
        accountRepository.save(accToSave);
        return accountMapper.toInfoAccount(accToSave);
    }

    @Transactional(readOnly = true)
    public InfoAccount getInfoAccount(final String name) {
        return accountRepository.findAccountByUsernameIgnoreCase(name)
            .map(accountMapper::toInfoAccount)
            .orElseThrow(() -> new AccountNotFoundException(name));
    }

    @Transactional(readOnly = true)
    public List<WorkspaceRoleInfo> getWorkspacesInfoListByUsername(final String username) {
        return workspaceRoleRepository.getWorkspaceRolesByAccountUsername(username).stream()
            .map(workspaceRoleMapper::toWorkspaceRoleInfo)
            .toList();
    }

    @Transactional(readOnly = true)
    public UpdateProfile getUpdateProfile(final String name) {
        return accountRepository.findAccountByUsernameIgnoreCase(name)
            .map(accountMapper::toUpdateProfile)
            .orElseThrow(() -> new AccountNotFoundException(name));
    }

    @Transactional(readOnly = true)
    public List<Account> findAll() {
        return accountRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Account findByUsername(String userName) {
        return accountRepository.findAccountByUsernameIgnoreCase(userName)
            .orElseThrow(() -> new AccountNotFoundException(userName));
    }

    public Account updateProfile(final UpdateProfile updateProfile, final String name) {
        final var sourceAccount = accountRepository.findAccountByUsernameIgnoreCase(name)
            .orElseThrow(() -> new AccountNotFoundException(name));
        final String username = sourceAccount.getUsername();
        if (!username.equals(updateProfile.getUsername()) && existsByUsername(updateProfile.getUsername())) {
            throw new AccountAlreadyExistException("username", updateProfile.getUsername());
        }
        final String email = sourceAccount.getEmail();
        if (!email.equals(updateProfile.getEmail()) && existsByEmail(updateProfile.getEmail())) {
            throw new AccountAlreadyExistException("email", updateProfile.getEmail());
        }
        Account updAccount = accountMapper.toAccount(updateProfile, sourceAccount);
        accountRepository.save(updAccount);
        return updAccount;
    }

    public Account updatePassword(final UpdatePassword updatePassword, final String name) {
        final var sourceAccount = accountRepository.findAccountByUsernameIgnoreCase(name)
            .orElseThrow(() -> new AccountNotFoundException(name));
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

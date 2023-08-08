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
        return accountRepository.existsByUsername(username);
    }

    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return accountRepository.existsByEmail(email);
    }

    @Override
    public InfoAccount signup(SignupAccount signupAccount) throws UsernameAlreadyExistException,
        EmailAlreadyExistException {
        final String lowerCaseEmail = TextUtils.toLowerCaseData(signupAccount.email());
        final String lowerCaseUserName = TextUtils.toLowerCaseData(signupAccount.username());
        if (existsByEmail(lowerCaseEmail)) {
            throw new EmailAlreadyExistException(lowerCaseEmail);
        }
        if (existsByUsername(lowerCaseUserName)) {
            throw new UsernameAlreadyExistException(lowerCaseUserName);
        }
        final var accToSave = accountMapper.toAccount(signupAccount);
        accToSave.setEmail(lowerCaseEmail);
        accToSave.setUsername(lowerCaseUserName);
        accToSave.setPassword(passwordEncoder.encode(signupAccount.password()));
        accToSave.setAuthProvider(AuthProvider.EMAIL);
        accountRepository.save(accToSave);
        return accountMapper.toInfoAccount(accToSave);
    }

    @Transactional(readOnly = true)
    public InfoAccount getInfoAccount(final String name) {
        return accountRepository.findAccountByUsername(name)
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
        return accountRepository.findAccountByUsername(name)
            .map(accountMapper::toUpdateProfile)
            .orElseThrow(() -> new AccountNotFoundException(name));
    }

    @Transactional(readOnly = true)
    public List<Account> findAll() {
        return accountRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Account findByUsername(String userName) {
        return accountRepository.findAccountByUsername(userName)
            .orElseThrow(() -> new AccountNotFoundException(userName));
    }

    public Account updateProfile(final UpdateProfile updateProfile, final String name) {
        final var sourceAccount = accountRepository.findAccountByUsername(name)
            .orElseThrow(() -> new AccountNotFoundException(name));
        final String sourceUserName = sourceAccount.getUsername();
        final String updatedUserName = TextUtils.toLowerCaseData(updateProfile.getUsername());
        final String updatedEmail = TextUtils.toLowerCaseData(updateProfile.getEmail());
        if (!sourceUserName.equals(updatedUserName) && existsByUsername(updatedUserName)) {
            throw new AccountAlreadyExistException("username", updatedUserName);
        }
        final String sourceEmail = sourceAccount.getEmail();
        if (!sourceEmail.equals(updatedEmail) && existsByEmail(updatedEmail)) {
            throw new AccountAlreadyExistException("email", updatedEmail);
        }
        Account updAccount = accountMapper.toAccount(updateProfile, sourceAccount);
        updAccount.setUsername(updatedUserName);
        updAccount.setEmail(updatedEmail);
        accountRepository.save(updAccount);
        return updAccount;
    }

    public Account updatePassword(final UpdatePassword updatePassword, final String name) {
        final var sourceAccount = accountRepository.findAccountByUsername(name)
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

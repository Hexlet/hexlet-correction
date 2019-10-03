package io.hexlet.hexletcorrection.service.impl;

import io.hexlet.hexletcorrection.domain.Account;
import io.hexlet.hexletcorrection.repository.AccountRepository;
import io.hexlet.hexletcorrection.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public Optional<Account> findById(Long id) {
        return accountRepository.findById(id);
    }

    @Override
    public Optional<Account> findByEmail(String email) {
        return accountRepository.findOneByEmail(email);
    }

    @Override
    public List<Account> findByName(String name) {
        return accountRepository.findByName(name);
    }

    @Override
    public List<Account> findAll() {
        return accountRepository.findAll();
    }

    @Override
    public Account create(Account account) {
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        return accountRepository.save(account);
    }

    @Override
    public void delete(Long id) {
        accountRepository.deleteById(id);
    }
}

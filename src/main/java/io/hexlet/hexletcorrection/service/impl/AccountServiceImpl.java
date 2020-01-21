package io.hexlet.hexletcorrection.service.impl;

import io.hexlet.hexletcorrection.domain.Account;
import io.hexlet.hexletcorrection.repository.AccountRepository;
import io.hexlet.hexletcorrection.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public Optional<Account> findById(Long id) {
        return accountRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Account> findByEmail(String email) {
        return accountRepository.findOneByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Account> findByName(String name) {
        return accountRepository.findByName(name);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Account> findAll() {
        return accountRepository.findAll();
    }

    @Override
    public Account create(Account account) {
        if (account.getId() != null) {
            Account accountToPut = accountRepository.findById(account.getId()).orElseThrow();
            accountToPut.setName(account.getName());
            accountToPut.setEmail(account.getEmail());
            return accountRepository.save(accountToPut);
        }
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        return accountRepository.save(account);
    }

    @Override
    public void delete(Long id) {
        try {
            accountRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            log.info("Delete non existing entity with id=" + id, e);
        }
    }
}

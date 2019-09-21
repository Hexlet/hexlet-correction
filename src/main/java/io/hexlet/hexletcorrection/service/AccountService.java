package io.hexlet.hexletcorrection.service;

import io.hexlet.hexletcorrection.domain.Account;

import java.util.List;
import java.util.Optional;

public interface AccountService {

    Optional<Account> findById(Long id);

    Optional<Account> findByEmail(String email);

    List<Account> findByName(String name);

    List<Account> findAll();

    Account create(Account account);

    void delete(Long id);
}

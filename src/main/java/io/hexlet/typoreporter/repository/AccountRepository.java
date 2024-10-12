package io.hexlet.typoreporter.repository;

import io.hexlet.typoreporter.domain.account.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findAccountByEmail(String email);

    Optional<Account> findAccountByUsername(String username);

    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}

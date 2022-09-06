package io.hexlet.typoreporter.repository;

import io.hexlet.typoreporter.domain.account.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.hexlet.typoreporter.domain.typo.*;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.*;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findAccountByEmail(String email);

    Page<Account> findPageAccountByWorkspaceName(Pageable pageable, String name);

}

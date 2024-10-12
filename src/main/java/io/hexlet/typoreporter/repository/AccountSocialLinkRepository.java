package io.hexlet.typoreporter.repository;

import io.hexlet.typoreporter.domain.AccountSocialLink;
import io.hexlet.typoreporter.domain.account.AuthProvider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountSocialLinkRepository extends JpaRepository<AccountSocialLink, Long> {
    Optional<AccountSocialLink> findAccountSocialLinkByLoginAndAuthProvider(Integer login, AuthProvider authProvider);
}

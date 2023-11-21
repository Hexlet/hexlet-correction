package io.hexlet.typoreporter.security.service;

import io.hexlet.typoreporter.repository.AccountRepository;
import io.hexlet.typoreporter.utils.TextUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountDetailService implements UserDetailsService {

    private final AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        final String normalizedEmail = TextUtils.toLowerCaseData(email);
        return accountRepository.findAccountByEmail(normalizedEmail)
            .map(acc -> User.withUsername(acc.getEmail())
                .password(acc.getPassword())
                .authorities("USER")
                .build())
            .orElseThrow(() -> new UsernameNotFoundException("Account with email='" + email + "' not found"));
    }
}

package io.hexlet.typoreporter.security.service;

import io.hexlet.typoreporter.repository.AccountRepository;
import io.hexlet.typoreporter.service.dto.account.CustomUserDetails;
import io.hexlet.typoreporter.utils.TextUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;

@Service
@RequiredArgsConstructor
public class AccountDetailService implements UserDetailsService {

    private final AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        final String normalizedEmail = TextUtils.toLowerCaseData(email);
        return accountRepository.findAccountByEmail(normalizedEmail)
            .map(acc -> {
                Collection<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("USER"));
                return new CustomUserDetails(acc.getEmail(), acc.getPassword(), acc.getUsername(), authorities);
            })
            .orElseThrow(() -> new UsernameNotFoundException("Account with email='" + email + "' not found"));
    }
}

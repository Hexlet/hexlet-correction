package io.hexlet.typoreporter.service.account;

import io.hexlet.typoreporter.repository.AccountRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class AccountDetailsService implements UserDetailsService {

    private final AccountRepository accountRepository;
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("USER"));

        final var account = accountRepository.findAccountByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("Account with email='" + email + "' not found"));

        return new User(email, account.getPassword(), authorities);
    }
}

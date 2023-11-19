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
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final String normalizedUsername = TextUtils.toLowerCaseData(username);
        //my add
//        return accountRepository.findAccountByUsername(normalizedUsername)
//            .map(acc -> User.withUsername(acc.getUsername())
        return accountRepository.findAccountByEmail(normalizedUsername)
            .map(acc -> User.withUsername(acc.getEmail())
        //my add end
                .password(acc.getPassword())
                .authorities("USER")
                .build())
            .orElseThrow(() -> new UsernameNotFoundException("Account with name='" + username + "' not found"));
    }
}

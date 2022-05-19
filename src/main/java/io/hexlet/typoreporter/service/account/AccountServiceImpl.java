package io.hexlet.typoreporter.service.account;


import io.hexlet.typoreporter.domain.account.Account;
import io.hexlet.typoreporter.repository.AccountRepository;
import io.hexlet.typoreporter.service.dto.account.AccountRegistration;
import lombok.AllArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class AccountServiceImpl implements UserDetailsService, AccountService {

    private AccountRepository accountRepository;

//    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("USER"));

        final var account = accountRepository.findAccountByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("Account with email='" + email + "' not found"));

        return new org.springframework.security.core.userdetails.User(email, account.getPassword(), authorities);
    }

    @Override
    @Transactional
    public Account registerAccount(final AccountRegistration accountRegistration) {
        Account newAccount = new Account();
        return fillInAccount(accountRegistration, newAccount);
    }

    private Account fillInAccount(AccountRegistration accountRegistration, Account account) {
        account.setFirstName(accountRegistration.getFirstName())
            .setLastName(accountRegistration.getLastName())
            .setUsername(accountRegistration.getUsername())
            .setAuthProvider(accountRegistration.getAuthProvider())
            .setExternalOpenId(accountRegistration.getExternalOpenId())
//            .setPassword(passwordEncoder.encode(accountRegistration.getPassword()))
            .setPassword(accountRegistration.getPassword())
            .setEmail(accountRegistration.getEmail());
        return accountRepository.save(account);
    }
}

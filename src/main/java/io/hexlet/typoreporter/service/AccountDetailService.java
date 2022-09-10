package io.hexlet.typoreporter.service;

import io.hexlet.typoreporter.domain.account.Account;
import io.hexlet.typoreporter.domain.account.AuthProvider;
import io.hexlet.typoreporter.repository.AccountRepository;
import io.hexlet.typoreporter.service.converter.CreateAccountToAccount;
import io.hexlet.typoreporter.service.dto.account.CreateAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountDetailService implements UserDetailsService {

    @Autowired
    private AccountRepository accountRepository;

    // @Autowired
    // BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private CreateAccountToAccount converter;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // TODO Roles
        List<SimpleGrantedAuthority> authorityList = List.of(new SimpleGrantedAuthority("ROLE_USER"));

        Account account = accountRepository.findAccountByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User " + username + " not found"));

        return new User(account.getUsername(), account.getPassword(), authorityList);
    }

    public boolean saveAccount(Account account) {
        Account accountFromDB = accountRepository.findAccountByUsername(account.getUsername())
            .orElse(null);

        if (accountFromDB != null) {
            return false;
        }

        // account.setRoles(Collections.singleton(new Role(1L, "ROLE_USER"))); // TODO make roles
        // account.setPassword(bCryptPasswordEncoder.encode(account.getPassword())); // TODO modify when encoded in frontend
        accountRepository.save(account);
        return true;
    }

    public boolean saveAccount(CreateAccount source) {
        Account sourceAccount = converter.convert(source);
        if (sourceAccount == null) {
            return false;
        }
        sourceAccount.setAuthProvider(AuthProvider.EMAIL); // TODO remove hard-coded stuff
        sourceAccount.setExternalOpenId("1L");

        return saveAccount(sourceAccount);
    }

    // TODO implement allAccounts, deleteAccount, findAccountById, updateAccount
}

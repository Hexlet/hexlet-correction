package io.hexlet.typoreporter.security.provider;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AccountAuthenticationProvider implements AuthenticationProvider {

    private final PasswordEncoder encoder;

    private final UserDetailsService service;

    public AccountAuthenticationProvider(@Qualifier("accountDetailService") UserDetailsService service,
                                         @Qualifier("bCryptPasswordEncoder") PasswordEncoder encoder) {
        this.encoder = encoder;
        this.service = service;
    }

    @Override
    public Authentication authenticate(Authentication auth) throws AuthenticationException {
        final String username = auth.getName();
        final UserDetails securedAccount = service.loadUserByUsername(username);
        final String password = auth.getCredentials().toString();
        if (encoder.matches(password, securedAccount.getPassword())) {
            return new UsernamePasswordAuthenticationToken(username, password, securedAccount.getAuthorities());
        }
        // if (password.equals(securedAccount.getPassword())) {
        //     return new UsernamePasswordAuthenticationToken(username, password, securedAccount.getAuthorities());
        // }
        throw new BadCredentialsException("BadCredentialsException");
    }

    @Override
    public boolean supports(Class<?> auth) {
        return auth.equals(UsernamePasswordAuthenticationToken.class);
    }
}

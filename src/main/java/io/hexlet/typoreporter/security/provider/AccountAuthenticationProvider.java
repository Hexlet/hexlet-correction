package io.hexlet.typoreporter.security.provider;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AccountAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

    private final PasswordEncoder encoder;

    private final UserDetailsService service;
    private volatile String userNotFoundEncodedPassword;

    public AccountAuthenticationProvider(@Qualifier("accountDetailService") UserDetailsService service,
                                         @Qualifier("bCryptPasswordEncoder") PasswordEncoder encoder) {
        this.encoder = encoder;
        this.service = service;
    }

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails,
                                                  UsernamePasswordAuthenticationToken auth) throws AuthenticationException {
      if (auth.getCredentials() == null) {
          throw new BadCredentialsException("Failed to authenticate. No credentials provided");
      }

      final String password = auth.getCredentials().toString();
      if (!encoder.matches(password, userDetails.getPassword())) {
          throw new BadCredentialsException("Failed to authenticate. Password is wrong.");
      }
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
    protected UserDetails retrieveUser(String username,
                                       UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        prepareTimingAttackProtection();

        try {
            final UserDetails securedAccount = service.loadUserByUsername(username);
            if (securedAccount == null) {
                throw new InternalAuthenticationServiceException("UserDetailsService returned null, which is an interface contract violation");
            }
            return securedAccount;
        } catch (UsernameNotFoundException var4) {
            mitigateAgainstTimingAttack(authentication);
            throw var4;
        } catch (InternalAuthenticationServiceException var5) {
            throw var5;
        } catch (Exception var6) {
            throw new InternalAuthenticationServiceException(var6.getMessage(), var6);
        }
    }

    @Override
    public boolean supports(Class<?> auth) {
        return auth.equals(UsernamePasswordAuthenticationToken.class);
    }

    private void prepareTimingAttackProtection() {
        if (userNotFoundEncodedPassword == null) {
            userNotFoundEncodedPassword = encoder.encode("userNotFoundPassword");
        }

    }

    private void mitigateAgainstTimingAttack(UsernamePasswordAuthenticationToken authentication) {
        if (authentication.getCredentials() != null) {
            String presentedPassword = authentication.getCredentials().toString();
            encoder.matches(presentedPassword, this.userNotFoundEncodedPassword);
        }

    }
}

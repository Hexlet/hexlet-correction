package io.hexlet.typoreporter.security.provider;

import io.hexlet.typoreporter.security.authentication.WorkspaceAuthenticationToken;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class WorkspaceTokenAuthenticationProvider implements AuthenticationProvider {

    private final PasswordEncoder passwordEncoder = new PasswordEncoder() {
        @Override
        public String encode(CharSequence rawPassword) {
            return rawPassword.toString();
        }

        @Override
        public boolean matches(CharSequence rawPassword, String encodedPassword) {
            return rawPassword.toString().equals(encodedPassword);
        }
    };

    private final UserDetailsService service;

    public WorkspaceTokenAuthenticationProvider(@Qualifier("securedWorkspaceService") UserDetailsService service) {
        this.service = service;
    }

    @Override
    public Authentication authenticate(Authentication auth) throws AuthenticationException {
        final var workspaceName = auth.getName();
        final var workspace = service.loadUserByUsername(workspaceName);
        final var token = auth.getCredentials().toString();
        if (passwordEncoder.matches(token, workspace.getPassword())) {
            return new WorkspaceAuthenticationToken(workspaceName, token, workspace.getAuthorities());
        }
        throw new BadCredentialsException("BadCredentialsException");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return WorkspaceAuthenticationToken.class.equals(authentication);
    }
}

package io.hexlet.typoreporter.security.provider;

import io.hexlet.typoreporter.security.authentication.WorkspaceAuthenticationToken;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class WorkspaceTokenAuthenticationProvider implements AuthenticationProvider {

    private final PasswordEncoder passwordEncoder = NoOpPasswordEncoder.getInstance();

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
